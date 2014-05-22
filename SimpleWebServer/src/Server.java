import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Server {
	private boolean debug;
	private String path;
	private ConnectionAccepter accepter;
	private SSLConnectionAccepter sslaccepter;
	public ArrayList<String> movedDirectories = new ArrayList<String>();
	public ArrayList<String> whiteList = new ArrayList<String>();
	public ArrayList<String> cookies = new ArrayList<String>();

	public Server(int port, int sslport,String path, boolean debug) {
		this.debug = debug;
		this.accepter = new ConnectionAccepter(this, port);
		this.sslaccepter = new SSLConnectionAccepter(this, sslport);
		if (path != null) {
			this.path = path;
		}
		else {
			this.path = System.getProperty("user.dir") +File.separator +"www";
		}
		logMessage("Server running with root directory " + this.path);
		loadMoved(); // load list of files that have moved from their original location
		loadWhiteList(); // load list of allowed IPs
		loadCookies();
		handleConnections();
	}
	
	public void logMessage(String message) {
		if (this.debug) {
			System.out.println(message);
		}
	}
	
	private void loadMoved() {
		File moved = new File(this.path + File.separator + "moved.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(moved));
			String dir = reader.readLine();
			while (dir != null) {
				movedDirectories.add(dir);
				dir = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadWhiteList(){
		File wlist = new File(this.path + File.separator + ".htaccess");
		try{
			BufferedReader reader = new BufferedReader(new FileReader(wlist));
			String address = reader.readLine();
			while (address != null) {
				if(address.equalsIgnoreCase("Deny from all")){
					break;
				}
				System.out.println(address);
				whiteList.add(address);
				address = reader.readLine();
			}
			reader.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadCookies() {
		File moved = new File(this.path + File.separator + "cookies.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(moved));
			String name = reader.readLine();
			while (name != null) {
				cookies.add(name);
				name = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getRandomCookieName() {
		Random r = new Random();
        return cookies.get(r.nextInt(cookies.size()));
	}
	
	public void handleConnections() {
		new Thread(accepter).run();
		new Thread(sslaccepter).run();
	}
	
	public String getPath() {
		return this.path;
	}

	public boolean getDebug() {
		return this.debug;
	}
}
