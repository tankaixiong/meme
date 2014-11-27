package tank.meme.core.event;

import org.springframework.context.ApplicationEvent;

import tank.meme.core.net.socket.SocketSession;

/**
 * @author tank
 * @date:27 Nov 2014 13:10:29
 * @description:应用启动之后事件
 * @version :1.0
 */
public final class ApplicationAfterStartEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6512740587036409799L;

	public ApplicationAfterStartEvent(Object source) {
		super(source);
	}

}
