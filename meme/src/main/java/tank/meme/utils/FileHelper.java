package tank.meme.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author tank
 * @version :
 * @date:Sep 26, 2011 9:03:29 PM
 * @description: 纯文本文件操作类 .txt 读写
 */
public class FileHelper {
	private static final Logger log = LoggerFactory.getLogger(FileHelper.class);

	public String getReadTXT(String path) throws FileNotFoundException {
		File file = new File(path);
		if (file.exists()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
				String data = null;
				StringBuffer sbf = new StringBuffer();
				while ((data = br.readLine()) != null) {
					sbf.append(data);
				}

				return sbf.toString();
			} catch (FileNotFoundException e) {
				log.error("文件没有找到{}", e);
			} catch (IOException e) {
				log.error("IO异常{}", e);
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						log.error("关闭IO异常{}", e);
					}
				}

			}
		}
		return null;
	}

	public boolean getWriteTXT(String path, String writeContext) {
		OutputStreamWriter fw = null;
		try {

			fw = new OutputStreamWriter(new FileOutputStream(path), "utf-8");

			fw.write(writeContext, 0, writeContext.length());
			fw.flush();

			return true;
		} catch (IOException e) {
			log.error("{}", e);
			return false;
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				log.error("{}", e);
			}
		}
	}
}
