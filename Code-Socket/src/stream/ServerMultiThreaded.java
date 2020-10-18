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
	private static LinkedList<ClientThread> clientsThread;

	
	
       public static void main(String args[]){ 
        ServerSocket listenSocket;
        ServerMultiThreaded serveur = new ServerMultiThreaded();
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		
		clientsThread = new LinkedList<ClientThread>();

		
		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
		System.out.println("Server ready..."); 
		
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());

			
			ClientThread ct = new ClientThread(clientSocket,serveur);

			
			ct.start();

			
			clientsThread.add(ct);

			
			

		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
       
       synchronized public void sendMessageToAll(String msg, String pseudoSend) {
    	   for(int i = 0;i<clientsThread.size();++i) {
    		   clientsThread.get(i).sendMessage(msg,pseudoSend);;
    		   
    	   }
       }
       synchronized public void removeClient(String pseudo) {
    	   for(int i = 0 ; i < clientsThread.size() ; ++i) {
       		if(pseudo.equals(clientsThread.get(i).getPseudo())) {
       			clientsThread.remove(i);

       		}
       	}
    	  
       }
       synchronized public String givePseudoToClient(String proposition) {
    	   int id = 0;
    	  String pseudo = proposition;
    	for(int i = 0 ; i < clientsThread.size() ; ++i) {
    		if(proposition.equals(clientsThread.get(i).getPseudo())) {
    			++id;
    		}
    	}
    	if(id!=0) {
    		pseudo+= id;
    	}
    	return pseudo;
       }
  }

  
