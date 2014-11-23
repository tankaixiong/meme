package tank.meme.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @author tank
 * @date:2014年11月24日 上午12:14:11
 * @description:
 * @version :1.0
 */
public class Application implements ApplicationListener<ApplicationEvent> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Application.class);

	private Application() {

	}

	private static Application application;
	private AbstractApplicationContext appContext;

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
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ContextClosedEvent){
			LOGGER.info("spring close 程序");
		}else if(event instanceof ContextStoppedEvent){
			LOGGER.info("spring stop 程序");
		}
		
	}
}
