import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	private boolean debug;
	private String path;
	ServerSocket socket;

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
		handleConnections();
	}
	
	public void logMessage(String message) {
		if (this.debug) {
			System.out.println(message);
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
}
