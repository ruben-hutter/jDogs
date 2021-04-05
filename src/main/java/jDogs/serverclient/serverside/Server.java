package jDogs.serverclient.serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server waits for new clients trying to connect to server,
 * and if one connects the server creates a ServerConnection-thread
 * for this client.
 *
 * ArrayList with all Sender-objects lies here
 * ArrayList with all Nicknames lies here
 * Hashmap with String(userName) and User(Password, Nickname..) lies here
 */
public class Server {

    ServerSocket serverSocket;
    ArrayList<SendFromServer> connections = new ArrayList<>();
    ArrayList<String> allNickNames = new ArrayList<>();

    boolean running = true;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try {
            // port 8090
            serverSocket = new ServerSocket(8090);
            System.out.println("server started...");
            // runs as long as the server is activated
            while(running) {
                Socket socket = serverSocket.accept();
                new ServerConnection(socket, this).createConnection();

                /*
                // new threads to maintain connection to the individual clients
                Thread scThread = new Thread(sc);
                scThread.start();

                 */
                System.out.println("new client:  " + socket.getInetAddress().getHostName());
                System.out.println("connections size:  " + (connections.size() + 1));
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("server error");
        }
    }

    /**
     * Checks if a nickname is valid, if it doesn't already exist
     * @param newNick a given nickname String
     * @return true if it is a valid nickname
     */
    public boolean isValidNickName(String newNick) {
        for (String allNickName : allNickNames) {
            if (allNickName.equals(newNick)) {
                return false;
            }
        }
        return true;
    }

    /**
     * server shutdown
     */
    public void kill() {
        System.exit(-1);
    }
}
