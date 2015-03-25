package tank.meme.core.net.socket.mina;

import java.util.Queue;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.node.ArrayNode;

import tank.meme.core.Application;
import tank.meme.core.QueueMsgDispatcher;
import tank.meme.core.constant.ApplicationProperties;
import tank.meme.core.constant.SessionConstant;
import tank.meme.core.event.SessionCloseEvent;
import tank.meme.core.event.SessionOpenedEvent;
import tank.meme.core.net.socket.SocketSession;
import tank.meme.core.redis.RedisSupport;
import tank.meme.utils.JsonUtils;

/**
 * @author tank
 * @version :1.0
 * @date:Oct 24, 2012 10:21:07 AM
 * @description:消息处理器
 */
@Controller
public class DefaultServerSocketHandler extends IoHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerSocketHandler.class);

	private final int QUEUE_NUM;
	private Application application = Application.getInstance();
	private Boolean isRedisDispather = null;

	public DefaultServerSocketHandler() {
		QUEUE_NUM = Application.getInstance().getThreadNum();
	}

	/**
	 * 当一个客户端连接进入时
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LOGGER.trace("session Opened");
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 120);

		application.publishEvent(new SessionOpenedEvent("sessionOpened", new SocketSession(session)));

		super.sessionOpened(session);

	}

	/**
	 * 当一个客户端关闭时
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOGGER.trace("session Closed");
		tank.meme.core.net.socket.SessionManager.getInstance().removeSessionBySessionId(session.getId());
		// TODO:这里定义 event事件进行触发相关监听
		application.publishEvent(new SessionCloseEvent("sessionClosed", new SocketSession(session)));
		super.sessionClosed(session);
	}

	/**
	 * 异常时断开连接
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

		// if (cause instanceof IOException) {
		// LOGGER.error("IO异常直接断掉session");
		// session.close(true);
		// }

		LOGGER.error("服务端处理产生异常:{}", cause);
	}

	/**
	 * 超过设置的最大闲置时间，会每隔设置的时间调用
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

		LOGGER.info("空闲!自动断开");
		// session.close(true);
	}

	/**
	 * 当接收到客户端的信息
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		LOGGER.trace("message Received:{}", message);

		int queueNum = (int) (session.getId() % QUEUE_NUM);
		// 前8位为SESSION ID 后面才是真正传递的消息
		String sessionId = String.format("%08d", session.getId());

		// 读取配置文件确定采用哪种消息队列分发模式
		if (isRedisDispather == null) {
			String queueType = application.getProperties().getString(ApplicationProperties.MSG_QUEUE_TYPE);
			if ("redis".equals(queueType)) {
				isRedisDispather = true;
			} else {
				isRedisDispather = false;
			}
		}
		if (isRedisDispather) {
			// 这里存到redis列队中进行处理
			String redisListKey = application.getQueueKey(queueNum);// 取模得到应该所属列队ID

			// ArrayNode node = JsonUtils.objectMapper.createArrayNode();
			// node.add(session.getId());
			// node.add(String.valueOf(message));
			// RedisSupport.getInstance().rpush(redisListKey, node.toString());

			RedisSupport.getInstance().rpush(redisListKey, sessionId + String.valueOf(message));
		} else {

			// 内存列队处理
			Queue<String> queue = QueueMsgDispatcher.msgQueueMap.get(queueNum);
			queue.offer(sessionId + String.valueOf(message));
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

		LOGGER.trace("【返回数据包】: {}", message);

		super.messageSent(session, message);
	}

}
