package tank.meme.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import tank.meme.core.event.LoginEvent;
import tank.meme.core.event.SessionCloseEvent;
import tank.meme.core.event.SessionOpenedEvent;
import tank.meme.core.net.socket.SocketSession;

/**
 * @author tank
 * @date:27 Nov 2014 11:34:15
 * @description:
 * @version :1.0
 */
@Component
public class SessionOpenedListener implements ApplicationListener<SessionOpenedEvent> {

	private Logger logger = LoggerFactory.getLogger(SessionOpenedListener.class);

	@Override
	public void onApplicationEvent(SessionOpenedEvent event) {
		// 加入session管理
		tank.meme.core.net.socket.SessionManager.getInstance().addSession(event.getSession());

	}

}
