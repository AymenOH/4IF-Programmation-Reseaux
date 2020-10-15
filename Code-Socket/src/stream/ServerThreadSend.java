package stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerThreadSend 
		extends Thread {
	private Socket serverSocket;
	
	public ServerThreadSend(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	
	public void run() {
		PrintStream socOut = null;
        BufferedReader stdIn = null;
        Boolean pseudoDefined = false;
        try {
			socOut= new PrintStream(serverSocket.getOutputStream());
			stdIn = new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
	    
		String line;
		while (true) {
	    	try {
	    		if ( !pseudoDefined ) {
	    			System.out.print("Veuillez entrer un pseudo : ");
	    			line = stdIn.readLine();
	    			socOut.println("pseudo:"+line);
	    			
	    		}else {
	    			line=stdIn.readLine();
					if (line.equals(".")) {
						socOut.println("disconnect");
						break;
					}
			    	socOut.println(line);
	    		}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
	    }
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
