package meme.netty;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import msg.proto.pojo.ReqeustProto;
import msg.proto.pojo.ResponseProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年11月4日 下午4:08:36
 * @description:
 * @version :0.1
 */

public class NettyClientHanlder extends ChannelHandlerAdapter {

	private Logger LOG = LoggerFactory.getLogger(NettyClientHanlder.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);

		ReqeustProto.Request.Builder request = ReqeustProto.Request.newBuilder();
		request.setAct("test");
		request.setId(1);
		request.setData("请求数据");

		//request.build();

		ctx.writeAndFlush(request.build());

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		super.channelRead(ctx, msg);

		ResponseProto.Response response = (ResponseProto.Response) msg;

		LOG.info("{}", response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);

		ctx.close();
	}

}
