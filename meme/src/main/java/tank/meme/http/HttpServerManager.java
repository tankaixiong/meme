package tank.meme.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import tank.meme.http.servlet.TestServlet;

/**
 * @author tank
 * @date:2015年1月28日 下午4:55:10
 * @description:http启动类
 * @version :0.1
 */

public class HttpServerManager {
	public static void main(String[] args) throws Exception {
		Server server = new Server(9999);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		// http://localhost:8080/hello
		context.addServlet(new ServletHolder(new TestServlet()), "/test");

		server.start();
		server.join();
	}
}
