
public class ErrorMessage404 {
	public static String getError() {
		String s = "HTTP/1.1 ";
		s = s + "404 Not Found";
	    s = s + "\r\n"; //other header fields,
	    s = s + "Connection: keep-alive\r\n";
	    s = s + "Server:" + Constants.SERVER_NAME+"\r\n";
	    s = s + "Content-Type: text/html\r\n";
	    String content = "<body><h2>404 Error</h2><p>File not found.</p></body>";
	    s = s + "Content-Length: "+ content.length() + "\r\n\r\n";
	    s = s + content;
	    return s;
	}
}
