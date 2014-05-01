import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Request {
	public final static String NEWLINE = "\r\n";
	public final static String DATE_HEADER_LINE = "Date: ";
	public final static String ACCEPT_HEADER_LINE = "Accept: ";
	public final static String CONNECTION_HEADER_LINE = "Connection: ";
	public final static String USER_AGENT_HEADER_LINE = "User-Agent: ";
	public final static String HOST_HEADER_LINE = "Host: ";
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
			throw new SocketClosedException("Bad header line from client: no header");
		}
		
		Request r;
		if (typeLine.contains("GET")) {
			r = new GetRequest(typeLine);
		}
		else if (typeLine.contains("HEAD")) {
			r = new HeadRequest();
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
				int index = line.indexOf(' ');
				if (line.contains(HOST_HEADER_LINE)) {
					this.Host = line.substring(index+1);
				}
				else if (line.contains(USER_AGENT_HEADER_LINE)) {
					this.UserAgent = line.substring(index+1);
				}
				else if (line.contains(CONNECTION_HEADER_LINE)) {
					this.Connection = line.substring(index+1);
				}
				else if (line.contains(ACCEPT_HEADER_LINE)) {
					this.Accept = line.substring(index+1);
				}
				else if (line.contains(DATE_HEADER_LINE)) {
					this.Date = line.substring(index+1);
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			this.server.logMessage(e.getMessage());
		}
	}
	
	public String getCommonHeader() {
		String header = USER_AGENT_HEADER_LINE + this.UserAgent + NEWLINE;
		header += HOST_HEADER_LINE + this.Host + NEWLINE;
		header += DATE_HEADER_LINE + this.Date + NEWLINE;
		header += ACCEPT_HEADER_LINE + this.Accept + NEWLINE;
		header += CONNECTION_HEADER_LINE + this.Connection + NEWLINE;
		return header;
	}
	
	public String getFullHeader() {
		return getCommonHeader();
	}
	
	public boolean Close() {
		return this.Connection.equals("Closed");
	}
}
