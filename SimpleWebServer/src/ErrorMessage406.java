
public class ErrorMessage406 {
	
	public static String getError() {
		String s = "HTTP/1.1 ";
		s = s + "406 Not Acceptable";
	    s = s + "\r\n"; //other header fields,
	    s = s + "Connection: keep-alive\r\n";
	    s = s + "Server:" + Constants.SERVER_NAME+"\r\n";
	    s = s + "Content-Type: text/html\r\n";
	    String content = "<body><h2>406 Error</h2><p>Content Unacceptable</p></body>";
	    s = s + "Content-Length: "+ content.length() + "\r\n\r\n";
	    s = s + content;
	    return s;
	}

}
