package jDogs.serverclient.serverside;

import jDogs.player.Player;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

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
    //this list contains all nicknames used at the moment(to avoid duplicates)
    ArrayList<String> allNickNames = new ArrayList<>();
    //this map contains the names and the corresponding serverConnections objects

    private Map<String, ServerConnection> serverConnectionMap = new HashMap<>();

    //this list contains all ongoing games
    ArrayList<MainGame> runningGames = new ArrayList<>();
    //this list contains all public lobby guest names
    ArrayList<String> publicLobbyGuests = new ArrayList<>();
    //this list contains all finished games
    ArrayList<OpenGameFile> finishedGames = new ArrayList<>();
    //this list contains all server connections active in the public lobby
    ArrayList<ServerConnection> publicLobbyConnections = new ArrayList<>();
    //this list exists only to store all serverConnections to enable more ServerConnections
    ArrayList<ServerConnection> basicConnectionList = new ArrayList<>();

    private static Server instance;

    boolean running = true;

    // contains all names of openGameFiles and running games
    private ArrayList<String> allGamesNotFinishedNames = new ArrayList<>();

    // contains all openGameFiles
    private ArrayList<OpenGameFile> allOpenGames = new ArrayList<>();



    public static void main(String[] args) {
        new Server(args);
    }

    // return Singleton
    public static Server getInstance() {
        return instance;
    }

    public Server(String[] args) {
        try {
            instance = this;
            serverSocket = new ServerSocket(Integer.parseInt(args[1]));
            // runs as long as the server is activated
            while(running) {
                Socket socket = serverSocket.accept();
                ServerConnection sc = new ServerConnection(socket, this);
                sc.createConnection();
                basicConnectionList.add(sc);

                System.out.println("new client:  " + socket.getInetAddress().getHostName());
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
     * this methlod is needed to send messages to clients which are connected by public lobbies or by a common game
     * @param nickname a new nickname of a client
     * @param serverConnection is the sC of this client
     */
    public void addNickname(String nickname, ServerConnection serverConnection) {

        //scMap
        serverConnectionMap.put(nickname,serverConnection);
        // add to lobbyGuests
        publicLobbyGuests.add(nickname);

        //add to nicknamelist
        allNickNames.add(nickname);
    }

    public void removeNickname(String nickname) {

        //remove nickname from sConnectionNicknameMap

        serverConnectionMap.remove(nickname);

        //remove nickname from lobbyGuests
        publicLobbyGuests.remove(nickname);

        //remove nickname from nicknamelist
        allNickNames.remove(nickname);
    }

    //returns sender object to send private message
    public SendFromServer getSender(String nickname) {
        return serverConnectionMap.get(nickname).getSender();
    }

    // add sender object from publicChatList

    // remove sender object from publicChatList



    public void startGame(OpenGameFile openGameFile) {
        MainGame mainGame = new MainGame(openGameFile);
        runningGames.add(mainGame);
    }

    public ServerConnection getServerConnection(String nickname) {

        for (int i = 0; i < basicConnectionList.size(); i++) {
            if (basicConnectionList.get(i).getNickname().equals(nickname)) {
                return basicConnectionList.get(i);
            }
        }
        return null;
    }

    /**
     *
     * @param openGameFile
     * @return list of server connection objects of clients who participate in this opened game or started game
     */
    public ArrayList<ServerConnection> getServerConnectionsArray(OpenGameFile openGameFile) {

        ArrayList<ServerConnection> aList = new ArrayList<>();
        System.out.println(openGameFile.getParticipants());
        String[] participantArray = openGameFile.getParticipantsArray();
       for (int i = 0; i < openGameFile.getNumberOfParticipants(); i++) {
           System.out.println(i);
           ServerConnection sc = serverConnectionMap.get(participantArray[i]);
           aList.add(sc);
           System.out.println(i);
        }
        return aList;
    }


    public synchronized void removeGameFromSC(OpenGameFile openGameFile, String nickname) {
        System.out.println("removeGameFromSC method on server entered");

        openGameFile.removeFromParticipantServer(nickname);
        if (openGameFile.getHost() == null) {
            if (openGameFile.isPendent()) {
                sendMessageToAll("DOGA " + openGameFile.getSendReady());
                for (Player player: openGameFile.getPlayers()) {
                    if (player.getPlayerName() != nickname) {
                        player.getServerConnection().getMessageHandlerServer().returnToLobby();
                    }
                }
            } else {
                for (Player player: openGameFile.getPlayers()) {
                    if (player.getPlayerName() != nickname) {
                        player.getServerConnection().getMessageHandlerServer().returnToLobby();
                        player.sendMessageToClient("INFO host " + nickname + " quit game..shutdown game");
                    }
                }
                runningGames.remove(openGameFile);
            }
            //allGamesNotFinished.remove(openGameFile);
        } else {
            System.out.println("openGame host " + openGameFile.getHost());
            if (openGameFile.isPendent()) {
                sendMessageToAll("OGAM " + openGameFile.getSendReady());
            } else {
                for (Player player: openGameFile.getPlayers()) {
                    if (player.getPlayerName() != nickname) {
                        player.getServerConnection().getMessageHandlerServer().returnToLobby();
                        player.sendMessageToClient("INFO " + nickname + " quit game..shutdown game");
                    }
                }
            }
        }
    }
    //TODO remove all serverConnection objects from all methods
    public void setPlayingState(String nickname, MainGame mainGame) {
        //serverConnectionMap.get(nickname).getMessageHandlerServer().setPlaying(mainGame);
    }

    public void removeGame(OpenGameFile openGameFile) {
        System.out.println("remove game method on server entered");
        if (openGameFile.isPendent()) {
            sendMessageToAll("DOGA " + openGameFile.getSendReady());
        } else {
            //Server.getInstance().finishedGames.add(gameFile);
            for (Player player : openGameFile.getPlayers()) {
                player.getServerConnection().getMessageHandlerServer().returnToLobby();
            }
            System.out.println("INFO game finished");
        }

        System.out.println("got removed");
        MainGame mainGame;
        if ((mainGame = getMainGame(openGameFile)) != null) {
            runningGames.remove(mainGame);
        }
    }

    private MainGame getMainGame(OpenGameFile openGameFile) {
        for (MainGame runningGame1 : runningGames) {
            if (runningGame1.getGameId().equals(openGameFile.getNameId())) {
                return runningGame1;
            }
        }
        return null;
    }

    /**
     * sends message to clients wherever they are
     * @param message
     */
    public void sendMessageToAll(String message) {
        for (ServerConnection activeServerConnection1 : basicConnectionList) {
            activeServerConnection1.getSender().sendStringToClient(message);
        }
    }

    /**
     *
     * @param message to clients in public lobby explicitly
     */
    public void sendMessageToPublicLobby(String message) {
        for (ServerConnection publicLobbyConnection1 : publicLobbyConnections) {
            publicLobbyConnection1.getSender().sendStringToClient(message);
        }

    }

    public void addToLobby(ServerConnection serverConnection) {
        publicLobbyConnections.add(serverConnection);
    }

    public void removeFromLobby(ServerConnection serverConnection) {
        publicLobbyConnections.remove(serverConnection);
    }

    public void removeServerConnection(ServerConnection serverConnection) {
        allNickNames.remove(serverConnection.getNickname());
        publicLobbyConnections.remove(serverConnection);
        basicConnectionList.remove(serverConnection);
        serverConnectionMap.remove(serverConnection);
    }

    public ArrayList<ServerConnection> getBasicConnections() {
        return basicConnectionList;
    }

    public ArrayList<ServerConnection> getPublicLobbyConnections() {
        return publicLobbyConnections;
    }

    public Map<String, ServerConnection> getServerConnectionMap() {
        return serverConnectionMap;
    }

    /**
     * add an "openGameFile" to openGames
     * and add name to list of games
     * that did not finish(prevent name duplicates)
     * @param openGameFile
     */
    public void addOpenGame(OpenGameFile openGameFile) {
        allGamesNotFinishedNames.add(openGameFile.getNameId());
        allOpenGames.add(openGameFile);
        sendMessageToAll("OGAM " + openGameFile.getSendReady());
    }

    /**
     * remove an "openGame" from openGameList and from names
     * @param openGameFileID
     */
    public void removeOpenGame(String openGameFileID) {

        // send INFO message
        getOpenGameFile(openGameFileID).sendMessageToParticipants("INFO delete this open game now");

        // send message to public
        // TODO for DOGA send only nameID not all data
        sendMessageToAll("DOGA " + getOpenGameFile(openGameFileID).getSendReady());

        // send participants back to lobby
        for (Player player : getOpenGameFile(openGameFileID).getPlayers()) {
            player.getServerConnection().getMessageHandlerServer().returnToLobby();
        }

        // remove

        for (int i = 0; i < allOpenGames.size(); i++) {
            if (allOpenGames.get(i).getNameId().equals(openGameFileID)) {
                allOpenGames.remove(i);
                System.out.println("removed open game from server");
            }
        }
    }

    /**
     * returns the openGameFile with the same name
     * @param openGameID name of game
     * @return openGameFile
     */
    public OpenGameFile getOpenGameFile (String openGameID) {
        for (OpenGameFile openGameFile : allOpenGames) {
            if (openGameFile.getNameId().equals(openGameID)) {
                return openGameFile;
            }
        }
        return null;
    }

    /**
     * returns the list of all open games
     * @return not started games
     */
    public ArrayList<OpenGameFile> getOpenGameList() {
        return allOpenGames;
    }
}
