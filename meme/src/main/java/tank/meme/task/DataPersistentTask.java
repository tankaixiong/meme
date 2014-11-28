package tank.meme.task;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author tank
 * @date:28 Nov 2014 10:53:18
 * @description:缓存数据待久化到数据库
 * @version :1.0
 */
public class DataPersistentTask {

	static {
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// TODO:

			}
		}, 0, 60, TimeUnit.SECONDS);

	}
}
