package tank.meme.core.net.socket.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

import msg.proto.pojo.ReqeustProto;
import msg.proto.pojo.ReqeustProto.Request;
import msg.proto.pojo.ResponseProto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月29日 下午2:13:59
 * @description:
 * @version :0.1
 */

public class DefaultServerSocketHandler extends ChannelHandlerAdapter {

	private Logger LOG = LoggerFactory.getLogger(DefaultServerSocketHandler.class);
	
	 
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		 
		super.channelActive(ctx);
		
		ctx.channel().parent().localAddress();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// super.channelRead(ctx, msg);
		
		SocketAddress address=ctx.channel().remoteAddress();
		LOG.info("{}",address);

		ReqeustProto.Request request = (Request) msg;

		LOG.info("请求数据:{}", request);
		LOG.info("测试中文",request.getData());

		ResponseProto.Response.Builder builder = ResponseProto.Response.newBuilder();
		builder.setId(1);
		builder.setAct("test");
		builder.setData("这是个测试响应!");

		builder.build();

		ctx.writeAndFlush(builder);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		
		LOG.error("{}",cause);

		ctx.close();
	}
}
