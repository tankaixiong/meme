package tank.meme.core.loader;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import tank.meme.core.Application;
import tank.meme.service.IUserService;
import tank.meme.service.impl.UserServiceImpl;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月20日 下午3:08:03
 * @description:热加载
 * @version :0.1
 */

public class LoaderManger {

	private static Logger logger = LoggerFactory.getLogger(LoaderManger.class);

	public static void main(String[] args) {

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
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public static void parseJarClass(String jarPath) {

		try {
			JarFile jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();

				if (jarEntry.getName().endsWith(".class")) {

					String jarClassName = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf("."));
					String className = jarClassName.replace('/', '.');

					Class clazz = urlClassLoader.loadClass(className);

					Annotation[] annotationArray = clazz.getDeclaredAnnotations();

					for (Annotation annotation : annotationArray) {
						// 判断是否是spring bean对象再注册
						if (annotation instanceof Repository || annotation instanceof Service || annotation instanceof Controller
								|| annotation instanceof Component) {
							registerBean(clazz);
						}
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 手动注册类到spring容器的环境中
	 * @param clazz
	 */
	public static void registerBean(Class clazz) {
		AbstractApplicationContext applicationContext = Application.getInstance().getApplicationContext();

		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
		GenericBeanDefinition messageSourceDefinition = new GenericBeanDefinition();

		// 参数参考上面配置文件里面
		// Map<String, String> original = new HashMap<String, String>();
		// original.put("basenames", "messages");
		// original.put("useCodeAsDefaultMessage", "true");

		messageSourceDefinition.setBeanClass(clazz);
		// messageSourceDefinition.setPropertyValues(new MutablePropertyValues(original));
		
		//String className = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
		String className = clazz.getSimpleName();
		logger.info(className);
		// 注册
		beanFactory.registerBeanDefinition(className, messageSourceDefinition);
		// 获取
		// ResourceBundleMessageSource messageSource = (ResourceBundleMessageSource) applicationContext.getBean("ResourceBundleMessageSource");
		// 测试
		// System.out.println(messageSource.getMessage("test", null, null));
	}

	protected static URLClassLoader urlClassLoader = null;

	public static void loadJar() {

		try {
			File file = new File("C:\\Users\\tank\\Desktop\\jar\\service.jar");
			URL url = file.toURI().toURL();

			urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			addUrl(url);
			// urlClassLoader = new URLClassLoader(new URL[] { url });

			parseJarClass(file.getPath());

			IUserService service = (IUserService) Application.getInstance().getApplicationContext().getBean("UserServiceImpl");
			 
			service.login("aaa", "123");

			// Class clazz = urlClassLoader.loadClass("tank.meme.service.impl.UserServiceImpl");
			//
			// Object obj = clazz.newInstance();
			//
			// Method login = clazz.getMethod("login", String.class, String.class);
			//
			// System.out.println(login.invoke(obj, "aaa", "123"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
