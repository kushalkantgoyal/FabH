package kkg.fabH.DAO;

import java.util.Date;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import kkg.fabH.Entity.TransactionEntity;
import kkg.fabH.Entity.UserEntity;


/**
 * @author Kushal
 *
 */
@Repository
public class TransactionDAO extends CommonDAO<TransactionEntity> {
	
	private static final Logger logger = Logger.getLogger(TransactionDAO.class);
	
	@Override
	public Class<TransactionEntity> getModelClass() {
		return TransactionEntity.class;
	}
	
	public TransactionEntity getLastTransactionByUser(UserEntity user) throws Exception{
		initCommonAttributes();
		logger.info("Begin : TransactionDAO.getLastTransactionByUser");
		Predicate predicate1 = builder.equal(root.get("user"),user);
		Order order = builder.desc(root.<Date>get("createdTime"));
		criteria.where(predicate1);
		criteria.orderBy(order);
		TransactionEntity results = null;
		try {
			results = getFirstResult();
		} catch (Exception e) {
			logger.error("Exception while getting last transaction by User", e);
			throw new Exception("Exception while getting last transaction by User", e);
		}
		logger.info("End : TransactionDAO.getLastTransactionByUser");
		return results;
		
	}
}
