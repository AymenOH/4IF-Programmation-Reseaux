package stream;

import java.io.PrintStream;
import java.net.Socket;

public class ClientThreadSend 
	extends Thread {
		
		private Socket clientSocket;
		private int id;
		private ServerMultiThreaded serveur;
		
		ClientThreadSend(Socket s, int id, ServerMultiThreaded serveur) {
			this.clientSocket = s;
			this.id = id;
			this.serveur = serveur;
		}
	
	public void sendMessage(String msg, int idSend) {
		try {
			PrintStream socOut = new PrintStream(clientSocket.getOutputStream());//ce que le serveur envoie
			if(idSend != this.id) {
				socOut.println("Client "+idSend+" said :"+msg);
			}else {
				socOut.println("You said :"+msg);
			}
			
	} catch (Exception e) {
    	System.err.println("Error in EchoServer:" + e); 
    }
	}
}
