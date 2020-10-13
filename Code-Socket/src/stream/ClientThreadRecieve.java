/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientThreadRecieve
	extends Thread {
	
	private Socket clientSocket;
	private int id;
	private ServerMultiThreaded serveur;
	
	ClientThreadRecieve(Socket s, int id, ServerMultiThreaded serveur) {
		this.clientSocket = s;
		this.id = id;
		this.serveur = serveur;
	}
	
 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream())); //ce qui arrive du client   
    		while (true) {
    		  String line = socIn.readLine();//message du client
    		  if(!line.isEmpty()) {
    			  serveur.sendMessageToAll(line,this.id);
    		  }
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
	
  
  }

  
