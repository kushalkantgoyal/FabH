package kkg.fabH.DAO;

import javax.persistence.criteria.Predicate;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import kkg.fabH.Entity.UserEntity;


/**
 * @author Kushal
 *
 */
@Repository
public class UserDAO extends CommonDAO<UserEntity> {
	
	private static final Logger logger = Logger.getLogger(UserDAO.class);
	
	@Override
	public Class<UserEntity> getModelClass() {
		return UserEntity.class;
	}
	
	public UserEntity getByEmailId(String email) throws Exception{
		initCommonAttributes();
		logger.info("Begin : UserDAO.getByEmailId");
		Predicate predicate1 = builder.equal(root.get("email"),email);
		criteria.where(predicate1);
		UserEntity results = null;
		try {
			results = getSingleResult();
		} catch (Exception e) {
			logger.error("Exception while getting entity by emailId", e);
			throw new Exception("Exception while getting entity by emailId", e);
		}
		logger.info("End : UserDAO.getByEmailId");
		return results;
		
	}
	
	public UserEntity getByMobileNo(String mobileNo) throws Exception{
		initCommonAttributes();
		logger.info("Begin : UserDAO.getByPhone");
		Predicate predicate1 = builder.equal(root.get("mobileNumber"),mobileNo);
		criteria.where(predicate1);
		UserEntity results;
		try {
			results = getSingleResult();
		} catch (Exception e) {
			logger.error("Exception while getting entity by mobile no.", e);
			throw new Exception("Exception while getting entity by mobile no.", e);
		}
		logger.info("End : UserDAO.getByPhone");
		return results;
	}
}
