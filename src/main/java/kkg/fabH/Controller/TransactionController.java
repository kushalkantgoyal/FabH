package kkg.fabH.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kkg.fabH.Service.TransactionService;
import kkg.fabH.Transfer.TransactionTO;
import kkg.fabH.Util.Result;

/**
 * @author Kushal
 *
 */
@RestController
@RequestMapping("transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	private static final Logger logger = Logger.getLogger(TransactionController.class);
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Result addMoney(@RequestBody TransactionTO transactionTO){
		try {
			TransactionTO balance = transactionService.addMoney(transactionTO);
			if(balance!=null){
				return new Result(true,"Transaction completed successfully",balance);
			}
			else{
				return new Result(true,"Unable to process transaction",null);
			}
		} catch (Exception e) {
			logger.error("Exception while processing the transaction ", e);
			return new Result(false,e.getMessage(),null);
		}
	}
	
	@RequestMapping(value = "/deduct", method = RequestMethod.POST)
	@ResponseBody
	public Result deductMoney(@RequestBody TransactionTO transactionTO){
		try {
			TransactionTO balance = transactionService.deductMoney(transactionTO);
			if(balance!=null){
				return new Result(true,"Transaction completed successfully",balance);
			}
			else{
				return new Result(true,"Unable to process transaction",null);
			}
		} catch (Exception e) {
			logger.error("Exception while processing the transaction ", e);
			return new Result(false,e.getMessage(),null);
		}
	}
	
	@RequestMapping(value = "/balance/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Result deductMoney(@PathVariable Long userId){
		try {
			TransactionTO balance = transactionService.getBalance(userId);
			if(balance!=null){
				return new Result(true,"Transaction completed successfully",balance);
			}
			else{
				return new Result(true,"Unable to process transaction",null);
			}
		} catch (Exception e) {
			logger.error("Exception while processing the transaction ", e);
			return new Result(false,e.getMessage(),null);
		}
	}
	
}
