package tank.meme.core.net.socket;

import tank.meme.utils.JsonUtils;

/**
 * 客户端提交给服务器的数据实体类
 * 
 * @author tank
 * @date Mar 11, 2013 11:44:28 AM
 */
public class Request extends Protocol {
	public Request() {
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
