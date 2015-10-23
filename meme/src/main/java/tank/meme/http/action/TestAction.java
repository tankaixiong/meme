package tank.meme.http.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tank.http.annotation.api.ContentType;
import tank.http.annotation.api.HttpController;
import tank.http.annotation.api.HttpMethod;
import tank.http.annotation.api.HttpRequestMapping;
import tank.meme.core.net.http.netty.HttpRequest;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月10日 下午5:17:53
 * @description:
 * @version :0.1
 */
@HttpController
public class TestAction {
	private Logger LOG = LoggerFactory.getLogger(TestAction.class);

	@HttpRequestMapping(value = "/test",method=HttpMethod.GET,contextType=ContentType.JSON)
	public String test(String name, Long id, HttpRequest request) {
		LOG.info("调用 了方法:{}", request);
		LOG.info("参数:{}",request.getParameter("name"));
		//return "success invoke";
		return "{\"name\":\"tom\"}";
	}

}
