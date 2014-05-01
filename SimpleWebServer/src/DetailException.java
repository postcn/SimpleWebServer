
public class DetailException extends Exception {
	String message;

	public DetailException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
