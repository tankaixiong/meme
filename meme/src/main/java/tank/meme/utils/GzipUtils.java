package tank.meme.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.InflaterInputStream;

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
	 * 压缩数据
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] jzlib(byte[] object) {

		byte[] data = null;
		ByteArrayOutputStream out = null;
		DeflaterOutputStream zOut = null;
		DataOutputStream objOut = null;
		try {
			out = new ByteArrayOutputStream();
			zOut = new DeflaterOutputStream(out);
			objOut = new DataOutputStream(zOut);
			objOut.write(object);
			objOut.flush();
			objOut.close();
			data = out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("压缩数据异常:{}", e);
		} finally {

			try {
				zOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("压缩数据异常:{}", e);
			}

			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("压缩数据异常:{}", e);
			}
		}
		return data;
	}

	/**
	 * 解压被压缩的数据
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] unjzlib(byte[] object) {

		byte[] data = null;
		ByteArrayInputStream in = null;
		InflaterInputStream zIn = null;
		ByteArrayOutputStream baos = null;
		try {
			in = new ByteArrayInputStream(object);
			zIn = new InflaterInputStream(in, false);

			byte[] buf = new byte[1024];
			int num = -1;
			baos = new ByteArrayOutputStream();
			while ((num = zIn.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			data = baos.toByteArray();
			baos.flush();

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("解压数据异常:{}", e);
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("解压数据异常:{}", e);
			}
			try {
				if (zIn != null)
					zIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("解压数据异常:{}", e);
			}
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("解压数据异常:{}", e);
			}
		}
		return data;
	}

	/**
	 * 压缩
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] gzip(byte[] data) {

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
				if (gzipOut != null) {
					gzipOut.close();
				}
			} catch (IOException e1) {
				logger.error("{}", e1);
			}

			try {
				if (byteOut != null) {
					byteOut.flush();
				}
			} catch (IOException e) {
				logger.error("{}", e);
			}
			try {
				if (byteOut != null) {
					byteOut.close();
				}
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
	public static byte[] ungzip(byte[] source) {

		byte[] output = null;
		GZIPInputStream gzipIn = null;
		ByteArrayOutputStream byteOut = null;
		ByteArrayInputStream byteIn = null;
		try {
			byteOut = new ByteArrayOutputStream();
			byteIn = new ByteArrayInputStream(source);
			// 压缩
			gzipIn = new GZIPInputStream(byteIn);

			int buffer = 1024;
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
				if (gzipIn != null) {
					gzipIn.close();
				}
			} catch (IOException e) {
				logger.error("{}", e);
			}
			try {
				if (byteIn != null) {
					byteIn.close();
				}
			} catch (IOException e) {
				logger.error("{}", e);
			}
			try {
				if (byteOut != null) {
					byteOut.close();
				}
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}

		return output;
	}

}
