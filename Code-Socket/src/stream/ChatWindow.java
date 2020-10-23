package stream;
import javax.swing.text.*;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;


/**
* @author 
*/
public class ChatWindow implements ActionListener {

	protected JFrame frmChat;
	protected JTextField textFieldPseudo;
	protected JTextField textFieldServAddr;
	protected JTextField textFieldServPort;
	protected JButton btnNewButtonConnect;
	protected JButton btnNewButtonDisconnect;
	protected JButton btnNewButtonSend;
	protected JTextPane textAreaChatIn;
	protected StyledDocument doc;
	protected SimpleAttributeSet left;
	protected SimpleAttributeSet right;
	protected JTextField textFieldChatOut;
	protected Socket echoSocket;
	protected ServerThreadRecieve sr;
	protected ServerThreadSend ss;
	

	/**
	 * Create the application.
	 */
	public ChatWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Socket echoSocket = null;
		ServerThreadRecieve sr = null;
		ServerThreadSend ss = null;
		frmChat = new JFrame();
		frmChat.setResizable(false);
		frmChat.setTitle("Chat TCP\r\n");
		frmChat.setSize(561,754);
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 525, 95);
		panel.setLayout(null);
		
		textFieldPseudo = new JTextField();
		textFieldPseudo.setColumns(10);
		textFieldPseudo.setBounds(77, 11, 86, 20);
		panel.add(textFieldPseudo);
		
		JLabel lblNewLabel = new JLabel("Pseudo");
		lblNewLabel.setBounds(21, 14, 46, 14);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Server Address");
		lblNewLabel_1.setBounds(173, 14, 80, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Server Port");
		lblNewLabel_2.setBounds(361, 14, 66, 14);
		panel.add(lblNewLabel_2);
		
		textFieldServPort = new JTextField();
		textFieldServPort.setColumns(10);
		textFieldServPort.setBounds(441, 11, 86, 20);
		panel.add(textFieldServPort);
		
		textFieldServAddr = new JTextField();
		textFieldServAddr.setBounds(265, 11, 86, 20);
		panel.add(textFieldServAddr);
		textFieldServAddr.setColumns(10);
		
		btnNewButtonConnect = new JButton("Connect");
		btnNewButtonConnect.addActionListener(this);
		btnNewButtonConnect.setBounds(289, 61, 89, 23);
		panel.add(btnNewButtonConnect);
		
		btnNewButtonDisconnect = new JButton("Disconnect");
		btnNewButtonDisconnect.setEnabled(false);
		btnNewButtonDisconnect.addActionListener(this);
		btnNewButtonDisconnect.setBounds(403, 61, 89, 23);
		panel.add(btnNewButtonDisconnect);
		
		
		textAreaChatIn = new JTextPane();
		textAreaChatIn.setBounds(10, 128, 535, 383);
		
		
		doc = textAreaChatIn.getStyledDocument();
        left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.BLACK);
        StyleConstants.setBackground(left, Color.GRAY);
        right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.WHITE);
        StyleConstants.setBackground(right, Color.BLUE);
        JScrollPane scroll = new JScrollPane(textAreaChatIn);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(10, 128, 535, 383);
		panel.add(scroll);
		
		textFieldChatOut = new JTextField();
		textFieldChatOut.setBackground(new Color(255, 255, 255));
		textFieldChatOut.setEnabled(false);
		textFieldChatOut.setBounds(21, 537, 506, 118);
		panel.add(textFieldChatOut);
		textFieldChatOut.setColumns(10);
		
		btnNewButtonSend = new JButton("Send");
		btnNewButtonSend.addActionListener(this);
		btnNewButtonSend.setEnabled(false);
		btnNewButtonSend.setBounds(403, 675, 89, 23);
		panel.add(btnNewButtonSend);
		
		frmChat.getContentPane().add(panel);
		frmChat.setVisible(true);
		
		
		
		
	}
	/**
	  * Résumé du rôle de la méthode.
	  * Commentaires détaillés sur le role de la methode
	  * @param val la valeur a traiter
	  * @return la valeur calculée
	  */
	
		public void actionPerformed(ActionEvent e){
			
			String message;
			
			if(e.getSource()==btnNewButtonConnect) {
				String serverAddr = textFieldServAddr.getText();
		        String pseudo = textFieldPseudo.getText();
		        String serverPort = textFieldServPort.getText();
		        
	
		       try { 
		      	    echoSocket = new Socket(serverAddr,new Integer(serverPort).intValue());
		      	    
		       
			 } catch (UnknownHostException t) {
		            System.err.println("Don't know about host:" + serverAddr);
		            System.exit(1);
		        } catch (IOException t) {
		            System.err.println("Couldn't get I/O for "
		                               + "the connection to:"+ serverAddr);
		            System.exit(1);
		        }
		        
		        System.out.println("Connexion to server "+serverAddr+" with port "+serverPort);
		        try {
					this.doc.insertString(this.doc.getLength(),"Connexion to server "+serverAddr+" with port "+serverPort+"\r\n",left);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        this.doc.setParagraphAttributes(this.doc.getLength(), 1, this.left, false);
		        
		        sr = new ServerThreadRecieve(echoSocket,this);
		        sr.start();
		        ss = new ServerThreadSend(echoSocket,this,pseudo);
		        ss.start();
		        btnNewButtonDisconnect.setEnabled(true);
		        btnNewButtonConnect.setEnabled(false);
		        btnNewButtonSend.setEnabled(true);
		        textFieldChatOut.setEnabled(true);
			}
			else if(e.getSource()==btnNewButtonDisconnect) {
				
				ss.disconnect();
				sr.disconnect();
				textAreaChatIn.setText("");
				try {
					this.doc.insertString(this.doc.getLength(),"You have been disconnected ! \r\n",left);
				} catch (BadLocationException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
		        this.doc.setParagraphAttributes(this.doc.getLength(), 1, this.left, false);
		        
				btnNewButtonDisconnect.setEnabled(false);
				btnNewButtonConnect.setEnabled(true);
				btnNewButtonSend.setEnabled(false);
				textFieldChatOut.setEnabled(false);
				try {
					ss.stop();
					sr.stop();
					echoSocket.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					echoSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}else if(e.getSource()==btnNewButtonSend) {
				message = textFieldChatOut.getText();
				ss.sendMessage(message);
				textFieldChatOut.setText("");
				
			}
		}
}
