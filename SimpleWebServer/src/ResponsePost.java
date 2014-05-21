import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
		String fullDir = super.server.getPath() + localDir;
		super.server.logMessage("Client Posting Resource at "+fullDir);
//		for (String s: super.server.movedDirectories) {
//			if (fullDir.contains(s+File.separator)) {
//				throw new FileMovedException();
//			}
//		}
		try {
			File f = new File(fullDir);
//			load = new byte[(int) f.length()];
			FileOutputStream fout = new FileOutputStream(f);
			
			int loadLen = this.args.toString().length();
			byte[] load = this.args.toString().getBytes();
			fout.write(load, 0, loadLen);
			fout.close();
//			String content = URLConnection.guessContentTypeFromName(fullDir);
//			if (content == null) {
//				super.server.logMessage("Requested an unknown file type");
//			}
//			super.server.logMessage("Requested content was identified as "+content);
//			super.setContent(load, content);
		}
		catch(Exception e) {
			super.server.logMessage(e.getMessage());
			return false;
		}
		return true;
	}
}
