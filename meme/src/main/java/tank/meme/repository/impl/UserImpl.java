package tank.meme.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import tank.meme.domain.User;
import tank.meme.repository.IUserRepository;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月22日 上午11:50:20
 * @description:
 * @version :0.1
 */
//@Repository
public class UserImpl {/**implements IUserRepository {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<User> findById(String id) {
		return null;
	}

	@Override
	public List<User> findByNameAndPwd(String name, String pwd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findByCreateTime(String createTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getByNameAndPwd(String name, String pwd) {
		Query query = em.createQuery("select u from User u where u.name=:name and u.pwd=:pwd ");
		query.setParameter("name", name);
		query.setParameter("pwd", pwd);

		Object obj = query.getSingleResult();
		return (User) obj;
	}

	@Override
	public User save(User user) {
		em.persist(user);
		return user;
	}**/

}
