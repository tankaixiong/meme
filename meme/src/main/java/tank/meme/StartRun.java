package tank.meme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tank.meme.core.Application;

/**
 * 程序入口类
 * 
 * @author tank
 *
 */
public class StartRun {
	private static Logger logger = LoggerFactory.getLogger(StartRun.class);

	public static void main(String[] args) {

		final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:spring-context.xml");
		ctx.registerShutdownHook();

		ctx.start();
		
		// 初始化
		Application.getInstance().init(ctx); 

		logger.info("添加JVM关闭hook!");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("程序关闭");
				ctx.stop();
			}
		});
		
		
		 
	}

}
