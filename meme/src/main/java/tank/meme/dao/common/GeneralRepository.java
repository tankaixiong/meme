package tank.meme.dao.common;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import tank.meme.domain.User;

/**
 * @author tank
 * @date:18 Nov 2014 15:47:16
 * @description:
 * @version :1.0
 */
public class GeneralRepository implements IGeneralRepository {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Configure the entity manager to be used.
	 * 
	 * @param em
	 *            the {@link EntityManager} to set.
	 */
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.example.repository.UserRepositoryCustom#
	 * myCustomBatchOperation()
	 */
//	public List<User> myCustomBatchOperation() {
//
//		CriteriaQuery<User> criteriaQuery = em.getCriteriaBuilder().createQuery(User.class);
//		criteriaQuery.select(criteriaQuery.from(User.class));
//		return em.createQuery(criteriaQuery).getResultList();
//	}
}
