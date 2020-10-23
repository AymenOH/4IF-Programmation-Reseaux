/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.LinkedList;


public class ServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
	private static LinkedList<ClientThread> clientsThread;
	/* Chemin absolu du fichier qui contient l'historique des messages*/
	protected static final String historicPath = "C:/Users/Aymen/git/4IF-Programmation-Reseaux/Code-Socket/src/stream/historic.txt";
	
	
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
    		   clientsThread.get(i).sendMessage(msg,pseudoSend);
    	   }
    	   
    	   saveMessage(msg,pseudoSend);
    	   
       }
       
       /**
  	  * Résumé du rôle de la méthode.
  	  * Commentaires détaillés sur le role de la methode
  	  * @param val la valeur a traiter
  	  * @return la valeur calculée
  	  */
       synchronized public void saveMessage(String msg, String pseudoSend) {
    	   
    	   String message = "-->" + pseudoSend + " : " + msg;
    	   Date date = new Date();
    	   try {
    	   File resource = new File(historicPath);
           BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(resource,resource.exists())); // Ouverture d'un flux d'ecriture binaire vers le fichier
           fileOut.write(date.toString().getBytes(), 0, date.toString().length() );
           fileOut.write(" : ".getBytes(), 0, " : ".length() );
           fileOut.write(message.getBytes(), 0, message.getBytes().length);
           fileOut.write("\r\n".getBytes(), 0, "\r\n".getBytes().length);
           fileOut.flush(); // écriture des données
           fileOut.close();  // fermeture flux ecriture
           
    	   }catch(Exception e){
               e.printStackTrace();
    	   }
       }
       
      /**
  	  * Résumé du rôle de la méthode.
  	  * Commentaires détaillés sur le role de la methode
  	  * @param val la valeur a traiter
  	  * @return la valeur calculée
  	  */
       synchronized public void removeClient(String pseudo) {
    	   for(int i = 0 ; i < clientsThread.size() ; ++i) {
       		if(pseudo.equals(clientsThread.get(i).getPseudo())) {
       			clientsThread.remove(i);

       		}
       	}
    	  
       }
       
      /**
  	  * Résumé du rôle de la méthode.
  	  * Commentaires détaillés sur le role de la methode
  	  * @param val la valeur a traiter
  	  * @return la valeur calculée
  	  */
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

  
