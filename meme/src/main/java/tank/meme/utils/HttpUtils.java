package tank.meme.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
		* @author tank
		* @date:2014-5-5 上午11:21:16
		* @description:
		* @version :
		*/
public class HttpUtils {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * http请求
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getHttpRequest(String url) throws IOException {
		log.info("转发请求地址:{}",url);
		HttpClient client = new HttpClient();
		StringBuilder sb = new StringBuilder();
		InputStream ins = null;
		// Create a method instance.  
		GetMethod method = new GetMethod(url);//这个不能变,以免sign不通过
		//PostMethod method = new PostMethod(url);
		// Provide custom retry handler is necessary  
		//method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		try {
			// Execute the method.  
			int statusCode = client.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK) {
				ins = method.getResponseBodyAsStream();
				byte[] b = new byte[1024];
				int r_len = 0;
				while ((r_len = ins.read(b)) > 0) {
					sb.append(new String(b, 0, r_len, method.getResponseCharSet()));
				}
			} else {
				log.error("请求:{}返回状态码:{}", url, statusCode);
			}
		} catch (HttpException e) {
			log.error("请求:{},异常{}", url, e);
		} catch (IOException e) {
			log.error("请求:{},异常{}", url, e);
		} finally {
			method.releaseConnection();
			if (ins != null) {
				ins.close();
			}
		}
		return sb.toString();
	}

	public static String postRequest(String url, NameValuePair... p) throws IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		InputStream ins = null;
		StringBuilder sb = new StringBuilder();

		if (p != null && p.length > 0) {
			method.setRequestBody(p);
		}

		try {
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				ins = method.getResponseBodyAsStream();
				byte[] b = new byte[1024];
				int r_len = 0;
				while ((r_len = ins.read(b)) > 0) {
					sb.append(new String(b, 0, r_len, method.getResponseCharSet()));
				}
			} else {
				log.error("请求:{}返回状态码:{}", url, statusCode);
			}
		} catch (HttpException e) {
			log.error("请求:{},异常{}", url, e);
		} catch (UnsupportedEncodingException e) {
			log.error("请求:{},异常{}", url, e);
		} catch (IOException e) {
			log.error("请求:{},异常{}", url, e);
		} finally {
			method.releaseConnection();
			if (ins != null) {
				ins.close();
			}
		}

		return sb.toString();

	}

}
