import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ConnectionHandler implements Runnable {

	private Socket socket;
	private Server server;
	
	public ConnectionHandler(Socket socket, Server s) {
		this.socket = socket;
		this.server = s;
	}
	
	public Socket getSocket() {
		return socket;
	}
	@Override
	public void run() {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		}
		catch(Exception e) {
			server.logMessage(e.getMessage());
		}
		
		//create the request details.
	    BufferedOutputStream output = new BufferedOutputStream(out);
	    Request r = null;
	    while (! socket.isClosed()) {
	    	try {
	    		r = Request.parseRequest(in, this.server);
	    		server.logMessage(r.getFullHeader());
	    		Response resp = Response.parseResponse(r, this.server);
	    		if (resp.getResource()) {
	    			server.logMessage(resp.getResponse().toString());
	    			output.write(resp.getResponse());
	    		}else
	    			output.write(ErrorMessage404.getError().getBytes());

	    		output.flush();
	    	} catch (IOException e) {
	    		output.write(ErrorMessage505.getError().getBytes());
	    		e.printStackTrace();
	    	} catch (DetailException e) { // request type not supported - 501
	    		output.write(ErrorMessage501.getError().getBytes());
	    		server.logMessage(e.getMessage());
	    	} catch (SocketClosedException e) {	// bad header
	    		output.write(ErrorMessage505.getError().getBytes());
	    		server.logMessage(e.getMessage());
	    		break;
	    	} catch (Exception e) {
	    		output.write(ErrorMessage505.getError().getBytes());
	    		server.logMessage(e.getMessage()+"Hello Bob");
	    	}
	    }
	    try {
			if (r != null && r.Close()) {
				socket.close();
				server.logMessage("Closing Socket");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
