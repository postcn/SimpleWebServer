import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class Request {
	protected Server server;
	protected String Host; // Domain name of the server
	protected String UserAgent; //The user agent string of the user agent
	protected String Connection; //What type of connection the user-agent would prefer
	protected String Accept; //Content-Types that are acceptable for Response
	protected String Language; //Language that is acceptable for Response
	protected HashMap<String, String> Cookies;
	
	public static Request parseRequest(InputStream in, Server s) throws DetailException, MalformedHeaderException, SocketClosedException, IOException {
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
			r = new HeadRequest(typeLine);
		}
		else if (typeLine.contains("POST")) {
			if (Main.failFlag == 1) {
				throw new IOException();
			}
			r = new PostRequest();
		}
		else {
			throw new DetailException("Request type not supported");
		}
		r.server = s;
		r.fillHeaderFields(reader);
		
		return r;
	}

	protected void fillHeaderFields(BufferedReader reader) throws MalformedHeaderException {
		Cookies = new HashMap<String, String>();
		try {
			String line = reader.readLine();
			while (!line.equals("")) {
				//Handle the header fields we are accepting
				line = line.trim();
				int index = line.indexOf(Constants.SPLIT);
				if (index < 0) {
					throw new MalformedHeaderException();
				}
				String headerType = line.substring(0,index);
				String headerValue = line.substring(index+2);
				if (headerType.equals(Constants.HOST_HEADER_LINE)) {
					this.Host = headerValue;
				}
				else if (headerType.equals(Constants.USER_AGENT_HEADER_LINE)) {
					this.UserAgent = headerValue;
				}
				else if (headerType.equals(Constants.CONNECTION_HEADER_LINE)) {
					this.Connection = headerValue;
				}
				else if (headerType.equals(Constants.ACCEPT_HEADER_LINE)) {
					this.Accept = headerValue;
				}
				else if (headerType.equals(Constants.ACCEPT_LANGUAGE_LINE)){
					this.Language = headerValue;
				} 
				else if (headerType.equals(Constants.COOKIE_HEADER_LINE)) {
					//parse the cookies.
					for (String cookie: headerValue.split(Constants.COOKIE_SEPERATOR)) {
						String key = cookie.split(Constants.COOKIE_VALUE_SEPERATOR)[0];
						String value = cookie.split(Constants.COOKIE_VALUE_SEPERATOR)[1];
						this.Cookies.put(key, value);
					}
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			this.server.logMessage(e.getMessage());
		}
	}
	
	public String getCommonHeader() {
		String header = Constants.USER_AGENT_HEADER_LINE + Constants.SPLIT + this.UserAgent + Constants.NEWLINE;
		header += Constants.HOST_HEADER_LINE + Constants.SPLIT + this.Host + Constants.NEWLINE;
		header += Constants.ACCEPT_HEADER_LINE + Constants.SPLIT + this.Accept + Constants.NEWLINE;
		header += Constants.ACCEPT_LANGUAGE_LINE + Constants.SPLIT + this.Language + Constants.NEWLINE;
		header += Constants.CONNECTION_HEADER_LINE + Constants.SPLIT + this.Connection + Constants.NEWLINE;
		if (this.Cookies.keySet().size() > 0) {
			header += Constants.COOKIE_HEADER_LINE + Constants.SPLIT;
			for (String key: Cookies.keySet()) {
				header += key + Constants.COOKIE_VALUE_SEPERATOR + Cookies.get(key) + Constants.COOKIE_SEPERATOR;
			}
			header += Constants.NEWLINE;
		}
		return header;
	}
	
	public String getFullHeader() throws AccessDeniedException {
		return getCommonHeader();
	}
	
	public boolean Close() {
		return this.Connection != null && this.Connection.equals("close");
	}
}
