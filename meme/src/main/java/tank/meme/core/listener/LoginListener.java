package tank.meme.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import tank.meme.core.event.LoginEvent;
import tank.meme.core.net.socket.SessionManager;

/**
 * @author tank
 * @date:27 Nov 2014 11:34:15
 * @description:登录后的监听事件
 * @version :1.0
 */
@Component
public class LoginListener implements ApplicationListener<LoginEvent> {

	private Logger logger = LoggerFactory.getLogger(LoginListener.class);

	@Override
	public void onApplicationEvent(LoginEvent event) {
		// 纳入session管理,并与session绑定关系 
		SessionManager.getInstance().addUser(event.getUser(), event.getUserId(), event.getSessionId());

	}

}
