
public class ErrorMessage400 {
	public static String getError() {
		String s = "HTTP/1.1 ";
		s = s + "400 Bad Request";
	    s = s + "\r\n"; //other header fields,
	    s = s + "Connection: keep-alive\r\n";
	    s = s + "Server:" + Constants.SERVER_NAME+"\r\n";
	    s = s + "Content-Type: text/html\r\n";
	    String content = "<body><h2>400 Error</h2><p>Malformed Header in Request</p></body>";
	    s = s + "Content-Length: "+ content.length() + "\r\n\r\n";
	    s = s + content;
	    return s;
	}
}
