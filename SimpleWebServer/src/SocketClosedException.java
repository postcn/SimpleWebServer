
public class SocketClosedException extends Exception {
	String message;

	public SocketClosedException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
