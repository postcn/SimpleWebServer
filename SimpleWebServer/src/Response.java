import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class Response {
	protected String Date;
	protected String Server;
	protected int ContentLength;
	protected String Connection;
	private String ContentType;
	private String dataLoad;
	
	public static Response parseResponse(Request r) {
		Response response = null;
		if (r.getClass().equals(GetRequest.class)) {
			response = new ResponseGet(r);
		}
		
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
	
	public void setContent(String data, String contentType) {
		this.dataLoad = data;
		this.ContentLength = dataLoad.length();
		this.ContentType = contentType;
	}
	
	public String getContent() {
		return this.dataLoad;
	}

	public String getContentType() {
		return this.ContentType;
	}
}
