package tank.meme.core.event;

import org.springframework.context.ApplicationEvent;

import tank.meme.core.net.socket.SocketSession;

/**
 * @author tank
 * @date:27 Nov 2014 13:10:29
 * @description:session 关闭事件
 * @version :1.0
 */
public final class SessionOpenedEvent extends ApplicationEvent {

	private SocketSession session;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6512740587036409799L;

	public SessionOpenedEvent(Object source) {
		super(source);
	}

	public SessionOpenedEvent(Object source, SocketSession session) {
		super(source);
		this.session = session;
	}

	public SocketSession getSession() {
		return session;
	}

	public void setSession(SocketSession session) {
		this.session = session;
	}

}
