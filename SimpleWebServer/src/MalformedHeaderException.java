
public class MalformedHeaderException extends Exception {

	private static final long serialVersionUID = 6566089259831877639L;

	public String getMessage() {
		return "Malformed Header Line in Request";
	}
}
