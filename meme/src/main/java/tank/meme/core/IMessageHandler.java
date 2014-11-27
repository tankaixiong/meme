package tank.meme.core;

import tank.meme.core.net.socket.Request;
import tank.meme.core.net.socket.SocketSession;

/**
 * 消息处理接口
 *
 * @author tank
 * @version 1.0
 * @date Mar 11, 2013 11:58:45 AM
 */
public interface IMessageHandler {

	/**
	 * 区分不同的消息类型处理模块
	 */
	public String getHandlerName();

	public void handler(SocketSession session, Request request);

	public void setSession(SocketSession session);

	public void setResponse(Request request);
}
