package tank.meme.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author tank
 * @date:18 Nov 2014 14:18:34
 * @description:
 * @version :1.0
 */
@Entity
@Table(name = "meme_user")
@GenericGenerator(name = "uuid_u", strategy = "uuid")
public class User implements Serializable {
	@Id
	@GeneratedValue(generator = "uuid_u")
	@Column(length = 32)
	private String id;

	private String name;
	private String pwd;

	private Date createTime;

	public User() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
