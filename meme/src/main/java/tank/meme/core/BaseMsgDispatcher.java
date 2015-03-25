package tank.meme.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import tank.meme.core.constant.ApplicationProperties;
import tank.meme.core.event.ApplicationAfterStartEvent;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月18日 上午11:12:32
 * @description:
 * @version :0.1
 */
public class BaseMsgDispatcher implements ApplicationListener<ApplicationAfterStartEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseMsgDispatcher.class);

	public static Map<String, IMessageHandler> messageHandler = new HashMap<String, IMessageHandler>();

	protected ExecutorService pool;
	private static boolean isInited = false;

	public BaseMsgDispatcher() {

	}

	public boolean isDefaultMsgType() {
		String queueType = Application.getInstance().getProperties().getString(ApplicationProperties.MSG_QUEUE_TYPE);
		if ("redis".equals(queueType)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 读取线程的列队
	 */
	public void init() {

	}

	@Override
	public void onApplicationEvent(ApplicationAfterStartEvent event) {
		if (!isInited) {
			LOGGER.info("初始化所有handler");

			Map<String, IMessageHandler> handlers = Application.getInstance().getApplicationContext().getBeansOfType(IMessageHandler.class);
			Iterator<IMessageHandler> handlerIt = handlers.values().iterator();
			while (handlerIt.hasNext()) {
				IMessageHandler item = handlerIt.next();
				LOGGER.trace("act:{},handler:{}", item.getHandlerName(), item.getClass());
				messageHandler.put(item.getHandlerName(), item);
			}
			int POOL_SIZE = Application.getInstance().getThreadNum();
			// 初始化列队消耗线程
			LOGGER.info("初始化处理队列线程");
			LOGGER.info("当前线程数:{}", POOL_SIZE);
			pool = Executors.newFixedThreadPool(POOL_SIZE);

			init();
			isInited = true;
		}
	}

	public int getThreadNum() {
		return Application.getInstance().getThreadNum();
	}

}
