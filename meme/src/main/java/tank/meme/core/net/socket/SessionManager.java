package tank.meme.core.net.socket;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tank.meme.core.constant.SessionConstant;

/**
 * @author tank
 * @date:26 Nov 2014 10:04:58
 * @description: session管理类
 * @version :1.0
 */
public class SessionManager {

	private Logger logger = LoggerFactory.getLogger(SessionManager.class);

	private static SessionManager sessionManager;

	private SessionManager() {

	}

	public static SessionManager getInstance() {
		if (sessionManager == null) {
			synchronized (SessionManager.class) {
				if (sessionManager == null) {
					sessionManager = new SessionManager();
				}
			}
		}
		return sessionManager;
	}

	/**
	 * 所有在线连接 sessionId,session
	 */
	private Map<Long, SocketSession> sessionMap = new ConcurrentHashMap<Long, SocketSession>();
	/**
	 * 用户与session的关联关系 userId,session
	 */
	private Map<Long, SocketSession> userSessionMap = new ConcurrentHashMap<Long, SocketSession>();

	public SocketSession getSession(Long sessionId) {
		return sessionMap.get(sessionId);
	}

	public SocketSession getSessionByUserId(Long userId) {
		return userSessionMap.get(userId);
	}

	public void addSession(SocketSession session) {
		sessionMap.put(session.getId(), session);
	}

	public void addUser(Object user, Long userId, Long sessionId) {
		// 用户对象放入session中
		SocketSession session = sessionMap.get(sessionId);
		session.setAttribute(SessionConstant.USER_ID_Key, userId);
		session.setAttribute(SessionConstant.USER_Key, user);
		// 建立user与session的关联关系
		userSessionMap.put(userId, session);
	}

	public void removeSessionBySessionId(Long sessionId) {
		SocketSession session = sessionMap.get(sessionId);

		Long userId = (Long) session.getAttribute(SessionConstant.USER_ID_Key);
		if (userId != null) {
			userSessionMap.remove(userId);
		}

		session.removeAttribute(SessionConstant.USER_Key);// 移除session中的user信息
		session.removeAttribute(SessionConstant.USER_ID_Key);

		sessionMap.remove(sessionId);
	}

	public void removeSessionByUserId(Long userId) {
		SocketSession session = userSessionMap.get(userId);

		userSessionMap.remove(userId);

		session.removeAttribute(SessionConstant.USER_Key);// 移除session中的user信息
		session.removeAttribute(SessionConstant.USER_ID_Key);

		sessionMap.remove(session.getId());
	}

	/**
	 * 给当前服务器所有在线进行消息推送
	 * 
	 * @param response
	 */
	public void broadcastAll(Response response) {
		Iterator<SocketSession> sessionIt = sessionMap.values().iterator();
		while (sessionIt.hasNext()) {
			SocketSession session = sessionIt.next();
			session.write(response);
		}
	}

	/**
	 * 给指定session ID 推送消息
	 * 
	 * @param response
	 * @param sessionId
	 */
	public void broadcastBySessionId(Response response, Long sessionId) {
		if (sessionId != null) {
			SocketSession session = sessionMap.get(sessionId);
			session.write(response);
		} else {
			logger.error("id 为空，无法找到相应的session");
		}
	}

	/**
	 * 给指定USER 推送消息
	 * 
	 * @param response
	 * @param userId
	 */
	public void broadcastByUserId(Response response, Long userId) {
		if (userId != null) {
			SocketSession session = userSessionMap.get(userId);
			session.write(response);
		} else {
			logger.error("id 为空，无法找到相应的session");
		}

	}
}
