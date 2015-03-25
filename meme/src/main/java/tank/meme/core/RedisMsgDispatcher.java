package tank.meme.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tank.meme.core.net.socket.Request;
import tank.meme.core.net.socket.SessionManager;
import tank.meme.core.net.socket.SocketSession;
import tank.meme.core.redis.RedisSupport;
import tank.meme.utils.JsonUtils;

/**
 * @author tank
 * @date:27 Nov 2014 14:15:27
 * @description:基于redis 机制的消息分发
 * @version :1.0
 */
@Component
public class RedisMsgDispatcher extends BaseMsgDispatcher {
	private static final Logger LOG = LoggerFactory.getLogger(RedisMsgDispatcher.class);

	/**
	 * 读取线程的列队
	 */
	@Override
	public void init() {
		if (!isDefaultMsgType()) {
			int threadNum=getThreadNum();
			LOG.info("当前线程数:{}", threadNum);

			for (int i = 0; i < threadNum; i++) {
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
						List<String> list = RedisSupport.getInstance().blpop(Application.getInstance().getQueueKey(threadId));
						if (list != null && !list.isEmpty()) {
							// Long key = Long.parseLong(list.get(0));//得到的是reids key

							String data = list.get(1);
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
