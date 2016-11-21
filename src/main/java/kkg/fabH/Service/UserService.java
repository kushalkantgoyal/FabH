package kkg.fabH.Service;

import kkg.fabH.DAO.UserDAO;
import kkg.fabH.Entity.UserEntity;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kushal
 *
 */
@Service("userService")
@Transactional
public class UserService {
	@Autowired
	private UserDAO userDAO;
	private static final Logger logger = Logger.getLogger(UserService.class);
	
	public UserEntity create(UserEntity user) throws Exception{
		try{
			if(StringUtils.isEmpty(user.getFirstName())){
				throw new Exception("First Name Required. Can't create user: "+user);
			}
			if(checkIfEmailExists(user.getEmail())){
				throw new Exception("Email already Exists. Can't create user: "+user);
			}
			
			if(checkIfMobileNoExists(user.getMobileNumber())){
				throw new Exception("Mobile No. already Exists. Can't create user: "+user);
			}
			
			userDAO.create(user);
			return user;
		}catch(Exception e){
			logger.error("Exception while creating user: "+user, e);
			throw new Exception("Exception while creating user: "+e.getMessage(), e);
		}
	}
	
	boolean checkIfEmailExists(String email) throws Exception{
		UserEntity user = userDAO.getByEmailId(email);
		if(user != null){
			return true;
		}else{
			return false;
		}
	}
	
	boolean checkIfMobileNoExists(String email) throws Exception{
		UserEntity user = userDAO.getByMobileNo(email);
		if(user != null){
			return true;
		}else{
			return false;
		}
	}
	
}
