package tank.meme.core.net.socket;

import org.apache.mina.core.session.IoSession;

import tank.meme.core.constant.SessionConstant;

/**
 * @author tank
 * @date:26 Nov 2014 10:06:12
 * @description:session包装类(统一多个通信框架的接口)
 * @version :1.0
 */
public class SocketSession {

	private IoSession minaIoSession;

	public SocketSession(IoSession session) {
		minaIoSession = session;
	}

	/**
	 * session Id
	 * 
	 * @return
	 */
	public long getId() {
		if (minaIoSession != null) {
			return minaIoSession.getId();
		}
		return -1;
	}

	public void write(Object obj) {
		if (minaIoSession != null) {
			minaIoSession.write(obj);
		}
	}

	public Object getAttribute(String key) {
		if (minaIoSession != null) {
			return minaIoSession.getAttribute(key);
		}
		return null;
	}

	public void setAttribute(String key, Object value) {
		if (minaIoSession != null) {
			minaIoSession.setAttribute(key, value);
		}
	}

	public void removeAttribute(String key) {
		if (minaIoSession != null) {
			minaIoSession.removeAttribute(key);
		}
	}
	/**
	 * 得到session中用户对象
	 * @param userId
	 * @param cs
	 * @return
	 */
	public <T> T getUser(Class<T> cs) {
		Object obj = getAttribute(SessionConstant.USER_Key);
		return (T) obj;
	}

	public Object getUser() {
		Object obj = getAttribute(SessionConstant.USER_Key);
		return obj;
	}

}
