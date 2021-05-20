package jDogs.serverclient.serverside;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jDogs.player.Player;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import javax.imageio.stream.ImageInputStreamImpl;

/**
 * Server waits for new clients trying to connect to server,
 * and if one connects the server creates a ServerConnection-object
 * for this client.
 *
 * ArrayList with all Sender-objects lies here
 * ArrayList with all Nicknames lies here
 * Hashmap with String(userName) and User(Password, Nickname..) lies here
 */
public class Server {

    private final CSVWriter csvWriter;
    private ServerSocket serverSocket;
    //this list contains all sender objects, but I want to replace by the list of all serverConnection objects
    //this list contains all nicknames used at the moment(to avoid duplicates)
    ArrayList<String> allNickNames = new ArrayList<>();
    //this map contains the names and the corresponding serverConnections objects
    private Map<String, ServerConnection> serverConnectionMap = new HashMap<>();
    //this list contains all ongoing games
    private final ArrayList<MainGame> runningGames = new ArrayList<>();
    //this list contains all finished games
    //this list contains all server connections active in the public lobby
    private final ArrayList<ServerConnection> publicLobbyConnections = new ArrayList<>();
    //this list exists only to store all serverConnections to enable more ServerConnections
    private final ArrayList<ServerConnection> basicConnectionList = new ArrayList<>();
    private static Server instance;
    private boolean running = true;
    // contains all names of openGameFiles and running games
    private final ArrayList<String> allGamesNotFinishedNames = new ArrayList<>();
    // contains all openGameFiles
    private final ArrayList<OpenGameFile> allOpenGames = new ArrayList<>();

    /**
     * constructor of the server and place where
     * the while loop is called
     * @param args "Server port number"
     */
    public Server(String[] args) {
        instance = this;
        csvWriter = new CSVWriter();
        if (csvWriter.isDirectoryExisting()) {csvWriter.readCSV();}
        try {
            serverSocket = new ServerSocket(Integer.parseInt(args[1]));
            // runs as long as the server is activated
            while (running) {
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
     * start Server here
     * @param args the port number
     */
    public static void main(String[] args) {
        new Server(args);
    }

    /**
     * get the singleton, single object of this class
     * @return static single object
     */
    public static Server getInstance() {
        return instance;
    }

    /**
     * Checks if a nickname is valid, if it doesn't already exist
     *
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
     * this methlod is needed to send messages to clients which are connected by public lobbies or
     * by a common game
     * @param serverConnection is the sC of this client
     */
    public void addNickname(ServerConnection serverConnection) {

        //scMap
        serverConnectionMap.put(serverConnection.getNickname(), serverConnection);

        //add to nicknamelist
        allNickNames.add(serverConnection.getNickname());
    }

    /**
     * removes a nickname from all lists
     * @param nickname the player's name
     */
    public void removeNickname(String nickname) {

        //remove nickname from sConnectionNicknameMap

        serverConnectionMap.remove(nickname);

        //remove nickname from nicknamelist
        allNickNames.remove(nickname);
    }

    /**
     * start game by creating mainGame and delete openGame file
     *
     * @param openGameFile extract necessary data and delete it
     */
    public void startGame(OpenGameFile openGameFile) {
        MainGame mainGame = new MainGame(openGameFile.getPlayersArray(), openGameFile.getNameId(),
                openGameFile.isTeamMode());
        // add running game
        runningGames.add(mainGame);
        //add name
        allGamesNotFinishedNames.add(openGameFile.getNameId());
        // remove open game file
        removeOpenGame(openGameFile.getNameId());

        mainGame.start();
    }

    /**
     * returns the serverConnection which fits the nickname
     * @param nickname player's name
     * @return serverConnection object
     */
    public ServerConnection getServerConnection(String nickname) {

        for (ServerConnection serverConnection : basicConnectionList) {
            if (serverConnection.getNickname().equals(nickname)) {
                return serverConnection;
            }
        }
        return null;
    }

    /**
     * removes the openGame from the list
     * @param openGameFile object that represents a game lobby
     */
    public void removeOpenGame(OpenGameFile openGameFile) {
      sendMessageToPublic("DOGA " + openGameFile.getSendReady(), 0);

            for (Player player : openGameFile.getPlayers()) {
                player.getServerConnection().getMessageHandlerServer().returnToLobby();
            }

        MainGame mainGame;
        if ((mainGame = getRunningGame(openGameFile.getNameId())) != null) {
            runningGames.remove(mainGame);
        }
    }


    /**
     * sends message to clients wherever they are
     * @param message message to send
     */
    public void sendMessageToAll(String message) {
        for (ServerConnection activeServerConnection1 : basicConnectionList) {
            activeServerConnection1.sendToClient(message);
        }
    }

    /**
     * send message to public guests
     * try-catch: just continue with the next person in list, if an error occurs
     * @param message message
     * @param i the index number
     */
    public void sendMessageToPublic(String message, int i) {
        try {
            while (i < publicLobbyConnections.size()) {
                publicLobbyConnections.get(i).sendToClient(message);
                i++;
            }
        } catch (Exception e) {
            i++;
            sendMessageToPublic(message, i);
        }
    }

    /**
     * add a serverConnection object to lobby list
     * @param serverConnection handles connection to server
     */
    public void addToLobby(ServerConnection serverConnection) {
        publicLobbyConnections.add(serverConnection);
    }

    /**
     * remove a serverConnection object from lobby list
     * @param serverConnection handles connection to server
     */
    public void removeFromLobby(ServerConnection serverConnection) {
        publicLobbyConnections.remove(serverConnection);
    }

    /**
     * remove a serverConnection and its name from all lists
     * @param serverConnection handles connection to server
     */
    public synchronized void removeServerConnection(ServerConnection serverConnection) {
        sendMessageToPublic("DPER " + serverConnection.getNickname(), 0);
        allNickNames.remove(serverConnection.getNickname());
        serverConnectionMap.remove(serverConnection.getNickname());
        publicLobbyConnections.remove(serverConnection);
        basicConnectionList.remove(serverConnection);
    }

    /**
     * add an "openGameFile" to openGames and add name to list of games that did not finish(prevent
     * name duplicates)
     * @param openGameFile object that represents a game lobby
     */
    public void addOpenGame(OpenGameFile openGameFile) {
        allGamesNotFinishedNames.add(openGameFile.getNameId());
        allOpenGames.add(openGameFile);
        sendMessageToAll("OGAM " + openGameFile.getSendReady());
    }

    /**
     * remove an "openGame" from openGameList and from names (keep in mind: messagehandlerstate is
     * not changed here)
     * @param openGameFileID name of the game lobby given by host
     */
    public synchronized void removeOpenGame(String openGameFileID) {
        // send INFO message
        getOpenGameFile(openGameFileID)
                .sendMessageToParticipants("INFO deleted this open game now");

        // send message to public
        // TODO for DOGA send only nameID not all data
        sendMessageToAll("DOGA " + getOpenGameFile(openGameFileID).getSendReady());

        // remove file
        for (int i = 0; i < allOpenGames.size(); i++) {
            if (allOpenGames.get(i).getNameId().equals(openGameFileID)) {
                allOpenGames.remove(i);
            }
        }

        // remove name
        allGamesNotFinishedNames.remove(openGameFileID);
    }

    /**
     * returns the openGameFile with the same name
     * @param openGameID name of game
     * @return openGameFile
     */
    public OpenGameFile getOpenGameFile(String openGameID) {
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

    /**
     * get all names of not finished games
     * @return list with all names
     */
    public ArrayList<String> getAllGamesNotFinishedNames() {
        return allGamesNotFinishedNames;
    }

    /**
     * get all running games
     * @return arrayList of mainGame objects
     */
    public ArrayList<MainGame> getRunningGames() {
        return runningGames;
    }

    /**
     * get the main game running by name
     * @param mainGameID a string
     * @return mainGame container
     */
    public MainGame getRunningGame(String mainGameID) {
        for (MainGame mainGame : runningGames) {
            if (mainGame.getGameId().equals(mainGameID)) {
                return mainGame;
            }
        }
        return null;
    }

    /**
     * delete this mainGame
     * @param mainGame contains the information of a game,
     *                 like participants, cards, turn number
     */
    public void deleteMainGame(MainGame mainGame) {
        for (Player player : mainGame.getPlayersArray()) {
            player.getServerConnection().getMessageHandlerServer().returnToLobby();
        }
        runningGames.remove(mainGame);
    }

    /**
     * remove open game when a client disconnected abruptly and is host else just remove this
     * participant
     * @param gameID openGameId
     * @param nickname player's name
     */
    public synchronized void errorRemoveOpenGame(String gameID, String nickname) {
        if (getOpenGameFile(gameID).getHost().equals(nickname)) {
            for (Player player : getOpenGameFile(gameID).getPlayers()) {
                if (!player.getPlayerName().equals(nickname)) {
                    player.getServerConnection().getMessageHandlerServer().returnToLobby();
                }
            }
            removeOpenGame(gameID);
        } else {
            getOpenGameFile(gameID).removeParticipant(nickname);
        }
    }

    /**
     * remove main game after a client disconnected abruptly and inform clients
     * @param gameID name of the game given by host
     * @param nickname player's name that left a game session
     */
    public synchronized void errorRemoveMainGame(String gameID, String nickname) {
        try {
            for (Player player : getRunningGame(gameID).getPlayersArray()) {
                if (!player.getPlayerName().equals(nickname)) {
                    player.getServerConnection().getMessageHandlerServer().returnToLobby();
                    player.sendMessageToClient("INFO shutdown game.Client "
                            + nickname + " just left session.");
                    player.sendMessageToClient("STOP");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("removed game with name " +
                    gameID + runningGames.remove(getRunningGame(gameID)));
        }
    }

    /**
     * stores user data after victory
     * @param gameID game's name
     * @param winner the winner's name or the 2 names separated by a whitespace
     */
    public void storeGame(String gameID, String winner) {
        MainGame mainGame = getRunningGame(gameID);

        // if 2 winners
        if (mainGame.isTeamMode()) {
            int separator = -1;
            for (int i = 0; i < winner.length(); i++) {
                if (Character.isWhitespace(winner.charAt(i))) {
                    separator = i;
                    break;
                }
            }

            String winner1 = winner.substring(0, separator);
            String winner2 = winner.substring(separator + 1);
            int count = -1;

            for (Player player : mainGame.getPlayersArray()) {
                // check with highScoreList
                for (SavedUser savedUser : csvWriter.getUsersHighScore()) {
                    if (savedUser.getName().equals(player.getPlayerName())) {
                        if (player.getPlayerName().equals(winner1) || player.getPlayerName()
                                .equals(winner2)) {
                            savedUser.addVictory();
                        } else {
                            savedUser.addDefeat();
                        }
                        count = 0;
                        break;
                    }
                }
                // if username is not in highScoreList: add it here
                if (count == -1) {
                    SavedUser savedUser = new SavedUser(player.getPlayerName());
                    if (player.getPlayerName().equals(winner1) || player.getPlayerName()
                            .equals(winner2)) {
                        savedUser.addVictory();
                    } else {
                        savedUser.addDefeat();
                    }
                    csvWriter.addUser(savedUser);
                } else {
                    count = -1;
                }
            }
            //else one winner
        } else {
            // check highScoreList
            int count = -1;
            for (Player player : mainGame.getPlayersArray()) {
                for (SavedUser savedUser : csvWriter.getUsersHighScore()) {
                    if (savedUser.getName().equals(player.getPlayerName())) {
                        if (player.getPlayerName().equals(winner)) {
                            savedUser.addVictory();
                        } else {
                            savedUser.addDefeat();
                        }
                        count = 0;
                        break;
                    }
                }
                // if username is not in highScoreList: add it here
                if (count == -1) {
                    SavedUser savedUser = new SavedUser(player.getPlayerName());
                    if (player.getPlayerName().equals(winner)) {
                        savedUser.addVictory();
                    } else {
                        savedUser.addDefeat();
                    }
                    csvWriter.addUser(savedUser);
                } else {
                    count = -1;
                }
            }
        }
        csvWriter.rankList();
        csvWriter.writeCSV();

        // delete main game
        deleteMainGame(mainGame);
        System.out.println("END");
    }
    /**
     * get highScoreList with all savedUsers
     * @return arrayList
     */
    public ArrayList<SavedUser> getHighScoreList() {
        return csvWriter.getUsersHighScore();
    }

    /**
     * returns the list of all public lobby connections
     * @return arraylist with server connections
     */
    public ArrayList<ServerConnection> getPublicLobbyConnections() {
        return publicLobbyConnections;
    }
}

