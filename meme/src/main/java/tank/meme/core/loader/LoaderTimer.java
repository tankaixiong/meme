package tank.meme.core.loader;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tank.meme.core.Application;
import tank.meme.core.constant.ApplicationProperties;
import tank.meme.utils.MD5Utils;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月24日 下午11:55:25
 * @description:
 * @version :0.1
 */

public class LoaderTimer {
	private static final Logger LOG = LoggerFactory.getLogger(LoaderTimer.class);
	protected Timer timer = new Timer();
	// 记录jar包的md5校验信息
	private HashMap<String, String> jarMd5Map = new HashMap<String, String>();

	public void start() {
		final String sdk = Application.getInstance().getProperties().getString(ApplicationProperties.SDK_PATH);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				String validateCode = null;
				File file = new File(sdk);

				if (file.isDirectory()) {
					File[] files = file.listFiles();
					for (File f : files) {
						String jarName = f.getName();
						String code = jarMd5Map.get(jarName);
						if (code == null) {
							LOG.info("新加载jar:{}", jarName);
							LoaderManger.reLoaderJar(f);

							validateCode = MD5Utils.getFileMD5String(f);
							jarMd5Map.put(jarName, validateCode);
						} else {
							validateCode = MD5Utils.getFileMD5String(f);
							if (!code.equals(validateCode)) {
								LOG.info("jar包:{}有变化，重新装载", jarName);
								LoaderManger.reLoaderJar(f);
								jarMd5Map.put(jarName, validateCode);
							} else {
								LOG.info("检测jar包:{}是否有更新...", jarName);
							}
						}
					}
				}

			}
		}, 90000, 90000);
	}
}
