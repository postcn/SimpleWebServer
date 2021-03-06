/*
	HAIL HYDRA!
*/
public class Main {
	public static final int DEFAULT_PORT = 8000;
	public static final int DEFAULT_SSL_PORT = 8001;
	public static int failFlag = 0;
	
	/*
		Parses the command line arguments input by the user
		and then creates the server.
	*/
	public static void main(String[] args) {
		// initialize flags for parsing
		int pFlag = 0, rFlag = 0, sFlag = 0;

		// Initialize server defaults
		int port = DEFAULT_PORT;
		int sslport = DEFAULT_SSL_PORT;
		String path = null;
		boolean debug = false;

		// parse command line arguments
		for(String str : args){
			if (str.equals("-p"))
				pFlag = 1;
			else if (str.equals("-s"))
				sFlag = 1;
			else if (str.equals("-root"))
				rFlag = 1;
			else if (str.equals("-failPostTo500"))
				failFlag = 1;
			else if (str.equals("-debug") || str.equals("-d"))
				debug = true;
			else{
				if ((pFlag == 1) && (Integer.parseInt(str) > 0)) {
					// set port and reset flag
					port = (Integer.parseInt(str));
					pFlag = 0;
				}else if (rFlag == 1) {
					// set root folder and reset flag
					path = str;
					rFlag = 0;
				}else if (sFlag == 1) {
					sslport = (Integer.parseInt(str));
					sFlag = 0;
				}else{
					// print message and quit
					System.out.println("Invlaid arguments.\n");
					return;
				}
			}
		}
		// Spawn server
		System.setProperty("java.net.preferIPv4Stack", "true");	// prefer IPv4 addressing
		new Server(port,sslport,path, debug);
	}

}
