package stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ServerThreadRecieve 
		extends Thread {
	private Socket serverSocket;
	private ChatWindow chat;
	BufferedReader socIn ;
	
	public ServerThreadRecieve(Socket serverSocket,ChatWindow chat) {
		this.serverSocket = serverSocket;
		this.chat = chat;
	}
	
	public void run() {
		
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
				//System.out.println(">>>>> "+socIn.readLine());
				String msg = socIn.readLine();
        		chat.textAreaChatIn.append(msg);
				
	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
	    }
		


	}
	
	
}
