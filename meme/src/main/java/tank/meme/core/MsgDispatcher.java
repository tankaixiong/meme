package tank.meme.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import tank.meme.cache.RedisSupport;
import tank.meme.core.event.ApplicationAfterStartEvent;
import tank.meme.core.net.socket.Request;
import tank.meme.core.net.socket.SessionManager;
import tank.meme.core.net.socket.SocketSession;
import tank.meme.utils.JsonUtils;

/**
 * @author tank
 * @date:27 Nov 2014 14:15:27
 * @description:消息分发
 * @version :1.0
 */
@Component
public class MsgDispatcher implements ApplicationListener<ApplicationAfterStartEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MsgDispatcher.class);

	private static final int POOL_SIZE = Application.getInstance().getThreadNum();

	private Map<String, IMessageHandler> messageHandler = new HashMap<String, IMessageHandler>();

	/**
	 * 读取线程的列队
	 */
	public void init() {
		LOGGER.info("当前线程数:{}", POOL_SIZE);
		ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
		for (int i = 0; i < POOL_SIZE; i++) {
			pool.execute(new MsgBee(i));
		}
	}

	private class MsgBee extends Thread {
		private int threadId;

		public MsgBee(int threadId) {
			this.threadId = threadId;
		}

		@Override
		public void run() {

			try {
				while (true) {
					try {
						List<String> list = RedisSupport.getInstance().blpop(Application.getInstance().getQueueKey(threadId));
						if (list != null && !list.isEmpty()) {
							Long sessionId = Long.parseLong(list.get(0));
							String json = list.get(1);

							Request request = JsonUtils.toBean(json, Request.class);

							SocketSession session = SessionManager.getInstance().getSession(sessionId);

							IMessageHandler handler = messageHandler.get(request.getAct());
							handler.handler(session, request);

						}
					} catch (Exception e) {
						LOGGER.error("{}", e);
					}
				}
			} catch (Exception e) {
				LOGGER.error("{}", e);
			}

		}
	}

	@Override
	public void onApplicationEvent(ApplicationAfterStartEvent event) {
		LOGGER.info("初始化所有handler");

		Map<String, IMessageHandler> handlers = Application.getInstance().getApplicationContext().getBeansOfType(IMessageHandler.class);
		Iterator<IMessageHandler> handlerIt = handlers.values().iterator();
		while (handlerIt.hasNext()) {
			IMessageHandler item = handlerIt.next();
			LOGGER.trace("act:{},handler:{}", item.getHandlerName(), item.getClass());
			messageHandler.put(item.getHandlerName(), item);
		}

		// 初始化列队消耗线程
		LOGGER.info("初始化处理队列线程");
		init();
	}

}
