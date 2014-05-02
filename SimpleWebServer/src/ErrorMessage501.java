
public class ErrorMessage501 {

	public static String getError() {
		String s = "HTTP/1.1 ";
		s = s + "501 Not Implemented";
	    s = s + "\r\n"; //other header fields,
	    s = s + "Connection: keep-alive\r\n";
	    s = s + "Server:" + Constants.SERVER_NAME+"\r\n";
	    s = s + "Content-Type: text/html\r\n";
	    String content = "<body><h3>501 Error</h3><p>Not Implemented</p></body>";
	    s = s + "Content-Length: "+ content.length() + "\r\n\r\n";
	    s = s + content;
	    return s;
	}
}
