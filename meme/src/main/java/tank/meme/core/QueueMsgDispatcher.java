package tank.meme.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tank.meme.cache.RedisSupport;
import tank.meme.core.net.socket.Request;
import tank.meme.core.net.socket.SessionManager;
import tank.meme.core.net.socket.SocketSession;
import tank.meme.utils.JsonUtils;

/**
 * @author tank
 * @date:2015年3月18日 上午10:26:54
 * @description:基本内存中queue列队的消息分发
 * @version :0.1
 */
//@Component
public class QueueMsgDispatcher extends BaseMsgDispatcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueMsgDispatcher.class);

	public static Map<Integer, Queue<String>> msgQueueMap = new HashMap<Integer, Queue<String>>();

	// private Queue<String> msgQueue = new ConcurrentLinkedQueue<String>();

	/**
	 * 读取线程的列队
	 */
	@Override
	public void init() {
		if (isDefaultMsgType()) {

			LOGGER.info("当前线程数:{}", POOL_SIZE);
			for (int i = 0; i < POOL_SIZE; i++) {
				// 初始化消息列队，消息列队数与线程数一致
				msgQueueMap.put(i, new ConcurrentLinkedQueue<String>());

				pool.execute(new MsgBee(i));
			}
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

						Queue<String> queue = msgQueueMap.get(threadId);
						String data = queue.poll();
						if (data != null) {
							Long sessionId = Long.parseLong(data.substring(0, 8));
							String json = data.substring(8);

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

}
