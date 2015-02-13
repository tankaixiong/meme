package tank.meme.http.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tank
 * @date:2015年1月28日 下午5:32:01
 * @description:
 * @version :0.1
 */

public class TestServlet extends HttpServlet {
	public TestServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("test");
		//response.sendRedirect("http://192.168.11.103:9090/mermaid/");
		//request.getRequestDispatcher("http://192.168.1.203:8080/").forward(request, response);
		StringBuffer sb=new StringBuffer();
		sb.append("http://192.168.1.203:8080").append(request.getRequestURI()).append("?r=1");
		Enumeration<String> params= request.getParameterNames();
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			sb.append("&").append(name).append("=");
			sb.append(request.getParameter(name));
		}
		response.sendRedirect(sb.toString());
	}

}
