
public class Response {
	protected Server server;
	protected String Date;
	protected String Server;
	protected int ContentLength;
	protected String Connection;
	protected String ContentType;
	protected String dataLoad;
	
	public static Response parseResponse(Request r, Server s) {
		Response response = null;
		if (r.getClass().equals(GetRequest.class)) {
			response = new ResponseGet((GetRequest)r);
		}
		
		response.server = s;
		return response;
	}
}
