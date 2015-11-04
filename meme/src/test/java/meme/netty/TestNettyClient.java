package meme.netty;

import msg.proto.pojo.ResponseProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ipfilter.IpFilterRule;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年11月4日 下午3:33:59
 * @description:
 * @version :0.1
 */

public class TestNettyClient {

	public static void main(String[] args) {

		// EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workeGroup = new NioEventLoopGroup();
		 
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workeGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());

					ch.pipeline().addLast(new ProtobufDecoder(ResponseProto.Response.getDefaultInstance()));

					ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());

					ch.pipeline().addLast(new ProtobufEncoder());

					ch.pipeline().addLast(new NettyClientHanlder());

				}

			});

			ChannelFuture future = bootstrap.connect("127.0.0.1", 1234).sync();

			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			workeGroup.shutdownGracefully();
		}
	}
}
