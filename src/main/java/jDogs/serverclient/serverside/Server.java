package jDogs.serverclient.serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private ServerSocket serverSocket;
    ArrayList<SendFromServer> senderlist = new ArrayList<>();
    ArrayList<String> allNickNames = new ArrayList<>();
    private Map<String, SendFromServer> whisperList = new HashMap<>();
    ArrayList<GameFile> allGamesNotFinished = new ArrayList<GameFile>();
    ArrayList<MainGame> runningGames = new ArrayList<>();
    ArrayList<ServerConnection> serverConnections = new ArrayList<>();


    private static Server instance;

    boolean running = true;

    public static void main(String[] args) {
        new Server();
    }

    // return Singleton
    public static Server getInstance() {
        return instance;
    }

    public Server() {
        try {
            instance = this;
            // port 8090
            serverSocket = new ServerSocket(8090);
            System.out.println("server started...");
            // runs as long as the server is activated
            while(running) {
                Socket socket = serverSocket.accept();
                ServerConnection sc = new ServerConnection(socket, this);
                sc.createConnection();
                serverConnections.add(sc);

                /*
                // new threads to maintain connection to the individual clients
                Thread scThread = new Thread(sc);
                scThread.start();

                 */
                System.out.println("new client:  " + socket.getInetAddress().getHostName());
                System.out.println("senderlist size:  " + (senderlist.size() + 1));
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

    public void addNickname(String nickname, SendFromServer connection) {
        //add to whisperlist
        whisperList.put(nickname, connection);

        //add to nicknamelist
        allNickNames.add(nickname);
    }

    public void removeNickname(String nickname) {
        //remove nickname from whisperlist
        whisperList.remove(nickname);

        //remove nickname from nicknamelist
        allNickNames.remove(nickname);
    }

    //returns sender object to send private message
    public SendFromServer getSender(String nickname) {
        return whisperList.get(nickname);
    }

    // add sender object from publicChatList
    public void addSender(SendFromServer connection) {
        senderlist.add(connection);
    }
    // remove sender object from publicChatList
    public void removeSender(SendFromServer connection) {
        senderlist.remove(connection);
    }


    public void startGame(MainGame mainGame) {
        runningGames.add(mainGame);
    }

    public ServerConnection getServerConnections(String nickname) {
        for (int i = 0; i < serverConnections.size(); i++) {
            if (serverConnections.get(i).getNickname() == nickname) {
                return serverConnections.get(i);
            }
        }
        return null;
    }
}
