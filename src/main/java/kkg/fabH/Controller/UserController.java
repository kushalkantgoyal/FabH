package kkg.fabH.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kkg.fabH.Entity.UserEntity;
import kkg.fabH.Service.UserService;
import kkg.fabH.Util.Result;

/**
 * @author Kushal
 *
 */
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Result createUser(@RequestBody UserEntity user){
		try {
			user = userService.create(user);
			if(user!=null){
				return new Result(true,"User created successfully",user);
			}
			else{
				return new Result(true,"Unable to create user",null);
			}
		} catch (Exception e) {
			logger.error("Exception while creating a new user"+user, e);
			return new Result(false,e.getMessage(),null);
		}
	}
	
}
