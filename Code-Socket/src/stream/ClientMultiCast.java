/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;



public class ClientMultiCast {


    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException {

        MulticastSocket echoSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;

        InetAddress groupAdress=InetAddress.getByName("228.5.6.7");
        int port=new Integer(args[0]).intValue();

            // Create a multicast socket
             echoSocket = new MulticastSocket(port);
            ClientThreadRecieveUDP th = new ClientThreadRecieveUDP(echoSocket);
            th.start();
            // Join the group
            echoSocket.joinGroup(groupAdress);
            //    socOut= new PrintStream(echoSocket.getOutputStream());
             stdIn = new BufferedReader(new InputStreamReader(System.in));

        String line;
        while (true) {
            line=stdIn.readLine();
            if (line.equals(".")) break;
            if(!line.equals("bye")){
                // Build a datagram packet for a message
                // to send to the group
                DatagramPacket dataToSend = new DatagramPacket(line.getBytes(),
                        line.length(), groupAdress, port);
                // Send a multicast message to the group
                echoSocket.send(dataToSend);
            }else{
                break;
            }
        }
        socOut.close();
        socIn.close();
        stdIn.close();
        echoSocket.close();
    }
}


