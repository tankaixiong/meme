package tank.meme.core.net.socket.mina.policy;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author tank
 * @version :1.0
 * @date:Oct 24, 2012 10:21:07 AM
 * @description:监听 处理响应客户端发来的请求策略文件
 */
@Component
public class PolicyFileHandler extends IoHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(PolicyFileHandler.class);

	//	private String xmlContext;
	//
	//	public PolicyFileHandler() {
	//		// 加载跨域策略文件
	//		try {
	//			xmlContext = new FileHelper().getReadTXT(this.getClass().getResource("").getPath() + "crossdomain.xml");
	//		} catch (FileNotFoundException e) {
	//			e.printStackTrace();
	//			log.error("读取策略文件异常{}", e);
	//		}
	//		if (StringUtils.isEmpty(xmlContext)) {
	//			log.error("策略文件内容为空！");
	//		}
	//	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// log.info("请求策略文件");
		String msg = (String) message;
		//int port = ((InetSocketAddress) session.getRemoteAddress()).getPort();
		log.info("请求端口{},策略文件{}", msg, session.getLocalAddress());
		// if(msg!=null&&msg.indexOf("<policy-file-request/>")!=-1){
		// log.info("来自" + session.getRemoteAddress() + "请求策略文件，返回数据：" +
		// xmlContext);
		//session.write(xmlContext + "\0");
		session.write(PolicyFileUtils.getPolicyFile());
	}
}
