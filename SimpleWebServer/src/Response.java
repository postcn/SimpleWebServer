import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.io.OutputStream;


public class Response {
	protected Server server;
	protected String Date;
	protected String Server;
	protected int ContentLength;
	protected String Connection;
	private String ContentType;
	private HashMap<String, String> Cookies;
	private byte[] dataLoad;
	
	public static Response parseResponse(Request r, Server s) {
		Response response = null;
		
		if (r.getClass().equals(GetRequest.class)) {
			response = new ResponseGet((GetRequest)r);
		}
		else if (r.getClass().equals(HeadRequest.class)) {
			response = new ResponseHead((HeadRequest)r);
		}
		
		if (r.Cookies.keySet().size() > 0) {
			response.Cookies = r.Cookies;
			//add one to the cookie for a proper request.
			String key = response.Cookies.keySet().iterator().next();
			String value = response.Cookies.get(key);
			Integer count = Integer.parseInt(value) + 1;
			s.logMessage("\nClient has now requested " + count + " pages from the server.\n");
			response.Cookies.put(key, count.toString());
		}
		else {
			response.Cookies = new HashMap<String, String>();
			response.Cookies.put(s.getRandomCookieName(), ""+1);
		}
		response.Connection = r.Connection;
		response.server = s;
		return response;
	}
	
	public Response() {
		this.Date = getServerTime();
		this.Server = Constants.SERVER_NAME;
	}
	
	/* Thanks Stackoverflow! */
	private String getServerTime() {
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}
	
	private String getCookieExpiration() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}
	
	public void setContent(byte[] data, String contentType) {
		this.dataLoad = data;
		this.ContentLength = dataLoad.length;
		this.ContentType = contentType;
	}
	
	public byte[] getContent() {
		return this.dataLoad;
	}

	public String getContentType() {
		return this.ContentType;
	}
	
	public String getCommonHeader() {
		String s = Constants.DATE_HEADER_LINE + Constants.SPLIT + this.Date + Constants.NEWLINE;
		s += Constants.SERVER_HEADER_LINE + Constants.SPLIT + Constants.SERVER_NAME + Constants.NEWLINE;
		s += Constants.CONNECTION_HEADER_LINE + Constants.SPLIT + this.Connection + Constants.NEWLINE;
		s += "Access-Control-Allow-Origin:*"+Constants.NEWLINE;
		if (dataLoad != null) {
			s += Constants.CONTENT_TYPE_HEADER_LINE + Constants.SPLIT + this.ContentType + Constants.NEWLINE;
			s += Constants.CONTENT_LENGTH_HEADER_LINE + Constants.SPLIT + this.ContentLength + Constants.NEWLINE;
		}
		for (String key: Cookies.keySet()) {
			String value = Cookies.get(key);
			s += Constants.SET_COOKIE_HEADER_LINE + Constants.SPLIT + key + Constants.COOKIE_VALUE_SEPERATOR + value + Constants.COOKIE_SEPERATOR + Constants.EXPIRES + getCookieExpiration() + Constants.NEWLINE; 
		}
		s += Constants.NEWLINE;
		return s;
	}
	
	public byte[] getResponse() {
		String s = getCommonHeader();
		return s.getBytes();
	}
	
	public boolean getResource() throws FileMovedException {
		return false;
	}

	public void send(OutputStream stream) throws IOException {
		stream.write(this.getResponse());
		stream.flush();
	}
}
