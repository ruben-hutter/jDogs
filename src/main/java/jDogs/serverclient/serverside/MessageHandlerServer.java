package jDogs.serverclient.serverside;

import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This thread processes messages received meaningfully.
 * it processes messages on the basis of the String "state" in
 * ServerMenuCommand("publicLobby"), SeparateLobbyCommand("openGame") and
 * ServerGameCommand("playing").
 */
public class MessageHandlerServer implements Runnable {

    private final BlockingQueue<String> receivedFromClient;
    private boolean running;
    private boolean loggedIn;
    private final ServerConnection serverConnection;
    private final ServerGameCommand serverGameCommand;
    private final ServerMenuCommand serverMenuCommand;
    private final SeparateLobbyCommand separateLobbyCommand;
    private String state;
    private String nickname;
    private final Logger LOGGER = LogManager.getLogger(MessageHandlerServer.class);

    /**
     * constructs a message-handler-server object
     * @param server ServerInstance
     * @param serverConnection sC - object
     * @param sendToThisClient blockingQueue
     * @param sendToAll blockingQueue
     * @param receivedFromClient blockingQueue
     * @param sendToPub blockingQueue
     */
    public MessageHandlerServer(Server server,ServerConnection serverConnection,
            BlockingQueue<String> sendToThisClient, BlockingQueue<String> sendToAll, BlockingQueue<String> receivedFromClient, BlockingQueue<String> sendToPub) {

        this.receivedFromClient = receivedFromClient;
        this.serverConnection = serverConnection;
        this.running = true;
        this.loggedIn = false;
        this.serverMenuCommand = new ServerMenuCommand(serverConnection,this);
        this.serverGameCommand = new ServerGameCommand(serverConnection, this);
        this.separateLobbyCommand = new SeparateLobbyCommand(serverConnection);
        this.state = "publicLobby";
    }

    @Override
    public void run() {
        String text;

        // get loggedIn
        serverConnection.sendToClient("USER");

        //while()-loop always running
        try {
            while (running) {
                    text = receivedFromClient.take();
                    if (text.length() >= 4) {
                        System.err.println("text: " + text + " text lengh " + text.length());
                        switch (state) {

                            case "playing":
                                System.out.println("game command case");
                                serverGameCommand.execute(text);
                                break;

                            case "openGame":
                                System.out.println("open game case");
                                separateLobbyCommand.execute(text);
                                break;

                            case "publicLobby":
                                System.out.println("public lobby case");
                                serverMenuCommand.execute(text);
                                break;

                        }

                    } else {
                        System.err.println(
                                "message did not match public, separate or game protocol:  " + text);
                    }
                }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println(this.toString() + "  stops now");
    }
    /**
     * Returns the nickName of the user
     * @return nickName
     */
    public String getNickName() {
        return serverMenuCommand.getNickName();
    }
    /**
     * Kills thread
     */
    public synchronized void kill() {
        running = false;
    }

    /**
     * sets messsageHandler to "playing" state and
     * sends the gameID to serverConnection
     * and serverGameCommand
     * @param mainGameID name of game
     */
    public void setPlaying(String mainGameID) {
            serverConnection.setState(1);
            serverConnection.setGameID(mainGameID);
            serverGameCommand.setMainGame(mainGameID);
            state = "playing";
    }

    /**
     * sets state to "openGame" and
     * sends gameFileID to separateLobbyCommand
     * @param openGameFileID the name of the game lobby
     */
    public void setJoinedOpenGame(String openGameFileID) {
        serverConnection.setState(0);
        serverConnection.setGameID(openGameFileID);
        Server.getInstance().removeFromLobby(serverConnection);
        serverConnection.sendToPub("DPER " + serverConnection.getNickname());
        state = "openGame";
        separateLobbyCommand.setJoinedGame(openGameFileID);
        this.nickname = nickname;
    }

    /**
     * this method is used to set instructions to public lobby
     * from game or from separate lobby
     */
    public synchronized void returnToLobby() {
        state = "publicLobby";
        serverConnection.setGameID(null);
        Server.getInstance().addToLobby(serverConnection);
        serverConnection.sendToPub("LPUB " + serverConnection.getNickname());
        serverMenuCommand.sendListOfPublicGuests();
    }

    /**
     *
     * @return the state which describes the mode in which the client is; is he public
     * , separated or playing a game?
     * The state is important for the switch in the run method
     */
    public String getState() {
        return state;
    }
}
