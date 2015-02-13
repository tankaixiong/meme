package tank.meme.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @date:2015年2月13日 上午9:49:11
 * @description:gzip压缩解压缩
 * @version :0.1
 */

public class GzipUtils {

	private static final Logger logger = LoggerFactory.getLogger(GzipUtils.class);

	/**
	 * 压缩
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] data) {

		byte[] output = null;
		ByteArrayOutputStream byteOut = null;
		GZIPOutputStream gzipOut = null;
		try {
			byteOut = new ByteArrayOutputStream();
			// 压缩
			gzipOut = new GZIPOutputStream(byteOut);

			gzipOut.write(data, 0, data.length);
			gzipOut.finish();

			output = byteOut.toByteArray();

		} catch (IOException e) {
			logger.error("gizp 压缩 数据异常:{}", e);
		} finally {
			try {
				gzipOut.close();
			} catch (IOException e1) {
				logger.error("{}", e1);
			}

			try {
				byteOut.flush();
			} catch (IOException e) {
				logger.error("{}", e);
			}
			try {
				byteOut.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}

		return output;
	}

	/**
	 * 解压
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] uncompress(byte[] source) {

		byte[] output = null;
		GZIPInputStream gzipIn = null;
		ByteArrayOutputStream byteOut = null;
		ByteArrayInputStream byteIn = null;
		try {
			byteOut = new ByteArrayOutputStream();
			byteIn = new ByteArrayInputStream(source);
			// 压缩
			gzipIn = new GZIPInputStream(byteIn);

			int buffer = 10240;
			int count;
			byte data[] = new byte[buffer];
			while ((count = gzipIn.read(data, 0, buffer)) != -1) {
				byteOut.write(data, 0, count);
			}

			output = byteOut.toByteArray();

		} catch (IOException e) {
			logger.error("gizp 解压 数据异常:{}", e);
		} finally {
			try {
				gzipIn.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
			try {
				byteIn.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
			try {
				byteOut.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}

		return output;
	}

}
