package meme;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tank.meme.domain.User;
import tank.meme.service.IUserService;

/**
 * @author tank
 * @date:18 Nov 2014 16:12:49
 * @description:
 * @version :1.0
 */
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@EnableAutoConfiguration
public class TestUser {

	@Autowired
	private IUserService userServie;

	@Test
	public void testSelect() {
		userServie.login("tank", "tank");

	}

	@Test
	public void testInert() {
		User user = new User();
		user.setCreateTime(new Date());
		user.setName("aaa");
		user.setPwd("123");
		userServie.save(user);

	}
}
