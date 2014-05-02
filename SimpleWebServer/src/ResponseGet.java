import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;


public class ResponseGet extends Response {
	String localDir;

	public ResponseGet(GetRequest r) {
		this.localDir = r.resourcePath.replace("/", File.separator);
	}
	
	public boolean getResource() {
		String fullDir = super.server.getPath() + localDir;
		byte[] load = null;
		super.server.logMessage("Client Requested Resource at "+fullDir);
		try {
			File f = new File(fullDir);
			load = new byte[(int) f.length()];
			FileInputStream fin = new FileInputStream(f);
			fin.read(load);
			fin.close();
			String content = URLConnection.guessContentTypeFromName(fullDir);
			if (content == null) {
				super.server.logMessage("Requested an unknown file type");
			}
			super.server.logMessage("Requested content was identified as "+content);
			super.setContent(load, content);
		}
		catch(Exception e) {
			super.server.logMessage(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public String getResponse() {
		String s = "HTTP/1.1 200 OK" + Constants.NEWLINE;
		s += super.getCommonHeader();
		for (byte b: super.getContent()) {
			s += (char) b;
		}
		return s;
	}
}
