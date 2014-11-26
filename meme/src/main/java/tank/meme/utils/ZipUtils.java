package tank.meme.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.InflaterInputStream;

/**
 * @author tank
 * @date:Nov 8, 2013 2:24:06 PM
 * @description:GZIP,ZIP,jzlib，压缩解压缩方法
 * @version :1.0
 */
public class ZipUtils {
	private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);

	/**
	 * gZip压缩方法
	 */
	public static byte[] gZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(data);
			gzip.finish();
			gzip.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("解压数据异常:{}", e);
		}
		return b;
	}

	/**
	 * gZip解压方法
	 */
	public static byte[] unGZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("解压数据异常:{}", e);
		}
		return b;
	}

	/***************************************************************************
	 * 压缩Zip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] zip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(bos);
			ZipEntry entry = new ZipEntry("zip");
			entry.setSize(data.length);
			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();
			zip.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("解压数据异常:{}", e);
		}
		return b;
	}

	/***************************************************************************
	 * 解压Zip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] unZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ZipInputStream zip = new ZipInputStream(bis);
			while (zip.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int num = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((num = zip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				b = baos.toByteArray();
				baos.flush();
				baos.close();
			}
			zip.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("解压数据异常:{}", e);
		}
		return b;
	}

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
			log.error("解压数据异常:{}", e);
		} finally {

			try {
				zOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("解压数据异常:{}", e);
			}

			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("解压数据异常:{}", e);
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
			log.error("解压数据异常:{}", e);
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("解压数据异常:{}", e);
			}
			try {
				if (zIn != null)
					zIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("解压数据异常:{}", e);
			}
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("解压数据异常:{}", e);
			}
		}
		return data;
	}
}
