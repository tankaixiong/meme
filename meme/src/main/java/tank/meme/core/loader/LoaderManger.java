package tank.meme.core.loader;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import tank.meme.core.Application;
import tank.meme.core.BaseMsgDispatcher;
import tank.meme.core.IMessageHandler;
import tank.meme.core.constant.ApplicationProperties;
import tank.meme.core.event.ApplicationAfterStartEvent;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月20日 下午3:08:03
 * @description:热加载
 * @version :0.1
 */
@Component
public class LoaderManger implements ApplicationListener<ApplicationAfterStartEvent> {

	private static Logger logger = LoggerFactory.getLogger(LoaderManger.class);

	/**
	 * 加载所有的业务JAR到环境中
	 */
	public void loadSdkJar() {
		urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

		String sdkPath = Application.getInstance().getProperties().getString(ApplicationProperties.SDK_PATH);
		sdkPath = sdkPath.replaceAll("////", "\\");
		File file = new File(sdkPath);
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (File f : files) {
				if (f.isFile()) {
					try {
						URL url = f.toURI().toURL();
						// 加载JAR包
						addUrl(url);
						// 注册bean
						parseJarClass(f.getAbsolutePath());

					} catch (Exception e) {
						logger.error("{}", e);
					}
				}
			}
		}
	}
	/**
	 * 重新加载jar，应用于jar的更新
	 * @param f
	 */
	public static void reLoaderJar(File f) {
		try {
			URL url = f.toURI().toURL();
			// 加载JAR包
			addUrl(url);
			// 注册bean
			parseJarClass(f.getAbsolutePath());

		} catch (Exception e) {
			logger.error("{}", e);
		}
	}

	/**
	 * 让系统级别的classloader去加载
	 * 
	 * @param url
	 */
	public static void addUrl(URL url) {
		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			boolean accessible = method.isAccessible();
			try {
				if (accessible == false) {
					method.setAccessible(true);
				}
				// 设置类加载器
				URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
				// 将当前类路径加入到类加载器中
				method.invoke(classLoader, url);
			} finally {
				method.setAccessible(accessible);
			}
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}

	/**
	 * 解析jar中的class并注册到spring环境中
	 */
	public static void parseJarClass(String jarPath) {
		Set<String> handlerBeanName = new HashSet<String>();
		try {
			JarFile jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();

				if (jarEntry.getName().endsWith(".class")) {

					String jarClassName = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf("."));
					String className = jarClassName.replace('/', '.');

					Class clazz = urlClassLoader.loadClass(className);

					// if (!clazz.isInterface()) {
					// registerBean(clazz);
					// }

					Annotation[] annotationArray = clazz.getDeclaredAnnotations();

					for (Annotation annotation : annotationArray) {
						// 判断是否是spring bean对象再注册
						if (annotation instanceof Repository || annotation instanceof Service || annotation instanceof Controller
								|| annotation instanceof Component) {
							String handlerName = registerBean(clazz);
							if (handlerName != null) {
								handlerBeanName.add(handlerName);
							}
						}
					}

				}

			}
		} catch (Exception e) {
			logger.error("{}", e);
		}

		AbstractApplicationContext applicationContext = Application.getInstance().getApplicationContext();
		for (String className : handlerBeanName) {
			IMessageHandler msgHandler = (IMessageHandler) applicationContext.getBean(className);

			if (msgHandler != null) {
				BaseMsgDispatcher.messageHandler.put(msgHandler.getHandlerName(), msgHandler);
				logger.info("动态加载业务:{}", msgHandler.getHandlerName());
			}
		}

	}

	/**
	 * 手动注册类到spring容器的环境中
	 * 
	 * @param clazz
	 */
	public static String registerBean(Class clazz) {
		AbstractApplicationContext applicationContext = Application.getInstance().getApplicationContext();

		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
		GenericBeanDefinition messageSourceDefinition = new GenericBeanDefinition();

		// 参数参考上面配置文件里面
		// Map<String, String> original = new HashMap<String, String>();
		// original.put("basenames", "messages");
		// original.put("useCodeAsDefaultMessage", "true");

		messageSourceDefinition.setBeanClass(clazz);
		// messageSourceDefinition.setPropertyValues(new MutablePropertyValues(original));

		// String className = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
		String className = clazz.getSimpleName();
		logger.info("注册 bean:{} 到spring 容器", className);

		if (beanFactory.containsBean(className)) {
			beanFactory.removeBeanDefinition(className);
		}

		// 注册
		beanFactory.registerBeanDefinition(className, messageSourceDefinition);
		// 获取
		// ResourceBundleMessageSource messageSource = (ResourceBundleMessageSource) applicationContext.getBean("ResourceBundleMessageSource");
		// 测试
		// System.out.println(messageSource.getMessage("test", null, null));

		try {
			if (!clazz.isInterface()) {
				Object obj = clazz.newInstance();
				if (obj instanceof IMessageHandler) {
					return className;
				}
				obj = null;

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{}", e);
		}
		return null;
	}

	protected static URLClassLoader urlClassLoader = null;

	 

	@Override
	public void onApplicationEvent(ApplicationAfterStartEvent event) {
		loadSdkJar();// 加载JAR

		new LoaderTimer().start();// 监听JAR，并及时更新
	}

}
