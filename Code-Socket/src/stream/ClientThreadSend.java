package stream;

import java.io.PrintStream;
import java.net.Socket;

public class ClientThreadSend 
	extends Thread {
		
		private Socket clientSocket;
		private String pseudo;
		private ServerMultiThreaded server;
		
		ClientThreadSend(Socket s, ServerMultiThreaded server) {
			this.clientSocket = s;
			this.server = server;
			pseudo="Anonyme";
		}
		public String getPseudo() {
			return pseudo;
		}
		public void setPseudo(String pseudo) {
			this.pseudo = pseudo;
		}
	public void sendMessage(String msg, String pseudoSend) {
		
		try {
			PrintStream socOut = new PrintStream(clientSocket.getOutputStream());//ce que le serveur envoie
			if(!(pseudoSend.equals(pseudo))){
				socOut.println(pseudo+" said :"+msg);
			}else {
				socOut.println("You said :"+msg);
			}
			
	} catch (Exception e) {
    	System.err.println("Error in EchoServer:" + e); 
    }
	}
	
}
