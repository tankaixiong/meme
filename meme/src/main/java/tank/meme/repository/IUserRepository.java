package tank.meme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tank.meme.domain.User;

/**
 * @author tank
 * @date:18 Nov 2014 15:34:13
 * @description:
 * @version :1.0
 */
public interface IUserRepository{
//extends CrudRepository<User, Long> {

	List<User> findById(String id);

	List<User> findByNameAndPwd(String name, String pwd);

	@Query("select u from User u where u.createTime >= ?1")
	List<User> findByCreateTime(String createTime);

	@Query("select u from User u where u.name=:name and u.pwd=:pwd ")
	User getByNameAndPwd(@Param("name") String name, @Param("pwd") String pwd);

	User save(User user);
}
