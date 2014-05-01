import java.io.File;
import java.util.Scanner;


public class ResponseGet extends Response {
	String localDir;

	public ResponseGet(GetRequest r) {
		this.localDir = r.resourcePath.replace("/", File.separator);
	}
	
	public String getResource() {
		String fullDir = super.server.getPath() + localDir;
		super.server.logMessage("Client Requested Resource at "+fullDir);
		try {
			File f = new File(fullDir);
			Scanner scan = new Scanner(f);
			scan.useDelimiter("\\");
			super.dataLoad = scan.next();
			scan.close();
		}
		catch(Exception e) {
			super.server.logMessage(e.getMessage());
			return null;
		}
		return super.dataLoad;
	}

}
