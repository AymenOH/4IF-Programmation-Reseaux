package stream;

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
public class ChatWindow {

	protected JFrame frmChat;
	protected JTextField textFieldPseudo;
	protected JTextField textFieldServAddr;
	protected JTextField textFieldServPort;
	protected JTextField textFieldChatOut;
	protected JButton btnNewButtonConnect;
	protected JButton btnNewButtonDisconnect;
	protected TextArea textAreaChatIn;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow();
					window.frmChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		frmChat = new JFrame();
		frmChat.setResizable(false);
		frmChat.setTitle("chat\r\n");
		frmChat.setSize(561,754);
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChat.setVisible(true);

		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 525, 95);
		frmChat.getContentPane().add(panel);
		panel.setLayout(null);
		
		textFieldPseudo = new JTextField();
		textFieldPseudo.setColumns(10);
		textFieldPseudo.setBounds(429, 11, 86, 20);
		panel.add(textFieldPseudo);
		
		JLabel lblNewLabel = new JLabel("Pseudo");
		lblNewLabel.setBounds(21, 11, 46, 14);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Server Address");
		lblNewLabel_1.setBounds(175, 14, 80, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Server Port");
		lblNewLabel_2.setBounds(361, 14, 66, 14);
		panel.add(lblNewLabel_2);
		
		textFieldServPort = new JTextField();
		textFieldServPort.setColumns(10);
		textFieldServPort.setBounds(260, 11, 86, 20);
		panel.add(textFieldServPort);
		
		textFieldServAddr = new JTextField();
		textFieldServAddr.setBounds(72, 11, 86, 20);
		panel.add(textFieldServAddr);
		textFieldServAddr.setColumns(10);
		
		btnNewButtonConnect = new JButton("Connect");
		btnNewButtonConnect.addActionListener((ActionListener) this);
		btnNewButtonConnect.setBounds(289, 61, 89, 23);
		panel.add(btnNewButtonConnect);
		
		btnNewButtonDisconnect = new JButton("Disconnect");
		btnNewButtonDisconnect.addActionListener((ActionListener) this);
		btnNewButtonDisconnect.setBounds(403, 61, 89, 23);
		panel.add(btnNewButtonDisconnect);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 117, 525, 403);
		frmChat.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		textAreaChatIn = new TextArea();
		textAreaChatIn.setBounds(10, 10, 505, 383);
		panel_1.add(textAreaChatIn);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 529, 525, 163);
		frmChat.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		textFieldChatOut = new JTextField();
		textFieldChatOut.setBounds(10, 11, 505, 107);
		panel_2.add(textFieldChatOut);
		textFieldChatOut.setColumns(10);
		
		JButton btnNewButtonSend = new JButton("Send");
		btnNewButtonSend.setBounds(376, 129, 89, 23);
		panel_2.add(btnNewButtonSend);
		
		
		
	}
	
	
		public void actionPerformed(ActionEvent e){
			Socket echoSocket = null;
			
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
		        
		        ServerThreadRecieve sr = new ServerThreadRecieve(echoSocket,this);
		        sr.start();
		        ServerThreadSend ss = new ServerThreadSend(echoSocket,sr,this,pseudo);
		        ss.start();
			}
		}
	
}
