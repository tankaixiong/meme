package tank.meme.core.net.http.netty;

import io.netty.handler.codec.http.HttpMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月22日 下午7:50:25
 * @description:http 请求对象，包含请求的相关数据
 * @version :0.1
 */

public class HttpRequest {

	private Map<String, List<String>> parameterMap = new HashMap<String, List<String>>();

	private Map<CharSequence, CharSequence> headers = new HashMap<CharSequence, CharSequence>();

	private HttpMethod method;

	private String uri;

	// private String queryString;

	private String path;

	/**
	 * 非键值对方式的参数
	 */
	private String parameterString;

	public String getParameter(String name) {
		List<String> values = parameterMap.get(name);
		if (values != null && !values.isEmpty()) {
			return values.get(0);
		}
		return null;
	}

	public List<String> getParameters(String name) {
		List<String> values = parameterMap.get(name);
		return values;
	}

	// public String getQueryString() {
	// return queryString;
	// }
	//
	// public void setQueryString(String queryString) {
	// this.queryString = queryString;
	// }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParameterString() {
		return parameterString;
	}

	public void setParameterString(String parameterString) {
		this.parameterString = parameterString;
	}

	public Map<String, List<String>> getParameterMap() {
		return parameterMap;
	}

	public void putParameter(String key, String value) {
		List<String> list = null;
		if (this.parameterMap.containsKey(key)) {
			list = this.parameterMap.get(key);
		} else {
			list = new ArrayList<String>();
		}
		list.add(value);
		this.parameterMap.put(key, list);
	}

	public void putParameter(String key, List<String> value) {
		this.parameterMap.put(key, value);
	}

	public Map<CharSequence, CharSequence> getHeaders() {
		return headers;
	}

	public void putHeaders(CharSequence key, CharSequence value) {
		this.headers.put(key, value);
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
