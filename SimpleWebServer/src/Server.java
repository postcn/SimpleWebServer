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
//				BufferedReader input = new BufferedReader(new InputStreamReader(connect.getInputStream()));
//			    DataOutputStream output = new DataOutputStream(connect.getOutputStream());
//			    httpHandle(input,output);
			        
			} catch (IOException e) {
				logMessage("Error during client connection");
			}
		}
	}
	
//	public void httpHandle(BufferedReader input, DataOutputStream output) {
//		ContentTypes method = ContentTypes.UNSUPPORTED;
//	    String http = new String();
//	    String path = new String();
//	    String file = new String();
//	    String user_agent = new String();
//	    try {
//			output.writeBytes(construct_http_header(501,0));
//			output.flush();
//			logMessage(construct_http_header(501,0));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	  //this method makes the HTTP header for the response
//	  //the headers job is to tell the browser the result of the request
//	  //among if it was successful or not.
//	  private String construct_http_header(int return_code, int file_type) {
//	    String s = "HTTP/1.1 ";
//	    switch (return_code) {
//	      case 200:
//	        s = s + "200 OK";
//	        break;
//	      case 400:
//	        s = s + "400 Bad Request";
//	        break;
//	      case 403:
//	        s = s + "403 Forbidden";
//	        break;
//	      case 404:
//	        s = s + "404 Not Found";
//	        break;
//	      case 500:
//	        s = s + "500 Internal Server Error";
//	        break;
//	      case 501:
//	        s = s + "501 Not Implemented";
//	        break;
//	    }
//
//	    s = s + "\r\n"; //other header fields,
//	    s = s + "Connection: keep-alive\r\n";
//	    s = s + "Server: SimpleWebServer432\r\n"; //server name
//
//	    //Construct the right Content-Type for the header.
//	    //This is so the browser knows what to do with the
//	    //file, you may know the browser dosen't look on the file
//	    //extension, it is the servers job to let the browser know
//	    //what kind of file is being transmitted. You may have experienced
//	    //if the server is miss configured it may result in
//	    //pictures displayed as text!
//	    switch (file_type) {
//	      //plenty of types for you to fill in
//	      case 0:
//	        break;
//	      case 1:
//	        s = s + "Content-Type: image/jpeg\r\n";
//	        break;
//	      case 2:
//	        s = s + "Content-Type: image/gif\r\n";
//	      case 3:
//	        s = s + "Content-Type: application/x-zip-compressed\r\n";
//	      default:
//	        s = s + "Content-Type: text/html\r\n";
//	        break;
//	    }
//
//	    s = s + "\r\n"; //this marks the end of the httpheader
//	    return s;
//	  }
}
