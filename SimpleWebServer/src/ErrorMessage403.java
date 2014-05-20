
public class ErrorMessage403 {
	public static String getError() {
		String s = "HTTP/1.1 ";
		s = s + "403 Access Denied";
	    s = s + "\r\n"; //other header fields,
	    s = s + "Connection: keep-alive\r\n";
	    s = s + "Server:" + Constants.SERVER_NAME+"\r\n";
	    s = s + "Content-Type: text/html\r\n";
	    String content = "<body><h2>403 Error</h2><p>Access denied.</p><br /><br /><br /><br /><img src='403Bauer.gif' /></body>";
	    s = s + "Content-Length: "+ content.length() + "\r\n\r\n";
	    s = s + content;
	    return s;
	}
}
