package tank.meme.service;

import org.springframework.transaction.annotation.Transactional;

import tank.meme.domain.User;

/**
 * @author tank
 * @date:18 Nov 2014 16:12:16
 * @description:
 * @version :1.0
 */
public interface IUserService {

	public abstract User login(String name, String pwd);

	public abstract User save(User user);

}