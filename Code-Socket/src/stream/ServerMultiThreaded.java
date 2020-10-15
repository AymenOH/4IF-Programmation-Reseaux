/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.LinkedList;


public class ServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
	private static LinkedList<ClientThreadRecieve> clientsThreadR;
	private static LinkedList<ClientThreadSend> clientsThreadS;
	
	
       public static void main(String args[]){ 
        ServerSocket listenSocket;
        ServerMultiThreaded serveur = new ServerMultiThreaded();
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		int id = 1;
		clientsThreadR = new LinkedList<ClientThreadRecieve>();
		clientsThreadS = new LinkedList<ClientThreadSend>();
		
		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
		System.out.println("Server ready..."); 
		
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());

			
			ClientThreadRecieve ctR = new ClientThreadRecieve(clientSocket,id,serveur);
			ClientThreadSend ctS = new ClientThreadSend(clientSocket,id,serveur);
			
			ctR.start();
			ctS.start();
			
			clientsThreadR.add(ctR);
			clientsThreadS.add(ctS);
			
			id++;

		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
       
       synchronized public void sendMessageToAll(String msg,int idSend) {
    	   for(int i = 0;i<clientsThreadS.size();++i) {
    		   clientsThreadS.get(i).sendMessage(msg,idSend);;
    		   
    	   }
       }
  }

  
