import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;


public class PostRequest extends Request {
	String payload = "";
	String resourcePath;
	String boundary;
	String contentLength;
	HashMap<String, String> args;
	
	public PostRequest(String typeLine) {
		this.resourcePath = typeLine.split(" ")[1];
	}

	@Override
	protected void fillHeaderFields(BufferedReader reader) throws MalformedHeaderException {
		super.fillHeaderFields(reader);
		for (String s: skippedHeaderLines) {
			if (s.indexOf("Content-Type: multipart/form-data") != -1) {
				boundary = s.split("boundary=")[1];
			}
			if (s.indexOf("Content-Length:") != -1) {
				contentLength = s.split(" ")[1];
			}
		}
		args = new HashMap<String, String>();
		String line;
		try {
			while (reader.ready()) {
				//get to boundary line
				while (true) {
					line = reader.readLine();
					if (line.indexOf("--" + boundary) != -1) {
						break;
					}
					if (args.keySet().size() > 0 && line.equals("")) {
						break;
					}
				}
				if (!reader.ready()) {
					break;
				}
				String key;
				//get key out.
				line = reader.readLine();
				key = line.split("name=")[1].replace("\"", "");
				while (true) {
					line = reader.readLine();
					if (!line.equals("")) {
						break;
					}
				}
				args.put(key, line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getFullHeader() throws AccessDeniedException {
		if(resourcePath.contains(".htaccess"))
			throw new AccessDeniedException();
		else {
			String ret = "POST " + resourcePath + " HTTP/1.1" + Constants.NEWLINE + super.getCommonHeader();
			for (String key: args.keySet()) {
				ret += Constants.NEWLINE + "Key: " + key + " \tValue: "+args.get(key);
			}
			return ret;
		}
	}
}
