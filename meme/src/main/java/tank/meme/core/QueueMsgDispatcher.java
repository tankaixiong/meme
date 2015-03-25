package tank.meme.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
@Component
public class QueueMsgDispatcher extends BaseMsgDispatcher {

	private static final Logger LOG = LoggerFactory.getLogger(QueueMsgDispatcher.class);

	public static Map<Integer, BlockingQueue<String>> msgQueueMap = new HashMap<Integer, BlockingQueue<String>>();

	// private Queue<String> msgQueue = new ConcurrentLinkedQueue<String>();

	/**
	 * 读取线程的列队
	 */
	@Override
	public void init() {
		if (isDefaultMsgType()) {
			int threadNum = getThreadNum();
			LOG.info("当前线程数:{}", threadNum);
			for (int i = 0; i < threadNum; i++) {
				// 初始化消息列队，消息列队数与线程数一致
				// msgQueueMap.put(i, new ConcurrentLinkedQueue<String>());
				msgQueueMap.put(i, new LinkedBlockingQueue<String>());// 更改为阻塞队列

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

						BlockingQueue<String> queue = msgQueueMap.get(threadId);
						// String data = queue.poll();
						String data = queue.take();
						if (data != null) {
							Long sessionId = Long.parseLong(data.substring(0, 8));
							String json = data.substring(8);

							Request request = JsonUtils.toBean(json, Request.class);

							SocketSession session = SessionManager.getInstance().getSession(sessionId);

							IMessageHandler handler = messageHandler.get(request.getAct());
							if (handler != null) {
								handler.handler(session, request);
							} else {
								LOG.error("没有找到相应的处理handler:{}", request.getAct());
							}

						}

					} catch (Exception e) {
						LOG.error("{}", e);
					}
				}
			} catch (Exception e) {
				LOG.error("{}", e);
			}

		}
	}

}
