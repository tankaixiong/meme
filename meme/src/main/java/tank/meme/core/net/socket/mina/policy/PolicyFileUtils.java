package tank.meme.core.net.socket.mina.policy;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tank.meme.utils.FileHelper;

/**
 * @author tank
 * @date:Jan 23, 2014 4:58:42 PM
 * @description:
 * @version :1.0
 */
@Component
public class PolicyFileUtils {
	private static final Logger log = LoggerFactory.getLogger(PolicyFileUtils.class);
	private static String xmlContext;
	static {
		// 加载跨域策略文件
		try {
			if (xmlContext == null) {
				xmlContext = new FileHelper().getReadTXT(PolicyFileUtils.class.getResource("").getPath() + "crossdomain.xml") + "\0";
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("读取策略文件异常{}", e);
		}
	}

	public static String getPolicyFile() {

		return xmlContext;
	}
}
