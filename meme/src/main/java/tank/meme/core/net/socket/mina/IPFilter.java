package tank.meme.core.net.socket.mina;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author tank
 * @date:Dec 12, 2013 6:02:43 PM
 * @description:IP过滤,限制一个IP过多的连接(防止恶意攻击超成服务器过多的负载)
 * @version :1.0
 */
@Component
public class IPFilter extends IoFilterAdapter {
	private Logger log = LoggerFactory.getLogger(IPFilter.class);
	private static ConcurrentHashMap<String, Integer> ipList = new ConcurrentHashMap<String, Integer>();
	private static final String SESSION_IP = "ip";
	/**
	 * 一个IP最大可创建的连接数
	 */
	private int maxConnection;

	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		if (isBlock(session)) {
			log.error("超过一个IP允许的最大连接数:{},服务器主动断开!", this.getMaxConnection());
			session.close(true);
		} else {
			super.sessionOpened(nextFilter, session);
		}
	}

	private boolean isBlock(IoSession session) throws Exception {
		SocketAddress remoteAddress = session.getRemoteAddress();
		if (remoteAddress instanceof InetSocketAddress) {
			InetAddress address = ((InetSocketAddress) remoteAddress).getAddress();
			String ip = address.getHostAddress();
			session.setAttribute(SESSION_IP, ip);
			log.info("当前连接客户端IP:{}",ip);
			if (!ipList.containsKey(ip)) {
				ipList.put(ip, 1);
			} else {
				Integer num = ipList.get(ip);

				ipList.put(ip, ++num);
				if (num > this.getMaxConnection()) {
					log.info("当前ip:{},socket数量:{}",ip,num);
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		String key = (String) session.getAttribute(SESSION_IP);
		if (key != null) {
			Integer num = ipList.get(key);
			ipList.put(key, --num);
		}

		super.sessionClosed(nextFilter, session);
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}
}
