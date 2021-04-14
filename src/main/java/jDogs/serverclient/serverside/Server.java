package jDogs.serverclient.serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import org.checkerframework.checker.units.qual.A;

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
    //this list contains all sender objects, but I want to replace by the list of all serverConnection objects
    //TODO delete it
    ArrayList<SendFromServer> publicSenderList = new ArrayList<>();
    //this list contains all nicknames used at the moment(to avoid duplicates)
    ArrayList<String> allNickNames = new ArrayList<>();
    //this map contains the names and the corresponding serverConnections objects
    private Map<String, ServerConnection> serverConnectionMap = new HashMap<>();
    //this map contains the names and the corresponding sender objects, but I want to delete it
    // and get the sender objects from the server connection
    //TODO delete it
    private Map<String, SendFromServer> whisperList = new HashMap<>();
    //this list contains all ongoing games and all pendent games
    ArrayList<GameFile> allGamesNotFinished = new ArrayList<GameFile>();
    //this list contains all ongoing games
    ArrayList<MainGame> runningGames = new ArrayList<>();
    //this list contains all public lobby guest names
    ArrayList<String> publicLobbyGuests = new ArrayList<>();
    //this list contains all serverConnections of active players
    ArrayList<ServerConnection> serverConnections = new ArrayList<>();
    //this list contains all finished games
    ArrayList<GameFile> finishedGames = new ArrayList<>();
    //this list contains all server connections active in the public lobby
    ArrayList<ServerConnection> publicLobbyConnections = new ArrayList<>();

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
                System.out.println("publicSenderList size:  " + (publicSenderList.size() + 1));
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

    /**
     * this method is needed to send messages to clients which are connected by public lobbies or by a common game
     * @param nickname a new nickname of a client
     * @param serverConnection is the sC of this client
     */
    public void addNickname(String nickname, ServerConnection serverConnection) {
        //add to whisperlist
        whisperList.put(nickname, serverConnection.getSender());

        //scMap

        serverConnectionMap.put(nickname,serverConnection);
        System.out.println("added to serverConnection " + nickname + nickname.length());
        serverConnectionMap.get("maintenance");
        // add to lobbyGuests
        publicLobbyGuests.add(nickname);

        //add to nicknamelist
        allNickNames.add(nickname);
    }

    public void removeNickname(String nickname) {
        //remove nickname from whisperlist
        whisperList.remove(nickname);

        //remove nickname from sConnectionNicknameMap

        serverConnectionMap.remove(nickname);

        //remove nickname from lobbyGuests
        publicLobbyGuests.remove(nickname);

        //remove nickname from nicknamelist
        allNickNames.remove(nickname);
    }

    //returns sender object to send private message
    public SendFromServer getSender(String nickname) {
        return whisperList.get(nickname);
    }

    // add sender object from publicChatList
    public void addSender(SendFromServer connection) {

        publicSenderList.add(connection);
    }
    // remove sender object from publicChatList
    public void removeSender(SendFromServer connection) {
        publicSenderList.remove(connection);
    }


    public void startGame(MainGame mainGame) {
        runningGames.add(mainGame);
    }

    public ServerConnection getServerConnectionsMap(String nickname) {

        for (int i = 0; i < serverConnections.size(); i++) {
            if (serverConnections.get(i).getNickname() == nickname) {
                return serverConnections.get(i);
            }
        }
        return null;
    }

    /**
     *
     * @param gameFile
     * @return list of server connection objects of clients who participate in this opened game or started game
     */
    public ArrayList<ServerConnection> getServerConnectionsMap(GameFile gameFile) {

        ArrayList<ServerConnection> aList = new ArrayList<>();
        System.out.println(gameFile.getParticipants());
        String[] participantArray = gameFile.getParticipantsArray();
       for (int i = 0; i < gameFile.getNumberOfParticipants(); i++) {
           System.out.println(i);
           ServerConnection sc = serverConnectionMap.get(participantArray[i]);
           aList.add(sc);
           System.out.println(i);
        }
        return aList;
    }

    public GameFile getNotFinishedGame(String gameName) {
        for (int i = 0; i < allGamesNotFinished.size(); i++) {
            if (allGamesNotFinished.get(i).getNameId().equals(gameName)) {
                return allGamesNotFinished.get(i);
            }
        }
        return null;
    }

    public void removeGame(GameFile gameFile) {
        allGamesNotFinished.remove(gameFile);
        MainGame mainGame;
        if ((mainGame = getMainGame(gameFile)) != null) {
            runningGames.remove(mainGame);
        }
    }

    private MainGame getMainGame(GameFile gameFile) {
        for (MainGame runningGame1 : runningGames) {
            if (runningGame1.getGameId().equals(gameFile.getNameId())) {
                return runningGame1;
            }
        }
        return null;
    }

    public void sendMessageToAll(String message) {

    }

    public void sendMessageToPublicLobby(String message) {

    }
}
