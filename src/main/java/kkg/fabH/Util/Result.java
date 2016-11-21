package kkg.fabH.Util;


/**
 * @author Kushal
 *
 */
public class Result{

	protected Boolean success;
	protected String message;
	protected Object data;
	
	public Result(){
		
	}
	public Result(Boolean success, String message, Object data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
