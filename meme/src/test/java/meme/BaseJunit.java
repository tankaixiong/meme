package meme;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import tank.meme.core.Application;

/**
 * @author tank
 * @date:18 Nov 2014 16:12:49
 * @description:单元测试基类
 * @version :1.0
 */
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class BaseJunit extends AbstractJUnit4SpringContextTests {
	private Logger logger=LoggerFactory.getLogger(BaseJunit.class);
	@Before
	public void init() {
		logger.info("单元测试初始化...");
		Application.getInstance().init((AbstractApplicationContext)applicationContext);
	}

	public <T> T getBean(Class<T> type) {
		return applicationContext.getBean(type);
	}

	public Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	protected ApplicationContext getContext() {
		return applicationContext;
	}

}
