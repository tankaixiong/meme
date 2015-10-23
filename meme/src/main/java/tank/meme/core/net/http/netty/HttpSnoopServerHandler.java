package tank.meme.core.net.http.netty;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.COOKIE;
import static io.netty.handler.codec.http.HttpHeaderNames.SET_COOKIE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.ServerCookieDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.LoggerFactory;

import tank.http.annotation.api.HttpMappingContext;
import tank.http.annotation.api.MappingEntity;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月12日 上午11:32:42
 * @description:
 * @version :0.1
 */

public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<Object> {
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HttpSnoopServerHandler.class);

	/** Buffer that stores the response content */
	// private final StringBuilder buf = new StringBuilder();

	private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk

	private HttpPostRequestDecoder decoder;

	private boolean readingChunks;

	private HttpRequest request;

	private static final AttributeKey<tank.meme.core.net.http.netty.HttpRequest> requestAttributeKey = AttributeKey.newInstance("request");

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) {
		LOG.info("messageReceived,对象:{}", msg);
		LOG.info("messageReceived:channel.id:{}", ctx.channel().id());

		if (msg instanceof HttpRequest) {
			HttpRequest request = this.request = (HttpRequest) msg;

			tank.meme.core.net.http.netty.HttpRequest httpRequet = new tank.meme.core.net.http.netty.HttpRequest();

			httpRequet.setMethod(request.method());

			if (HttpHeaderUtil.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}

			String uri = request.uri();
			httpRequet.setUri(uri);
			LOG.info("uri:{}", uri);
			if (uri.equals("/favicon.ico")) {
				return;
			}

			HttpHeaders headers = request.headers();
			if (!headers.isEmpty()) {

				for (Map.Entry<CharSequence, CharSequence> h : headers) {
					CharSequence key = h.getKey();
					CharSequence value = h.getValue();

					httpRequet.putHeaders(key, value);

				}

			}

			QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
			
			httpRequet.setPath(queryStringDecoder.path());
			
			Map<String, List<String>> params = queryStringDecoder.parameters();
			if (!params.isEmpty()) {
				for (Entry<String, List<String>> p : params.entrySet()) {
					String key = p.getKey();
					List<String> vals = p.getValue();

					httpRequet.putParameter(key, vals);
					for (String val : vals) {
						LOG.info("param:name={},value={}", key, val);
					}
				}
			}
			// if GET Method: should not try to create a HttpPostRequestDecoder
			if (request.method().GET.equals(request.method())) {
				// GET Method: should not try to create a HttpPostRequestDecoder
				// So stop here
				// writeResponse(request, ctx, "success-get");
				// ctx.flush();
				// ctx.close();

			}

			if (request.method().POST.equals(request.method())) {

				try {
					/**
					 * 通过HttpDataFactory和request构造解码器
					 */
					decoder = new HttpPostRequestDecoder(factory, request);
				} catch (ErrorDataDecoderException e1) {
					e1.printStackTrace();

					ctx.channel().close();
					return;
				}

			}
			if (request.decoderResult().isFailure()) {
				LOG.error("{}", request.decoderResult().cause());
			}

			ctx.channel().attr(requestAttributeKey).set(httpRequet);
		}
		if (decoder != null) {
			if (msg instanceof HttpContent) {

				HttpContent httpContent = (HttpContent) msg;

				ByteBuf content = httpContent.content();

				if (content.isReadable()) {
					String paramStr = content.toString(CharsetUtil.UTF_8);// 非键值对的
					LOG.info("参数:{}", paramStr);
					ctx.attr(requestAttributeKey).get().setParameterString(paramStr);
				}

				try {
					decoder.offer(httpContent);
				} catch (ErrorDataDecoderException e1) {
					LOG.error("{}", e1);
					ctx.channel().close();
					return;
				}

				try {
					while (decoder.hasNext()) {
						InterfaceHttpData data = decoder.next();
						if (data != null) {
							try {
								writeHttpData(data, ctx);
							} finally {
								data.release();
							}
						}
					}
				} catch (EndOfDataDecoderException e1) {
					// LOG.error("{}", e1);
					LOG.info("end");
				}

				if (msg instanceof LastHttpContent) {

					LastHttpContent trailer = (LastHttpContent) msg;
					if (!trailer.trailingHeaders().isEmpty()) {
						for (CharSequence name : trailer.trailingHeaders().names()) {
							for (CharSequence value : trailer.trailingHeaders().getAll(name)) {
								LOG.info("TRAILING HEADER:");
								LOG.info("name:{},valule:{}", name, value);
							}
						}
					}

					// if (!writeResponse(trailer, ctx, "success")) {
					// // If keep-alive is off, close the connection once the content is fully written.
					// ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
					// }

					// writeResponse(trailer, ctx, "success");
					// ctx.flush();
					// ctx.close();

				}

			}
		}
	}

	private void writeHttpData(InterfaceHttpData data, ChannelHandlerContext ctx) {
		/**
		 * HttpDataType有三种类型 Attribute, FileUpload, InternalAttribute
		 */
		if (data.getHttpDataType() == HttpDataType.Attribute) {
			Attribute attribute = (Attribute) data;
			String value;
			String name;
			try {
				value = attribute.getValue();
				name = attribute.getName();

				LOG.info("param:name={},value={}", name, value);

				ctx.attr(requestAttributeKey).get().putParameter(name, value);

			} catch (IOException e1) {

				return;
			}

		}
	}

	private static void appendDecoderResult(StringBuilder buf, HttpObject o) {
		DecoderResult result = o.decoderResult();
		if (result.isSuccess()) {
			return;
		}

		buf.append(".. WITH DECODER FAILURE: ");
		buf.append(result.cause());
		buf.append("\r\n");
	}

	private void writeResponse(HttpResponseStatus status, String contentType, ChannelHandlerContext ctx, String responseStr) {
		// Decide whether to close the connection or not.
		boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
		// Build the response object.
		FullHttpResponse response = null;
		if (responseStr != null) {
			response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer(responseStr, CharsetUtil.UTF_8));
		} else {
			response = new DefaultFullHttpResponse(HTTP_1_1, status);
		}

		response.headers().set(CONTENT_TYPE, contentType);

		if (keepAlive) {
			// Add 'Content-Length' header only for a keep-alive connection.
			response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
			// Add keep alive header as per:
			// - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
			response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);

			// response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");//可允许跨域
		}

		// Encode the cookie.
		String cookieString = request.headers().getAndConvert(COOKIE);
		if (cookieString != null) {
			Set<Cookie> cookies = ServerCookieDecoder.decode(cookieString);
			if (!cookies.isEmpty()) {
				// Reset the cookies if necessary.
				for (Cookie cookie : cookies) {
					response.headers().add(SET_COOKIE, ServerCookieEncoder.encode(cookie));
				}
			}
		} else {
			// Browser sent no cookie. Add some.
			response.headers().add(SET_COOKIE, ServerCookieEncoder.encode("key1", "value1"));
			response.headers().add(SET_COOKIE, ServerCookieEncoder.encode("key2", "value2"));
		}

		// Write the response.
		// ctx.write(response);

		ctx.writeAndFlush(response);

		// return keepAlive;
	}

	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
		ctx.write(response);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelUnregistered");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelRegistered:{}", ctx.channel().id());
		super.channelRegistered(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		LOG.info("---------------------channelRead");
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelReadComplete");
		super.channelReadComplete(ctx);

		LOG.info("{}", ctx.attr(requestAttributeKey).get());

		tank.meme.core.net.http.netty.HttpRequest httpRequest = ctx.attr(requestAttributeKey).get();
		if (httpRequest != null) {
			MappingEntity entity = HttpMappingContext.getEntity(httpRequest.getPath());
			if (entity != null) {
				 
				String mappingMethodType=entity.getHttpType().toString();
				if (mappingMethodType.equals("DEFAULT") || mappingMethodType.equals(httpRequest.getMethod().toString())) {

					Class<?>[] parameterTypes = entity.getMethod().getParameterTypes();

					Object[] paramObj = new Object[parameterTypes.length];
					int i = 0;
					Object paramItem = null;
					for (Class<?> clas : parameterTypes) {
						String parameterName = clas.getName();
						if (tank.meme.core.net.http.netty.HttpRequest.class.getName().equals(parameterName)) {
							paramItem = httpRequest;
						}
						paramObj[i] = paramItem;
						// Class.forName(parameterName).newInstance();
						i++;
						System.out.println("参数名称:" + parameterName);
					}
					Object result = entity.getMethod().invoke(entity.getInstance(), paramObj);

					writeResponse(OK, entity.getContentType(), ctx, String.valueOf(result));

					ctx.close();
				} else {
					writeResponse(METHOD_NOT_ALLOWED, entity.getContentType(), ctx, null);

					ctx.close();
				}
			} else {
				writeResponse(NOT_FOUND, entity.getContentType(), ctx, null);

				ctx.close();
			}
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelActive:{}", ctx.channel().id());

		super.channelActive(ctx);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		LOG.info("----------------------channelWritabilityChanged");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		LOG.info("----------------------connect:{}", ctx.channel().id());
		super.connect(ctx, remoteAddress, localAddress, promise);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		LOG.info("----------------------write:{}", ctx.channel().id());
		LOG.info("----------------------write:{}", msg);
		super.write(ctx, msg, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		LOG.info("----------------------disconnect:{}", ctx.channel().id());
		super.disconnect(ctx, promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		LOG.info("----------------------close:{}", ctx.channel().id());
		super.close(ctx, promise);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();

		super.exceptionCaught(ctx, cause);

	}
}
