package meme;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import tank.meme.core.loader.LoaderManger;

/**
 * @author tank
 * @date:18 Nov 2014 16:12:49
 * @description:
 * @version :1.0
 */
public class TestSetting extends BaseJunit {
	@Resource(name="entityManagerFactory")
	private LocalContainerEntityManagerFactoryBean jpaFactory;
 

	@Test
	public void testLoader() {
		
		//jpaFactory.getPersistenceProvider().
		
		LoaderManger.loadServiceJar();
	}

}
