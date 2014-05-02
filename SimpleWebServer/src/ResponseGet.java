import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;


public class ResponseGet extends Response {
	String localDir;

	public ResponseGet(GetRequest r) {
		this.localDir = r.resourcePath.replace("/", File.separator);
	}
	
	public boolean getResource() throws FileMovedException {
		String fullDir = super.server.getPath() + localDir;
		byte[] load = null;
		super.server.logMessage("Client Requested Resource at "+fullDir);
		for (String s: super.server.movedDirectories) {
			if (fullDir.contains(s)) {
				throw new FileMovedException();
			}
		}
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
	public byte[] getResponse() {
		String s = "HTTP/1.1 200 OK" + Constants.NEWLINE;
		s += super.getCommonHeader();
		byte[] header = s.getBytes();
		byte[] content = super.getContent();
		byte[] combined = new byte[header.length + content.length];
		System.arraycopy(header, 0, combined, 0, header.length);
		System.arraycopy(content, 0, combined, header.length, content.length);
		return combined;
	}
}
