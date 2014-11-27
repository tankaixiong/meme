package tank.meme.core;

import tank.meme.core.net.socket.Request;
import tank.meme.core.net.socket.Response;
import tank.meme.core.net.socket.SocketSession;

/**
 * 消息处理接口
 *
 * @author tank
 * @version 1.0
 * @date Mar 11, 2013 11:58:45 AM
 */
public abstract class MessageHandler implements IMessageHandler {

	private SocketSession session;
	private Request request;

	@Override
	public void setResponse(Request request) {
		this.request = request;
	}

	@Override
	public void setSession(SocketSession session) {
		this.session = session;
	}

	public SocketSession getSession() {
		return this.session;
	}

	public Request getRequest() {
		return this.request;
	}

	public void write(Object[] data) {
		Response response = new Response();
		response.setAct(this.getHandlerName());
		response.setData(data);

		session.write(response);
	}
}
