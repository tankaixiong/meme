package meme;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tank.meme.core.loader.LoaderManger;
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
// @Configuration
// @EnableAutoConfiguration
public class TestUser {
	
//	@Autowired
//	protected ApplicationContext ctx;//注入方式

	@Autowired
	private IUserService userServie;

	@Test
	public void testSelect() {
		User user = userServie.login("aaa", "123");
		Assert.assertNotNull(user);
		System.out.println(user.getId());
	}

	@Test
	public void testInert() {
		User user = new User();
		user.setCreateTime(new Date());
		user.setName("aaa");
		user.setPwd("123");
		userServie.save(user);

	}

	@Test
	public void loadJar() {
		LoaderManger.loadJar();
	}
}
