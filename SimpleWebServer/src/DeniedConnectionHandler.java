import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


public class DeniedConnectionHandler implements Runnable {

	private Socket socket;
	private Server server;
	
	public DeniedConnectionHandler(Socket socket, Server s) {
		this.socket = socket;
		this.server = s;
	}
	
	public Socket getSocket() {
		return socket;
	}

	@Override
	public void run() {
		OutputStream out = null;
		try {
			out = socket.getOutputStream();
		}
		catch(Exception e) {
			server.logMessage(e.getMessage());
		}
		
		//create the request details.
	    BufferedOutputStream output = new BufferedOutputStream(out);
	    String error = null;
	    error = ErrorMessage403.getError();
	    if (error != null) {
	    	try{
	    		output.write(error.getBytes());
	    		output.flush();
	    		server.logMessage(error);
	    	}catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }

	    try {
	    	server.logMessage("Closing Socket...");
			socket.close();
			server.logMessage("... socket closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
