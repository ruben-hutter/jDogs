package JDogs.ServerClientEnvironment;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {

    ServerSocket serverSocket;
    ArrayList<SendFromServer> connections = new ArrayList<>();
    ArrayList <String> allNickNames = new ArrayList<>();
    Map<String, User> UserPasswordMap = new HashMap<>();


    boolean running = true;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {

        try {
            //port 8090
            serverSocket = new ServerSocket(8090);
            System.out.println("server started...");

            //runs as long as the server is activated
            while(running) {

                Socket socket = serverSocket.accept();
                ServerConnection sc = new ServerConnection(socket, this);
                //new threads to maintain connection to the individual clients

                Thread scThread = new Thread(sc);
                scThread.start();

                System.out.println("new client:  " + socket.getInetAddress().getHostName());
                System.out.println("connections size:  " + connections.size());

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("server error");
        }
    }

    //returns if a nickname is already taken or not
    public boolean isValidNickName(String newNick) {
        for (String allNickName : allNickNames) {
            if (allNickName.equals(newNick)) {
                return false;
            }
        }
        return true;
    }


    public void kill() {
        System.exit(-1);
    }


}
