/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	private ServerMultiThreaded server;
	private String pseudo;
	
	ClientThread(Socket s, ServerMultiThreaded server) {
		this.clientSocket = s;
		this.server = server;
		this.pseudo = "Anonyme";
	}
	
 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	
	/**
	  * Résumé du rôle de la méthode.
	  * Commentaires détaillés sur le role de la methode
	  * @param val la valeur a traiter
	  * @return la valeur calculée
	  */
	
	public void run() {
    	  try {
    		BufferedReader socIn = null;
    		
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream())); //ce qui arrive du client   
    		while (true) {
    		  String line = socIn.readLine();//message du client
    		  if(!line.equals("disconnect") && !line.contains("pseudo") ) {
    			  server.sendMessageToAll(line,pseudo);
    		  }else if (line.contains("pseudo") ) {
    			  String tab [] = line.split(":");
    			  pseudo = server.givePseudoToClient(tab[1]);
    			  
    		  }else {
    			  System.out.println("disco test 1 "+pseudo);
    			  server.removeClient(pseudo);
    			  System.out.println("disco test 2 "+pseudo);
    			  socIn.close();
    			  System.out.println("disco test 3 "+pseudo);
    			  clientSocket.close();
    			  System.out.println("disco test 4 "+pseudo);
    		  }
    		}
    	} catch (Exception e) {
        	System.err.println("Error in ClientThread:" + e); 
        }
       }
	
	/**
	  * Résumé du rôle de la méthode.
	  * Commentaires détaillés sur le role de la methode
	  * @param val la valeur a traiter
	  * @return la valeur calculée
	  */
	public void sendMessage(String msg, String pseudoSend) {
			
		try {
			PrintStream socOut = new PrintStream(clientSocket.getOutputStream());//ce que le serveur envoie
			if(!(pseudoSend.equals(pseudo))){
				socOut.println(pseudoSend+" said :"+msg);
			}else {
				socOut.println("You said :"+msg);
			}
	
			
	} catch (Exception e) {
    	System.err.println("Error in EchoServer:" + e); 
    }
	}
	
  
  }

  
