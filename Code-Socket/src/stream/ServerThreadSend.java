package stream;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ServerThreadSend 
		extends Thread {
	private Socket serverSocket;
	private ServerThreadRecieve sr;
	private ChatWindow chat;
	private String pseudo;
	private PrintStream socOut;
	
	public ServerThreadSend(Socket serverSocket, ChatWindow chat, String pseudo) {
		this.serverSocket = serverSocket;
		this.chat = chat;
		this.pseudo = pseudo;
	}
	
	
	public void run() {
		
        try {
			socOut= new PrintStream(serverSocket.getOutputStream());
			socOut.println("pseudo:"+pseudo);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

}
	
    public void sendMessage(String msg) {
    	socOut.println(msg);
    }
    
    public void disconnect() {
    	socOut.println("disconnect");
    	socOut.close();
 
    	
    }
}
