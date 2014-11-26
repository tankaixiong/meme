package tank.meme.core.net.socket;

import tank.meme.utils.JsonUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 响应给客户端的数据实体类，可继承它来扩展它的属性
 * 
 * @author tank
 * @date Apr 11, 2013 5:18:22 PM
 */
public class Response extends Protocol {
	public Response() {
		time = System.currentTimeMillis();
	}

	public Response(String act) {
		this();
		this.act = act;
	}

	public Response(String act, Object[] data) {
		this();
		this.act = act;
		this.data = data;
	}

	public static Response valueOf(Object response) {
		if (response == null)
			return null;

		return JsonUtils.toBean(response.toString(), Response.class);

	}

	@JsonIgnore
	public boolean hasError() {
		return this.getAct().contains("alert");
	}

	// 为了在控制台查看Response对象更直观
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
