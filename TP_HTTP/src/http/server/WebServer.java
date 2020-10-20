///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */




public class WebServer {
	
	/* Chemin absolu du repertoire des fichiers ressources utilisés par le server (fichiers statique de tout format (texte, html, média...)) */
	protected static final String resourceDirectory = "C:/Users/Aymen/git/4IF-Programmation-Reseaux/TP_HTTP/web/";
	/* Chemin absolu de la page d'acceuil/index du serveur */
	protected static final String index = "C:/Users/Aymen/git/4IF-Programmation-Reseaux/TP HTTP/web/index.html";
	/* Chemin absolu de la page web envoyee en cas d'erreur 404 */
    protected static final String fileNotFound = "C:/Users/Aymen/git/4IF-Programmation-Reseaux/TP HTTP/web/notfound.html";

  /**
   * WebServer constructor.
   */
	 /** WebServer constructor. */
    protected void start() {
        ServerSocket s;

        System.out.println("Webserver starting up on port 3000");
        System.out.println("(press ctrl-c to exit)");
        try {
            // create the main server socket
            s = new ServerSocket(3000);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return;
        }

        /** Le serveur écoute sur son port, en attente de connexion... */
        System.out.println("Waiting for connection");
        Socket remote = null;
        BufferedOutputStream out = null;
        for (; ; ) {
            try {
                // wait for a connection
                remote = s.accept();
                // remote is now the connected socket
                System.out.println("Connection, sending data.");
                System.out.println("Remote : " + remote);

                // Ouverture d'un flux d'écriture pour permettre ensuite d'envoyer en bytes du contenu au client
                out = new BufferedOutputStream(remote.getOutputStream());
                // Ouverture d'un flux de lecture pour lire en string/ligne par ligne la requête envoyée par le client
                BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));

                /** Lecture des données envoyées par le client
                 * We basically ignore it,
                 * stop reading once a blank line is hit. This
                 * blank line signals the end of the client HTTP
                 * headers.
                 */

                String line = in.readLine();
                String header = "";

                /** Parcourt du header jusqu'à sa fin
                 On considère ici que le format du header est respecté, s'il se finit par une ligne vide / pas d'autre verif
                 Sinon on aurait pu générer une erreur 400 bad request si le format n'était pas respecté */
                header = line;
                while (!line.equals("") && line != null) {
                    header += line;
                    line = in.readLine();
                    System.out.println(line);
                }

                /** On traite le requête dans une méthode séparée */
                //handleRequest(header, out, in);

                remote.close();

            } catch (Exception e) { // Erreur lors de la connexion du client
                System.out.println(e);
                try { // essai de prévenir le client, pas sûr que le message arrive à destination
                    //out.write(sendHeader("500 Internal Server Error").getBytes());
                    out.flush();
                    remote.close();
                } catch (Exception e2) {
                }
                ;
            }
        }
    }
    
    /** Traitement de la requête du client
     * Le serveur implémente ici les requêtes de type GET, PUT, POST, HEAD et DELETE
     * @param header en-tete de la requete
     * @param outBytes flux d'écriture en byte sur le socket Client
     * @param in flux de lecture des données envoyées par le client, ce buffer a déjà lu l'en-tete
     */
    public void handleRequest(String header, BufferedOutputStream outBytes, BufferedReader in) {
        String[] params = header.split(" ");
        String method, path;

        method = params[0]; // GET, POST, PUT, HEAD, ou DELETE
        path = params[1].substring(1); // target resource, on enlève le / qui précède le nome de la ressource
        System.out.println(method + " " + path + ".");

        /** On traite différement la requête suivant son type */
        switch (method) {
            case "GET":
                getRequest(path, outBytes);
                break;
            case "HEAD":
                //headRequest(outBytes, path);
                break;
            case "POST":
                //postRequest(path, outBytes, in);
                break;
            case "PUT":
                //putRequest(path, outBytes, in);
                break;
            case "DELETE":
                //deleteRequest(path, outBytes);
                break;
            default: // cas d'une requete non implémentée sur notre serveur (par exemple VIEW)
                try {
                    //outBytes.write(sendHeader("501 Not Implemented").getBytes());
                    outBytes.flush();
                } catch (Exception e) {
                    System.out.println(e);
                }
        }
    }
    
    public void sendFile(BufferedOutputStream out, File resource) {
        try {
            // Ouverture d'un flux de lecture binaire sur le fichier demande
            BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(resource));
            // Envoi du corps : le fichier (page HTML, image, video...)
            byte[] buffer = new byte[256];
            int nbRead = fileIn.read(buffer);
            while (nbRead!= -1) {
                out.write(buffer, 0, nbRead);
                nbRead = fileIn.read(buffer);
            }
            fileIn.close(); // Fermeture du flux de lecture
            out.flush(); //Envoi des donnees
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Implémentation du traitement d'une requete GET - cette méthode retourne une page WEB identifiée par son URL
     * Tente d'ouvrir et de lire la ressource demandee et de l'envoyer au client, sous forme de bytes.
     * /!\ on aurait pu envoyer sous forme de string les fichiers txt ou html... mais ici la méthode est générale et peut aussi être amené à envoyer des medias
     * On renvoie le code 200 OK si le fichier a ete trouve et 404 Not Found sinon.
     * Le corps de la reponse est le contenu du fichier, transmis en bytes, ou bien le contenu de la page fileNotFound du serveur
     *
     * @param outBytes  Flux d'ecriture binaire vers le socket client auquel il faut envoyer une reponse.
     * @param path Chemin du fichier que le client veut consulter.
     */
    public void getRequest(String path, BufferedOutputStream outBytes) {
        try {
            if (path.equals("")) {
                path = index; // si rien n'est demandé on renvoie le fichier index
            } else {
                path = resourceDirectory + path; // si une resource est demandée on la recherche dans le répertoire de resssources du serveur
            }
            // un fichier est demandé
            File resource = new File(path);
            if (resource.exists() && resource.isFile()) { // Si la ressource demandée existe
                outBytes.write(sendHeader("200 OK", path, resource.length()).getBytes());
                /** Envoie du contenu du fichier */
                sendFile(outBytes, resource);
            } else { // la ressource n'existe pas, on envoie l'erreur 404
                resource = new File(fileNotFound);
                outBytes.write(sendHeader("404 Not Found", fileNotFound, resource.length()).getBytes());
                sendFile(outBytes, resource);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    protected String sendHeader(String status) {
        String header = "HTTP/1.0 " + status + "\r\n";
        header += "Server: Bot\r\n";
        header += "\r\n";
        System.out.println(header);
        return header;
    }
    
    protected String sendHeader(String status, String filename, long length) {
        String header = "HTTP/1.0 " + status + "\r\n";
        if (filename.endsWith(".html") || filename.endsWith(".htm"))
            header += "Content-Type: text/html\r\n";
        else if (filename.endsWith(".mp4"))
            header += "Content-Type: video/mp4\r\n";
        else if (filename.endsWith(".png"))
            header += "Content-Type: image/png\r\n";
        else if (filename.endsWith(".jpeg") || filename.endsWith(".jpeg"))
            header += "Content-Type: image/jpg\r\n";
        else if (filename.endsWith(".mp3"))
            header += "Content-Type: audio/mp3\r\n";
        else if (filename.endsWith(".avi"))
            header += "Content-Type: video/x-msvideo\r\n";
        else if (filename.endsWith(".css"))
            header += "Content-Type: text/css\r\n";
        else if (filename.endsWith(".pdf"))
            header += "Content-Type: application/pdf\r\n";
        else if (filename.endsWith(".odt"))
            header += "Content-Type: application/vnd.oasis.opendocument.text\r\n";
        header += "Content-Length: " + length + "\r\n";
        header += "Server: Bot\r\n";
        header += "\r\n";
        System.out.println("ANSWER HEADER :");
        System.out.println(header);
        return header;
    }
  
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
