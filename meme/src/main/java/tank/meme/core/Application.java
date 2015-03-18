package tank.meme.core;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import tank.meme.core.constant.SessionConstant;
import tank.meme.core.event.ApplicationAfterStartEvent;

/**
 * @author tank
 * @date:2014年11月24日 上午12:14:11
 * @description:
 * @version :1.0
 */
public class Application implements ApplicationListener<ContextClosedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	private static Application application;
	private AbstractApplicationContext appContext;
	private PropertiesConfiguration propertiesConfiguration = null;

	public static int serverId = -1;

	private Application() {

	}

	public void loadConfig() {
		try {
			propertiesConfiguration = new PropertiesConfiguration("application.properties");
			serverId = propertiesConfiguration.getInt("server.id");
		} catch (ConfigurationException e) {
			LOGGER.error("没有找到默认配置文件application.properties:{}", e);
		}
	}

	public AbstractApplicationContext getApplicationContext() {
		return this.appContext;
	}

	public static Application getInstance() {
		if (application == null) {
			synchronized (Application.class) {
				if (application == null) {
					application = new Application();
				}
			}
		}
		return application;
	}

	public void init(AbstractApplicationContext appContext) {
		this.appContext = appContext;
		this.appContext.addApplicationListener(this);
		// 加载application.properties配置文件
		loadConfig();

		appContext.publishEvent(new ApplicationAfterStartEvent("appstarted"));
	}

	public PropertiesConfiguration getProperties() {
		return propertiesConfiguration;
	}

	/**
	 * 执行用户列表的线程数
	 * 
	 * @return
	 */
	public int getThreadNum() {
		if (propertiesConfiguration != null) {
			return propertiesConfiguration.getInt("msg.thread.num");
		} else {
			return Runtime.getRuntime().availableProcessors() + 1;
		}
	}
	/**
	 *  得到线程key
	 * @param threadNum
	 * @return
	 */
	public String getQueueKey(int threadNum) {
		String key = "msl:" + Application.serverId + ":" + threadNum;
		return key;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		LOGGER.info("spring close 程序");
		// TODO:销毁资源
	}

	/**
	 * 发布事件
	 * 
	 * @param event
	 */
	public void publishEvent(ApplicationEvent event) {
		this.appContext.publishEvent(event);
	}
}
