import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.util.HashMap;


public class ResponsePost extends Response {
	String localDir;
	HashMap<String, String> args;
	
	public ResponsePost(PostRequest r) {
		this.localDir = r.resourcePath.replace("/", File.separator);
		this.args = r.args;
	}

	public boolean getResource() throws FileMovedException {
//		String fullDir = super.server.getPath() + localDir;
//		byte[] load = null;
//		super.server.logMessage("Client Requested Resource at "+fullDir);
//		for (String s: super.server.movedDirectories) {
//			if (fullDir.contains(s+File.separator)) {
//				throw new FileMovedException();
//			}
//		}
//		try {
//			File f = new File(fullDir);
//			load = new byte[(int) f.length()];
//			FileInputStream fin = new FileInputStream(f);
//			fin.read(load);
//			fin.close();
//			String content = URLConnection.guessContentTypeFromName(fullDir);
//			if (content == null) {
//				super.server.logMessage("Requested an unknown file type");
//			}
//			super.server.logMessage("Requested content was identified as "+content);
//			super.setContent(load, content);
//		}
//		catch(Exception e) {
//			super.server.logMessage(e.getMessage());
//			return false;
//		}
//		return true;
		return false;
	}
}
