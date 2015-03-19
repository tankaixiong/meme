package tank.meme.core.entity;

import java.util.Date;
import java.util.Map;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月19日 上午10:13:40
 * @description:服务器状态
 * @version :0.1
 */

public class ServerState {
	/**
	 * 服务器名
	 */
	private String name;
	/**
	 * 启动时间
	 */
	private Date startTime;
	/**
	 * 运行时长
	 */
	private Long duration;

	/**
	 * 客户端连接数
	 */
	private Integer clientConnect;
	/**
	 * 在线玩家数（登录了）
	 */
	private Integer onlineNum;

	/**
	 * ip 对应连接数
	 */
	private Map<String, Integer> ipConnect;

}
