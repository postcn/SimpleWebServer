
public class DetailException extends Exception {
	private static final long serialVersionUID = -570219964635182420L;
	String message;

	public DetailException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
