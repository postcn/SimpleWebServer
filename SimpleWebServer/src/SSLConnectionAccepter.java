import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;


public class SSLConnectionAccepter implements Runnable {

	private Server s;
	private SSLServerSocket sslsocket;
	
	public SSLConnectionAccepter(Server s, int port) {
		this.s = s;
		System.setProperty("javax.net.ssl.keyStore", "mySrvKeystore");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		if (s.getDebug()) {
			System.setProperty("java.protocol.handler.pkgs","com.sun.net.ssl.internal.www.protocol");
			System.setProperty("javax.net.debug", "ssl");
		}
		try {
			s.logMessage("Trying to bind to ssl localhost on port " + port + "...");
			SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			sslsocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(port);
		}
		catch (Exception e) {
			s.logMessage("Fatal error occurred when trying to create ssl server socket");
			return;
		}
		s.logMessage("Success. Server ssl socket running on port " + port);
	}

	@Override
	public void run() {
		while(true) {
			try {
				SSLSocket connect = (SSLSocket) this.sslsocket.accept();
				InputStream inputstream = connect.getInputStream();
		        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
		        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

		        String string = null;
		        while ((string = bufferedreader.readLine()) != null) {
		            System.out.println(string);
		            System.out.flush();
		        }
				//connect.setKeepAlive(true);
				// InetAddress client = connect.getInetAddress();
				// if (s.whiteList.contains(client.getHostAddress())) {
				// 	s.logMessage("Client "+client.getHostName() + " connected to server.");
				// 	ConnectionHandler h = new ConnectionHandler(connect, s);
				// 	new Thread(h).start();
				// }else{
				// 	s.logMessage("\r\n==================\r\nBlocked " + client.getHostAddress() + "\r\n==================");
				// 	DeniedConnectionHandler h = new DeniedConnectionHandler(connect, s);
				// 	new Thread(h).start();
				// }				
			} catch (IOException e) {
				s.logMessage("Error during client connection");
				e.printStackTrace();
			}
		}
	}
}
