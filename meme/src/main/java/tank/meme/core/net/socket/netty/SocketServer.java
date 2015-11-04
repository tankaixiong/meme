package tank.meme.core.net.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import msg.proto.pojo.ReqeustProto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月28日 下午8:09:00
 * @description:
 * @version :0.1
 */

public class SocketServer {
	private static Logger LOG = LoggerFactory.getLogger(SocketServer.class);

	public static void main(String[] args) {
		run(1234);
	}

	public static void run(int port) {
		LOG.info("http 监听端口:{}", port);
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootStrap = new ServerBootstrap();
			serverBootStrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {

					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());// 半包处理

					ch.pipeline().addLast(new ProtobufDecoder(ReqeustProto.Request.getDefaultInstance()));
					
					ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());

					ch.pipeline().addLast(new ProtobufEncoder());

					ch.pipeline().addLast(new DefaultServerSocketHandler());

				}
			});

			try {
				ChannelFuture channelFuture = serverBootStrap.bind(port).sync();
				channelFuture.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			bossGroup.shutdownGracefully();

			workGroup.shutdownGracefully();
		}

	}

}
