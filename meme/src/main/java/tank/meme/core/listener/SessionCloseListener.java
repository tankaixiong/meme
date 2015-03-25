package tank.meme.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import tank.meme.core.event.SessionCloseEvent;

/**
 * @author tank
 * @date:27 Nov 2014 11:34:15
 * @description:
 * @version :1.0
 */
@Component
public class SessionCloseListener implements ApplicationListener<SessionCloseEvent> {

	private Logger logger = LoggerFactory.getLogger(SessionCloseListener.class);

	@Override
	public void onApplicationEvent(SessionCloseEvent event) {
		event.getSession().getUser();//TODO:持久化用户数据到数据库中

	}

}
