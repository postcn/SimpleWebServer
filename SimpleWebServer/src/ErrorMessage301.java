
public class ErrorMessage301 {
	public static String getError() {
		String s = "HTTP/1.1 ";
		s = s + "301 Moved Permanently";
	    s = s + "\r\n"; //other header fields,
	    s = s + "Location: /Droids.jpg";
	    s = s + "\r\n\r\n";
	    return s;
	}
}
