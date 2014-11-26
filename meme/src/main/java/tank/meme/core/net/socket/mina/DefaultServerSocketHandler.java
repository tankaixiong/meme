package tank.meme.core.net.socket.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import tank.meme.cache.RedisSupport;
import tank.meme.core.net.socket.SocketSession;

/**
 * @author tank
 * @version :1.0
 * @date:Oct 24, 2012 10:21:07 AM
 * @description:消息处理器
 */
@Controller
public class DefaultServerSocketHandler extends IoHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerSocketHandler.class);

	private static final int QUEUE_NUM = 4;

	/**
	 * 当一个客户端连接进入时
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LOGGER.trace("session Opened");
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 120);
		// 加入session管理
		tank.meme.core.net.socket.SessionManager.getInstance().addSession(new SocketSession(session));
		// TODO:这里定义 event事件进行触发相关监听

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
		session.close(true);
	}

	/**
	 * 当接收到客户端的信息
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		LOGGER.trace("message Received:{}",message);

		// 这里存到redis列队中进行处理

		String llistKeyString = "msl_" + (session.getId() % QUEUE_NUM);

		//RedisSupport.getInstance().lpush(llistKeyString, (String) message);

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

		LOGGER.trace("【返回数据包】: {}", message);

		super.messageSent(session, message);
	}

}
