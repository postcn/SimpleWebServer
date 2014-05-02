import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	private byte[] dataLoad;
	
	public static Response parseResponse(Request r, Server s) {
		Response response = null;
		if (r.getClass().equals(GetRequest.class)) {
			response = new ResponseGet((GetRequest)r);
		}
		
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
		if (dataLoad != null) {
			s += Constants.CONTENT_TYPE_HEADER_LINE + Constants.SPLIT + this.ContentType + Constants.NEWLINE;
			s += Constants.CONTENT_LENGTH_HEADER_LINE + Constants.SPLIT + this.ContentLength + Constants.NEWLINE;
		}
		s += Constants.NEWLINE;
		return s;
	}
	
	public byte[] getResponse() {
		String s = getCommonHeader();
		return s.getBytes();
	}
	
	public boolean getResource() {
		return false;
	}

	public void send(OutputStream stream) throws IOException {
		stream.write(this.getResponse());
		stream.flush();
	}
}
