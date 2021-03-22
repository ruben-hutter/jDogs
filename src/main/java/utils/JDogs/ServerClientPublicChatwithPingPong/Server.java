package utils.JDogs.ServerClientPublicChatwithPingPong;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    ServerSocket serverSocket;
    ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    boolean shouldRun = true;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {

        try {
            serverSocket = new ServerSocket(8090);

            while (shouldRun) {

                Socket socket = serverSocket.accept();
                ServerConnection sc = new ServerConnection(socket, this);
                Thread scThread = new Thread(sc);
                scThread.start();
                connections.add(sc);
                System.out.println("new Server:  " + socket.getInetAddress().getHostName());
                System.out.println("connection size:  " + connections.size());

            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("error in Server-Thread");
        }
    }
}
