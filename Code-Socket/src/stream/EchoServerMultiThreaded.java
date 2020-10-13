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


public class EchoServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
	private static LinkedList<ClientThread> clients;
	
	
       public static void main(String args[]){ 
        ServerSocket listenSocket;
        EchoServerMultiThreaded serveur = new EchoServerMultiThreaded();
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		int id = 1;
		clients = new LinkedList<ClientThread>();
		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
		System.out.println("Server ready..."); 
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());
			//System.out.println("Client "+id+" has joined the Chat");
			
			ClientThread ct = new ClientThread(clientSocket,id,serveur);
			clients.add(ct);
			id++;
			ct.start();
			
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
       
       synchronized public void sendMessageToAll(String msg,int idSend) {
    	   for(int i = 0;i<clients.size();++i) {
    		   System.out.println("test avant "+i);
    		   clients.get(i).sendMessage(msg,idSend);;
    		   System.out.println("test apres "+i);
    		   
    	   }
       }
  }

  
