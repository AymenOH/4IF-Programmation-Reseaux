///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * WebServer HTTP
 */
public class WebServer {
	
	/* Chemin absolu du repertoire des fichiers ressources utilisés par le server (fichiers statique de tout format (texte, html, média...)) */
	protected static final String resourceDirectory = "C:/Users/Aymen/git/4IF-Programmation-Reseaux/TP_HTTP/web/";
	/* Chemin absolu de la page d'acceuil/index du serveur */
	protected static final String index = "C:/Users/Aymen/git/4IF-Programmation-Reseaux/TP_HTTP/web/index.html";
	/* Chemin absolu de la page web envoyee en cas d'erreur 404 */
    protected static final String fileNotFound = "C:/Users/Aymen/git/4IF-Programmation-Reseaux/TP_HTTP/web/notfound.html";

    /**
	  * Demarre le web serveur sur le port 3000 
	  * et place le serveur en attente de connexion et de requete
	  * @return void
	  */
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

               

                String line = in.readLine();
                String header = "";

                header = line;
                while (!line.equals("") && line != null) {
                    header += line;
                    line = in.readLine();
                    System.out.println(line);
                }

              
                handleRequest(header, out, in);

                remote.close();

            } catch (Exception e) { // Erreur lors de la connexion du client
                System.out.println(e);
                try { // essai de prévenir le client, pas sûr que le message arrive à destination
                    out.write(sendHeader("500 Internal Server Error").getBytes());
                    out.flush();
                    remote.close();
                } catch (Exception e2) {
                }
                ;
            }
        }
    }
    /**
	  * Les parametres de la requete du client sont separes dans un tableau.
	  * Le type de la requete est isole dans la premiere case du tableau.
	  * Selon le type de la requete, on appelle à travers un switch la methode 
	  * correspondante pour traiter la requete.
	  * @param header String qui contient la requete du client
	  * @param out flux de sortie du web server
	  * @param in flux d'entree du web server  
	  * @return void
	  */
    public void handleRequest(String header, BufferedOutputStream out, BufferedReader in) {
        String[] params = header.split(" ");
        String method, path;

        method = params[0]; // GET, POST, PUT, HEAD, ou DELETE
        path = params[1].substring(1); // target resource, on enlève le / qui précède le nom de la ressource
        System.out.println(method + " " + path + ".");

        /** On traite différement la requête suivant son type */
        switch (method) {
            case "GET":
                getRequest(path, out);
                break;
            case "HEAD":
                headRequest(path, out);
                break;
            case "POST":
                postRequest(path, out, in);
                break;
            case "PUT":
                putRequest(path, out, in);
                break;
            case "DELETE":
                deleteRequest(path, out);
                break;
            default: // cas d'une requete non implémentée sur notre serveur (par exemple VIEW)
                try {
                    out.write(sendHeader("501 Not Implemented").getBytes());
                    out.flush();
                } catch (Exception e) {
                    System.out.println(e);
                }
        }
    }
    /**
	  * Ecriture du contenu du fichier ressource dans 
	  * le flux de sortie du web server.
	  * @param out flux de sortie du web server
	  * @param ressource fichier a envoyer au client
	  * @return void
	  */
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
	  * Trouve le fichier ressource que l'on souhaite consulter avec le parametre
	  * path et ecrit ce fichier dans le flux de sortie du web server en appelant
	  * {@link #sendFile(BufferedOutputStream, File)}
	  * @param path String du chemin de la ressource que l'on souhaite consulter
	  * @param out flux de sortie du web server
	  * @return void
	  */
    public void getRequest(String path, BufferedOutputStream out) {
        try {
            if (path.equals("")) {
                path = index; // si rien n'est demandé on renvoie le fichier index
            } else {
                path = resourceDirectory + path; // si une resource est demandée on la recherche dans le répertoire de resssources du serveur
            }
            // un fichier est demandé
            File resource = new File(path);
            if (resource.exists() && resource.isFile()) { // Si la ressource demandée existe
                out.write(sendHeader("200 OK", path, resource.length()).getBytes());
                /** Envoie du contenu du fichier */
                sendFile(out, resource);
            } else { // la ressource n'existe pas, on envoie l'erreur 404
                resource = new File(fileNotFound);
                out.write(sendHeader("404 Not Found", fileNotFound, resource.length()).getBytes());
                sendFile(out, resource);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
	  * Ajoute du contenu au fichier localise par le parametre path sans supprimer 
	  * le contenu deja existant.
	  *@param path String du chemin ou on veut stocker la ressource
	  *@param out flux de sortie du web server
	  *@param in flux d'entree du web server
	  * @return void
	  */
    public void postRequest(String path,BufferedOutputStream out, BufferedReader in) {
        // Similaire à put sauf qu'on n'écrase pas le contenu du fichier
        try {
            File resource = new File(resourceDirectory + path);
            boolean newFile = resource.createNewFile(); // newFile vaut vrai si le fichier est créé
            System.out.println("new file vaut : "+newFile);

            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(resource,resource.exists())); // Ouverture d'un flux d'ecriture binaire vers le fichier
            /** Parcourt des informations recues dans le body de la requete POST dans le fichier destination */
            String lineBody = in.readLine();
            while (lineBody!=null && !lineBody.equals("")) {
                System.out.println("line :"+lineBody);
                lineBody = in.readLine();
                fileOut.write(lineBody.getBytes(), 0, lineBody.getBytes().length);
                fileOut.write("\r\n".getBytes(), 0, "\r\n".getBytes().length);
            }
            fileOut.flush(); // écriture des données
            fileOut.close();  // fermeture flux ecriture

            if (newFile) {
                out.write(sendHeader("201 Created").getBytes()); // si le fichier est nouveau
                out.write("\r\n".getBytes());
            } else {
                out.write(sendHeader("200 OK").getBytes()); // si le fichier existait déjà
                out.write("\r\n".getBytes());
            }
            out.flush();
        } catch (Exception e){
            e.printStackTrace();
            try {
                out.write(sendHeader("500 Internal Server Error").getBytes());
                out.write("\r\n".getBytes());
                out.flush();
            } catch (Exception e2) {
                System.out.println(e);
            }

        }
    }
    
    /**
	  * Ajoute du contenu au fichier localise par le parametre path en supprimant 
	  * le contenu deja existant.
	  *@param path String du chemin ou l'on veut stocker la ressource
	  *@param out flux de sortie du web server
	  *@param in flux d'entree du web server
	  * @return void
	  */
    
    public void putRequest(String path,BufferedOutputStream out, BufferedReader in) {
      
        try {
            File resource = new File(resourceDirectory + path);
            boolean newFile = resource.createNewFile(); // newFile vaut vrai si le fichier est créé
            System.out.println("new file vaut : "+newFile);
            if(!newFile) { 
                PrintWriter pw = new PrintWriter(resource);
                pw.close();
            }
            

            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(resource,resource.exists())); // Ouverture d'un flux d'ecriture binaire vers le fichier
            /** Parcourt des informations recues dans le body de la requete PUT dans le fichier destination */
            String lineBody = in.readLine();
            while (lineBody!=null && !lineBody.equals("")) {
                System.out.println("line :"+lineBody);
                lineBody = in.readLine();
                fileOut.write(lineBody.getBytes(), 0, lineBody.getBytes().length);
                fileOut.write("\r\n".getBytes(), 0, "\r\n".getBytes().length);
            }
            fileOut.flush(); // écriture des données
            fileOut.close();  // fermeture flux ecriture

            if (newFile) {
                out.write(sendHeader("201 Created").getBytes()); // si le fichier est nouveau
                out.write("\r\n".getBytes());
            } else {
                out.write(sendHeader("200 OK").getBytes()); // si le fichier existait déjà
                out.write("\r\n".getBytes());
            }
            out.flush();
        } catch (Exception e){
            e.printStackTrace();
            try {
                out.write(sendHeader("500 Internal Server Error").getBytes());
                out.write("\r\n".getBytes());
                out.flush();
            } catch (Exception e2) {
                System.out.println(e);
            }

        }
    }
    
    /**
	  * Supprime le contenu du fichier localise par le parametre path.
	  *@param path String du chemin  du fichier a supprimer
	  *@param out flux de sortie du web server
	  * @return void
	  */
    
    public void deleteRequest(String path, BufferedOutputStream out) {
        try {
            File resource = new File(resourceDirectory+path);
            // Suppression du fichier
            boolean deleted = false;
            boolean existed = false;
            if((existed = resource.exists()) && resource.isFile()) {
                deleted = resource.delete();
            }

            // Envoi du Header
            if(deleted) {
                out.write(sendHeader("204 No Content").getBytes());
                out.write("\r\n".getBytes());
            } else if (!existed) {
                out.write(sendHeader("404 Not Found").getBytes());
                out.write("\r\n".getBytes());
            } else {
                // Le fichier a ete trouve mais n'a pas pu etre supprime
                out.write(sendHeader("403 Forbidden").getBytes());
                out.write("\r\n".getBytes());
            }
            // Envoi des donnees
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            // En cas d'erreur on essaie d'avertir le client
            try {
                out.write(sendHeader("500 Internal Server Error").getBytes());
                out.write("\r\n".getBytes());
                out.flush();
            } catch (Exception e2) {};
        }
    }

    /**
	  * Envoie le header (status ,type du fichier,taille)   du fichier localise par le parametre path 
	  *@param path String du chemin ou on veut stocker la ressource
	  *@param out flux de sortie du web server
	  * @return void
	  */
    public void headRequest(String path, BufferedOutputStream out) {
        try {
            
            path = resourceDirectory + path; // si une resource est demandée on la recherche dans le répertoire de resssources du serveur
            // un fichier est demandé
            File resource = new File(path);
            if (resource.exists() && resource.isFile()) { // Si la ressource demandée existe
                out.write(sendHeader("200 OK", path, resource.length()).getBytes());
                out.write("\r\n".getBytes());
            } else { // la ressource n'existe pas, on envoie l'erreur 404
            	out.write(sendHeader("404 Not Found").getBytes());
            	out.write("\r\n".getBytes());
            }
            out.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
	  * Construit le header d'une ressource  a partir d'un status passe en parametre
	  * @param status String du status
	  * @return header construit avec le status
	  */
    protected String sendHeader(String status) {
        String header = "HTTP/1.0 " + status + "\r\n";
        header += "Server: Bot\r\n";
        header += "\r\n";
        System.out.println(header);
        return header;
    }
    
    /**
	  *  Construit le header d'une ressource  a partir 
	  *  de son status, de son nom
	  * et  de sa taille  passes en parametre
	  * @param val la valeur a traiter
	  * @return header construit avec le status, le nom du fichier
	  * et sa taille
	  */
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
    
    /**
	  * Résumé du rôle de la méthode.
	  * Commentaires détaillés sur le role de la methode
	  * @param val la valeur a traiter
	  * @return la valeur calculée
	  */  
  
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
