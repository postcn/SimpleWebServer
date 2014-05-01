
public class Response {
	protected String Date;
	protected String Server;
	protected int ContentLength;
	protected String Connection;
	protected String ContentType;
	protected String dataLoad;
	
	public static Response parseResponse(Request r) {
		Response response = null;
		if (r.getClass().equals(GetRequest.class)) {
			response = new ResponseGet((GetRequest)r);
		}
		
		return response;
	}
}
