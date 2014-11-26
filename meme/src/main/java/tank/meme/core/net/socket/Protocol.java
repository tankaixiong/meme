package tank.meme.core.net.socket;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Protocol {
	public static final String PACK = "pack";
	public static final String ACT = "act";
	public static final String DATA = "data";
	public static final String TIME = "time";
	public static final String KEY = "key";
	public static final String SID = "sid";

	public static final String TOKEN = "token";
	public static final String PERSISENT_TOKEN = "taTk93#!8(&)";

	public static final byte[] HEADER = Protocol.PACK.getBytes();
	public static final Object[] emptyData = new Object[] {};

	protected Long time = 0L;
	protected String act;
	protected Object[] data;

	protected Long sid;
	protected Integer key;

	public String getAct() {
		return act;
	}

	@JsonIgnore
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	@JsonIgnore
	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Object[] getData() {
		return data == null ? emptyData : data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}
