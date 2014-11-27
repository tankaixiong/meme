package tank.meme.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import tank.meme.core.event.RegisterEvent;

/**
 * @author tank
 * @date:27 Nov 2014 11:34:15
 * @description:注册事件监听
 * @version :1.0
 */
@Component
public class RegisterListener implements ApplicationListener<RegisterEvent> {

	private Logger logger = LoggerFactory.getLogger(RegisterListener.class);

	@Override
	public void onApplicationEvent(RegisterEvent event) {
		// 注册后的相应逻辑
	}

}
