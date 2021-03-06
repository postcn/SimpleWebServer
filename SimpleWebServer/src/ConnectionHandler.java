import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


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
	    	String error = null;
	    	try {
	    		r = Request.parseRequest(in, this.server);
	    		server.logMessage(r.getFullHeader());
	    		Response resp = Response.parseResponse(r, this.server);
	    		if (resp.getResource()) {
	    			server.logMessage(resp.getCommonHeader());
	    			output.write(resp.getResponse());
	    		}else
	    			output.write(ErrorMessage404.getError().getBytes());

	    		output.flush();
	    	} catch (AccessDeniedException e){
	    		error = ErrorMessage403.getError();
	    		e.printStackTrace();
	    	} catch (SocketException e) {
	    		//client closed connection or the socket is unexpectedly closed.
	    		try {
					socket.close();
					server.logMessage("Socket was closed.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    		break;
	    	} catch (SocketTimeoutException e){
	    		try {
					socket.close();
					server.logMessage("Socket timeout. Socket closed.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    	} catch (IOException e) {
	    		error = ErrorMessage500.getError();
	    		e.printStackTrace();
	    	} catch (DetailException e) { // request type not supported - 501
	    		error = ErrorMessage501.getError();
	    		server.logMessage(e.getMessage());
	    	} catch (SocketClosedException e) {	// bad header
	    		error = ErrorMessage500.getError();
	    		server.logMessage(e.getMessage());
	    		break;
	    	} catch (MalformedHeaderException e) {
	    		server.logMessage(e.getMessage());
	    		error = ErrorMessage400.getError();
	    	} catch (FileMovedException e) {
	    		server.logMessage(e.getMessage());
	    		error = ErrorMessage301.getError();	
	    	} catch (FileNotAcceptedException e) {
	    		error = ErrorMessage406.getError();
	    	} catch (Exception e) {
				error = ErrorMessage500.getError();
	    		server.logMessage(e.getMessage());
	    	}
	    	if (error != null) {
	    		try {
					output.write(error.getBytes());
					output.flush();
					server.logMessage(error);
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    }
	    try {
			if (r != null && r.Close()) {
				socket.close();
				server.logMessage("Closing Socket");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
