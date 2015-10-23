package tank.meme.core.net.http.netty;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月9日 下午11:36:27
 * @description:
 * @version :0.1
 */

public enum ErrorMsg {
	METHOD_NOT_ALLOWED(405, "请求方式异常");

	private String msg;
	private int code;

	private ErrorMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "{code:" + this.code + ",msg:\"" + this.msg + "\"}";
	}

}
