import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
	private boolean debug;
	private String path;
	ServerSocket socket;
	public ArrayList<String> movedDirectories = new ArrayList<String>();

	public Server(int port, String path, boolean debug) {
		this.debug = debug;
		try {
			logMessage("Trying to bind to localhost on port " + port + "...");
			socket = new ServerSocket(port);
		}
		catch (Exception e) {
			logMessage("Fatal error occurred when trying to create server socket");
		}
		logMessage("Success. Server socket running on port " + port);
		if (path != null) {
			this.path = path.replace(".", System.getProperty("user.dir"));
		}
		else {
			this.path = System.getProperty("user.dir") +File.separator +"www";
		}
		logMessage("Server running with root directory " + this.path);
		loadMoved();
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
	
	public void handleConnections() {
		while(true) {
			try {
				Socket connect = this.socket.accept();
				connect.setKeepAlive(true);
				InetAddress client = connect.getInetAddress();
				logMessage("Client "+client.getHostName() + " connected to server.");
				ConnectionHandler h = new ConnectionHandler(connect, this);
				new Thread(h).start();
			        
			} catch (IOException e) {
				logMessage("Error during client connection");
			}
		}
	}
	
	public String getPath() {
		return this.path;
	}
}
