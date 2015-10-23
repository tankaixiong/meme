package tank.meme.core.net.http.netty;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月9日 下午11:48:10
 * @description:
 * @version :0.1
 */

public class ErrorResponse {

	public static FullHttpResponse getErrorResponse(HttpResponseStatus errorMsg) {

		String text = HttpResponseStatusToJSON(errorMsg);

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(text.getBytes()));
		response.headers().set("Content-Type", "text/plain;charset=utf-8");
		response.headers().set("Connection", "keep-alive");
		response.headers().set("Content-Length", String.valueOf(response.content().readableBytes()));

		return response;
	}

	private static String HttpResponseStatusToJSON(HttpResponseStatus status) {
		return "{code:" + status.code() + ",msg:\"" + status.reasonPhrase() + "\"}";
	}

}
