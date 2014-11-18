package tank.meme.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tank.meme.domain.User;
import tank.meme.repository.IUserRepository;
import tank.meme.service.IUserService;

/**
 * @author tank
 * @date:18 Nov 2014 16:05:16
 * @description:
 * @version :1.0
 */
@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	IUserRepository userRepository;

	/* (non-Javadoc)
	 * @see tank.meme.service.impl.IUserService#login(java.lang.String, java.lang.String)
	 */
	public User login(String name, String pwd) {
		return userRepository.getByNameAndPwd(name, pwd);
	}

	/* (non-Javadoc)
	 * @see tank.meme.service.impl.IUserService#save(tank.meme.domain.User)
	 */
	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}
}
