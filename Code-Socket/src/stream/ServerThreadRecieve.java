package stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThreadRecieve 
		extends Thread {
	private Socket serverSocket;
	
	public ServerThreadRecieve(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void run() {
		BufferedReader socIn = null;
		try {
			socIn = new BufferedReader(
			          new InputStreamReader(serverSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		while (true) {
	    	try {
				System.out.println(">>>>> "+socIn.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
	    }


	}
	
	
}
