package tank.meme.core.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author tank
 * @date:27 Nov 2014 11:31:53
 * @description:
 * @version :1.0
 */

public class RegisterEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long userId;

	private Object user;

	private Long sessionId;

	public RegisterEvent(Object source) {
		super(source);
	}

	public RegisterEvent(Object source, Object user, Long userId) {
		super(source);
		this.userId = userId;
		this.user = user;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

}
