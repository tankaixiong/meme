package tank.meme.core.net.http.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import tank.http.annotation.api.HttpMappingContext;
import tank.http.annotation.api.HttpMethod;
import tank.http.annotation.api.HttpParam;
import tank.http.annotation.api.MappingEntity;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月8日 下午2:33:46
 * @description:
 * @version :0.1
 */

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HttpServerHandler.class);

	 private boolean readingChunks;
	 private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //Disk
	 
	@Override
	protected void messageReceived(ChannelHandlerContext context, FullHttpRequest request) throws Exception {

		FullHttpResponse response = null;

		if (!request.decoderResult().isSuccess()) {
			sendError(context, HttpResponseStatus.BAD_REQUEST);
			return;
		}

		String uri = request.uri();
		LOG.info("请求地址:{}", uri);

		URI uriObj = new URI(uri);
		LOG.info("请求地址:{}", uriObj.getPath());
		MappingEntity mapping = HttpMappingContext.getEntity(uriObj.getPath());
		if (mapping != null) {
			if (mapping.getHttpType() != null && mapping.getHttpType() != HttpMethod.DEFAULT && !request.method().toString().equals(mapping.getHttpType().toString())) {
				LOG.error("请求method方法类型不一致:{}", mapping.getHttpType().toString());
				sendError(context, HttpResponseStatus.METHOD_NOT_ALLOWED);
				return;
			} else {

				if (request.method().GET.equals(request.method())) {

					// get请求
					QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri());
					Map<String, List<String>> uriAttributes = decoderQuery.parameters();
					for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
						for (String attrVal : attr.getValue()) {
							System.out.println("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
						}
					}

				} else if (request.method().POST.equals(request.method())) {

 					HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
//					try {
//						List<InterfaceHttpData> postList = decoder.getBodyHttpDatas();
//						// 读取从客户端传过来的参数
//						for (InterfaceHttpData data : postList) {
//							String name = data.getName();
//
//							String value = null;
//							if (InterfaceHttpData.HttpDataType.Attribute == data.getHttpDataType()) {
//								MemoryAttribute attribute = (MemoryAttribute) data;
//								attribute.setCharset(CharsetUtil.UTF_8);
//								value = attribute.getValue();
//
//							}
//							System.out.println(" name:" + name + ",value=" + value);
//						}
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					
					 //post请求
		            decoder = new HttpPostRequestDecoder(factory, request);
		            readingChunks = HttpHeaderUtil.isTransferEncodingChunked(request);
		            System.out.println("Is Chunked: " + readingChunks + "\r\n");
		            System.out.println("IsMultipart: " + decoder.isMultipart() + "\r\n");
		  
		            try {
		                while (decoder.hasNext()) {
		                    InterfaceHttpData data = decoder.next();
		                    if (data != null) {
		                        try {
		                            writeHttpData(data);
		                        } finally {
		                            data.release();
		                        }
		                    }
		                }
		            } catch (EndOfDataDecoderException e1) {
		            	System.out.println("\r\n\r\nEND OF POST CONTENT\r\n\r\n");
		            }

				}

				// Class[] paramClass = mapping.getMethod().getParameterTypes();
				// for (Class pclazz : paramClass) {
				// // TODO:获取参数名，赋值
				//
				// }
				// Annotation[][] paramAnno = mapping.getMethod().getParameterAnnotations();
				// int paramLength = paramAnno.length;
				// if (paramAnno != null && paramLength > 0) {
				// Object[] paramValueArray = new Object[paramLength];
				//
				// for (int i = 0; i < paramLength; i++) {
				// Annotation[] annotations = paramAnno[i];
				//
				// if (annotations.length > 0) {
				// if (annotations[0] instanceof HttpParam) {
				// String paramName = ((HttpParam) annotations[0]).value();
				//
				// }
				// }
				//
				// }
				// }

				// mapping.getMethod().invoke(mapping.getInstance(), args);

			}
		} else {
			LOG.error("请求地址没有映射相应的处理:{}", uri);
			sendError(context, HttpResponseStatus.NOT_FOUND);
			return;
		}

	}
	private void writeHttpData(InterfaceHttpData data) {
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

				LOG.info("param:name={},value={}",name,value);
				 
			} catch (IOException e1) {

				return;
			}

		}
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelUnregistered");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelRegistered");
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
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("---------------------channelActive");
		super.channelActive(ctx);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		LOG.info("----------------------channelWritabilityChanged");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();

		super.exceptionCaught(ctx, cause);

	}

	private static ByteBuf parseBuf(String title, String context) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html> <html> <head> <meta charset=\"UTF-8\"> <title>").append(title).append("</title> </head> <body>").append(context).append(" </body> </html>");
		return Unpooled.wrappedBuffer(sb.toString().getBytes());
	}

	private static void sendError(ChannelHandlerContext context, HttpResponseStatus status) {

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure:" + status.toString() + "\r\n", CharsetUtil.UTF_8));
		context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static void sendRedirect(String url) {

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		// response.headers().set();

	}
}
