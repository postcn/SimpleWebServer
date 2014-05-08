
public class SocketClosedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4088561685191627398L;
	String message;

	public SocketClosedException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
