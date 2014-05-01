import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Request {
	protected Server server;
	protected String Host; // Domain name of the server
	protected String UserAgent; //The user agent string of the user agent
	protected String Connection; //What type of connection the user-agent would prefer
	protected String Accept; //Content-Types that are acceptable for Response
	protected String Date;
	
	public static Request parseRequest(InputStream in, Server s) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String typeLine = reader.readLine();
		s.logMessage("Client sent type header: " + typeLine);
		if (typeLine == null) {
			throw new DetailException("Bad header line from client: no header");
		}
		
		Request r;
		if (typeLine.contains("GET")) {
			r = new GetRequest(typeLine);
		}
		else if (typeLine.contains("HEAD")) {
			r = new HeadRequest(typeLine);
		}
		else if (typeLine.contains("POST")) {
			r = new PostRequest();
		}
		else {
			throw new DetailException("Request type not supported");
		}
		r.server = s;
		r.fillHeaderFields(reader);
		
		return r;
	}

	protected void fillHeaderFields(BufferedReader reader) {
		
		try {
			String line = reader.readLine();
			while (!line.equals("")) {
				//Handle the header fields we are accepting
				line = line.trim();
				int index = line.indexOf(" ");
				if (line.contains(Constants.HOST_HEADER_LINE)) {
					this.Host = line.substring(index+1);
				}
				else if (line.contains(Constants.USER_AGENT_HEADER_LINE)) {
					this.UserAgent = line.substring(index+1);
				}
				else if (line.contains(Constants.CONNECTION_HEADER_LINE)) {
					this.Connection = line.substring(index+1);
				}
				else if (line.contains(Constants.ACCEPT_HEADER_LINE)) {
					this.Accept = line.substring(index+1);
				}
				else if (line.contains(Constants.DATE_HEADER_LINE)) {
					this.Date = line.substring(index+1);
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			this.server.logMessage(e.getMessage());
		}
	}
	
	public String getCommonHeader() {
		String header = Constants.USER_AGENT_HEADER_LINE + this.UserAgent + Constants.NEWLINE;
		header += Constants.HOST_HEADER_LINE + this.Host + Constants.NEWLINE;
		header += Constants.DATE_HEADER_LINE + this.Date + Constants.NEWLINE;
		header += Constants.ACCEPT_HEADER_LINE + this.Accept + Constants.NEWLINE;
		header += Constants.CONNECTION_HEADER_LINE + this.Connection + Constants.NEWLINE;
		return header;
	}
	
	public String getFullHeader() {
		return getCommonHeader();
	}
	
	public boolean Close() {
		return this.Connection != null && this.Connection.equals("close");
	}
}
