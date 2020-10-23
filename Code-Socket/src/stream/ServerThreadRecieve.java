package stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.text.BadLocationException;


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
		}
		while (true) {
	    	
				//System.out.println(">>>>> "+socIn.readLine());
				String msg;
				try {
					msg = socIn.readLine();
					if(msg.contains("You said :")) {
							chat.doc.insertString(chat.doc.getLength(), "\n"+msg, chat.right );
				            chat.doc.setParagraphAttributes(chat.doc.getLength(), 1, chat.right, false);
					}else {
							chat.doc.insertString(chat.doc.getLength(), "\n"+msg, chat.left );
				            chat.doc.setParagraphAttributes(chat.doc.getLength(), 1, chat.left, false);
					}         

				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	}
	
	 public void disconnect() {
	    	try {
				socIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	
	    }
	
	
}
