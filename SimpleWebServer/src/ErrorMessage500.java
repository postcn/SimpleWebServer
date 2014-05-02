
public class ErrorMessage500 {

	public static String getError() {
		String s = "HTTP/1.1 ";
		s = s + "500 Internal Server Error";
	    s = s + "\r\n"; //other header fields,
	    s = s + "Connection: keep-alive\r\n";
	    s = s + "Server: SimpleWebServer432\r\n";
	    s = s + "Content-Type: text/html\r\n";
	    String content = "<body><h3>500 Error</h3><p>Internal Server Error</p></body>";
	    s = s + "Content-Length: "+ content.length() + "\r\n\r\n";
	    s = s + content;
	    return s;
	}
}
