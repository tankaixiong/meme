package tank.meme.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月24日 下午11:43:33
 * @description:MD5校验文件
 * @version :0.1
 */

public class MD5Utils {
	private static Logger LOG = LoggerFactory.getLogger(MD5Utils.class);
	/**
	 * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	protected static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messagedigest = null;

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String getFileMD5String(File file) {
		InputStream fis = null;
		try {

			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				messagedigest.update(buffer, 0, numRead);
			}

			return bufferToHex(messagedigest.digest());
		} catch (FileNotFoundException e) {
			LOG.error("{}", e);
		} catch (IOException e) {
			LOG.error("{}", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOG.error("{}", e);
				}
			}
		}
		return null;
	}

	public static String getFileMD5String(InputStream in) {
		try {
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = in.read(buffer)) > 0) {
				messagedigest.update(buffer, 0, numRead);
			}

			return bufferToHex(messagedigest.digest());
		} catch (IOException e) {
			LOG.error("{}", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				LOG.error("{}", e);
			}
		}
		return null;
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换
		// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getFileMD5String(new File("c:\\sdk\\setting-service-0.0.1.jar")));
		// e5d2fc53a77cbea2d2053aa2f85520ff
		// e5d2fc53a77cbea2d2053aa2f85520ff
	}

}
