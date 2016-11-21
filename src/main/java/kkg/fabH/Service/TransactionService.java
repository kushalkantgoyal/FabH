package kkg.fabH.Service;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kkg.fabH.DAO.TransactionDAO;
import kkg.fabH.Entity.TransactionEntity;
import kkg.fabH.Entity.UserEntity;
import kkg.fabH.Transfer.TransactionTO;
import kkg.fabH.Util.TransactionType;

/**
 * @author Kushal
 *
 */
@Service("TransactionService")
@Transactional
public class TransactionService {
	private static final Logger logger = Logger.getLogger(TransactionService.class);

	private static ConcurrentHashMap<Long, Object> lockMap = new ConcurrentHashMap<Long, Object>();

	@Autowired
	private TransactionDAO transactionDAO;

	public TransactionTO addMoney(TransactionTO transactionTO) throws Exception{
		try{
			TransactionTO balance = new TransactionTO();
			if(transactionTO.getUserId() != null){
				if(transactionTO.getAmount() != null){
					TransactionEntity transactionEntity = new TransactionEntity();
					UserEntity user = new UserEntity();
					user.setId(transactionTO.getUserId());
					transactionEntity.setUser(user);
					transactionEntity.setTransactionType(TransactionType.CREDIT);
					transactionEntity.setAmount(transactionTO.getAmount());
					synchronized (user.getId()) {
						TransactionTO currentBalance = getBalance(transactionTO.getUserId());
						transactionEntity.setCurrentBalance(currentBalance.getAmount() + transactionTO.getAmount());
						TransactionEntity transaction = transactionDAO.create(transactionEntity);
						balance.setUserId(transactionTO.getUserId());
						balance.setAmount(transactionEntity.getCurrentBalance());
					}

				}else{
					throw new Exception("Amount should be non empty. Can't process transaction");
				}
			}else{
				throw new Exception("User should be non empty. Can't process transaction");
			}
			return balance;
		}catch(Exception e){
			logger.error("Exception while processing transaction", e);
			throw new Exception("Exception while processing transaction "+e.getMessage(), e);
		}
	}

	public TransactionTO deductMoney(TransactionTO transactionTO) throws Exception{
		try{
			TransactionTO balance = new TransactionTO();
			if(transactionTO.getUserId() != null){
				if(transactionTO.getAmount() != null){
					TransactionEntity transactionEntity = new TransactionEntity();
					UserEntity user = new UserEntity();
					user.setId(transactionTO.getUserId());
					transactionEntity.setUser(user);
					transactionEntity.setTransactionType(TransactionType.DEBIT);
					transactionEntity.setAmount(transactionTO.getAmount());
					synchronized (user.getId()) {
						TransactionTO currentBalance = getBalance(transactionTO.getUserId());
						if(currentBalance.getAmount() >= transactionTO.getAmount()){
							transactionEntity.setCurrentBalance(currentBalance.getAmount() - transactionTO.getAmount());
							TransactionEntity transaction = transactionDAO.create(transactionEntity);
							balance.setUserId(transactionTO.getUserId());
							balance.setAmount(transactionEntity.getCurrentBalance());
						}else{
							throw new Exception("Balance is low. Can't process transaction");
						}
					}

				}else{
					throw new Exception("Amount should be non empty. Can't process transaction");
				}
			}else{
				throw new Exception("User should be non empty. Can't process transaction");
			}
			return balance;
		}catch(Exception e){
			logger.error("Exception while processing transaction", e);
			throw new Exception("Exception while processing transaction "+e.getMessage(), e);
		}
	}

	public TransactionTO getBalance(Long userId) throws Exception{
		try{
			TransactionTO transactionTO = new TransactionTO();
			if(userId != null){
				UserEntity user = new UserEntity();
				user.setId(userId);
				TransactionEntity transaction = transactionDAO.getLastTransactionByUser(user);
				transactionTO.setUserId(userId);
				if(transaction != null){
					transactionTO.setAmount(transaction.getCurrentBalance());
				}else{
					transactionTO.setAmount(0D);
				}
			}else{
				throw new Exception("User should not be empty.");
			}
			return transactionTO;
		}catch(Exception e){
			logger.error("Error while getting current balance. ", e);
			throw new Exception("Error while getting current balance. "+e.getMessage(), e);
		}
	}
}
