import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ConnectionAccepter implements Runnable {
	private Server s;
	private ServerSocket socket;
	
	public ConnectionAccepter(Server s, int port) {
		this.s = s;
		try {
			s.logMessage("Trying to bind to localhost on port " + port + "...");
			socket = new ServerSocket(port);
//			socket.setSoTimeout(Constants.DEFAULT_TIMEOUT);		SOMETHING'S WRONG HERE
		}
		catch (Exception e) {
			s.logMessage("Fatal error occurred when trying to create server socket");
			return;
		}
		s.logMessage("Success. Server socket running on port " + port);
	}

	@Override
	public void run() {
		while(true) {
			try {
				Socket connect = this.socket.accept();
				connect.setKeepAlive(true);
//				connect.setSoTimeout(Constants.DEFAULT_TIMEOUT); I DON'T THINK THIS IS CORRECT
				InetAddress client = connect.getInetAddress();
				if (s.whiteList.contains(client.getHostAddress())) {
					s.logMessage("Client "+client.getHostName() + " connected to server.");
					ConnectionHandler h = new ConnectionHandler(connect, s);
					new Thread(h).start();
				}else{
					s.logMessage("\r\n==================\r\nBlocked " + client.getHostAddress() + "\r\n==================");
					DeniedConnectionHandler h = new DeniedConnectionHandler(connect, s);
					new Thread(h).start();
				}				
			} catch (IOException e) {
				s.logMessage("Error during client connection");
			}
		}
	}

}
