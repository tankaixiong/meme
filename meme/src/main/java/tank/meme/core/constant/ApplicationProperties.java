package tank.meme.core.constant;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月18日 下午2:18:34
 * @description:
 * @version :0.1
 */

public class ApplicationProperties {

	// #处理消息分发的线程数
	public static String MSG_THREAD_NUM = "msg.thread.num";
	// #消息列队模式(default,redis,mem)默认为mem内存模式
	public static String MSG_QUEUE_TYPE = "msg.queue.type";
	// #最大客户端口连接数
	public static String SOCKET_MAXCONNECTIONS = "socket.maxConnections";
	// #服务器ID,集群中唯一
	public static String SERVER_ID = "server.id";

}
