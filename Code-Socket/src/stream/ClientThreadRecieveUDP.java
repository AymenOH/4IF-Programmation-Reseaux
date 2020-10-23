package stream;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.Socket;

public class ClientThreadRecieveUDP extends Thread {

    /**
     * Socket du client
     */
    public MulticastSocket socketLecture;
    /**
     * message entrant sur socIn
     */
    public String message;

    /**
     * Le constructeur de ThreadLecture prend en parametre un socket et une chaine de caracter qui initialise le nom et le le socket
     */
    ClientThreadRecieveUDP(MulticastSocket s) {
        this.socketLecture = s;
    }
    /**
     * Méthode appelée au démarrage du thread grâce à la méthose start(). Elle se charge de le lire les informations qui arrivent sur le socIn et les affiche ensuite.
     */
    public void run() {
        DatagramPacket recv;
        BufferedReader socIn;
        try {
            // Build a datagram packet for response

            // Receive a datagram packet response
            while(true){
                byte[] buf = new byte[1000];
                recv = new DatagramPacket(buf, buf.length);
                socketLecture.receive(recv);
                message= new String(recv.getData(),recv.getOffset(),recv.getLength());
                System.out.println(message);
            }

        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }

    }


}

