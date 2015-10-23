package tank.meme.core.net.http.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tank.http.annotation.api.AnnotationParser;
import tank.http.annotation.api.HttpMappingContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月8日 上午10:47:14
 * @description:
 * @version :0.1
 */

public class HttpServerManager {
	
	private static Logger LOG=LoggerFactory.getLogger(HttpServerManager.class);

	public static void main(String[] args) {
		setScanPackage(new String[] { "tank.meme.http.action" });
		run(8888);
	}

	public static void setScanPackage(String[] scanPackage) {
		HttpMappingContext.init(scanPackage);
	}

	public static void run(int port) {
		LOG.info("http 监听端口:{}",port);
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootStrap = new ServerBootstrap();
			serverBootStrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
//
					ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));//request 请求的多对象组装成一个fullrequest对象 

					ch.pipeline().addLast("http-endoder", new HttpResponseEncoder());

					ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());

					ch.pipeline().addLast("deflater", new HttpContentCompressor());

					//ch.pipeline().addLast("http-server", new HttpServerHandler());
					
					ch.pipeline().addLast("http-server", new HttpSnoopServerHandler());

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
