package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.Queuejd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This thread processes messages received meaningfully.
 * it distinguishes between messages for server and all clients
 * it handles the login of the client:
 *
 * switch defines which commands are accepted and what they cause
 *
 * playing
 *
 * openGame(separateLobby)
 *
 * public Lobby
 */

public class MessageHandlerServer implements Runnable {

    private final Queuejd sendToAll;
    private final Queuejd sendToThisClient;
    private final Queuejd receivedFromClient;
    private final Queuejd sendToPub;
    private boolean running;
    private boolean loggedIn;
    private final Server server;
    private final ServerConnection serverConnection;
    private ServerGameCommand serverGameCommand;
    private ServerMenuCommand serverMenuCommand;
    private SeparateLobbyCommand separateLobbyCommand;
    private String state;
    private String nickname;
    private final Logger LOGGER = LogManager.getLogger(MessageHandlerServer.class);
    private GameFile gameFile;

    public MessageHandlerServer(Server server,ServerConnection serverConnection,
            Queuejd sendToThisClient, Queuejd sendToAll, Queuejd receivedFromClient, Queuejd sendToPub) {
        this.sendToPub = sendToPub;
        this.sendToAll = sendToAll;
        this.sendToThisClient = sendToThisClient;
        this.receivedFromClient = receivedFromClient;
        this.server = server;
        this.serverConnection = serverConnection;
        this.running = true;
        this.loggedIn = false;
        this.serverMenuCommand = new ServerMenuCommand(server, serverConnection,this,sendToThisClient, sendToAll);
        this.serverGameCommand = new ServerGameCommand(server, serverConnection,this,sendToThisClient, sendToAll);
        this.separateLobbyCommand = new SeparateLobbyCommand(sendToThisClient, sendToAll, sendToPub, serverConnection);
        this.state = "publicLobby";
    }

    @Override
    public void run() {
        String text;

        // get loggedIn
        sendToThisClient.enqueue("USER");

        //while()-loop always running
        while (running) {
            if (!receivedFromClient.isEmpty()) {
                text = receivedFromClient.dequeue();
                System.out.println("messHandlerServer: " + text);
                if (text.length() >= 4) {

                switch(state) {

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

                    System.err.println("message did not match public, separate or game protocol:  " + text);
                }
            } else {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
    public void kill() {
        running = false;
    }

    /**
     *
     * @param playing true: send messages to GameCommand (a game started)
     *                false: send messages to public MenuCommand (a game ended)
     */

    public void setPlaying(boolean playing,MainGame mainGame) {
        if (playing) {
            this.gameFile = mainGame.getGameFile();
            serverGameCommand.setMainGame(mainGame);
            state = "playing";
        } else {
            state = "publicLobby";
        }
    }

    /**
     *
     * @param gameFile the openGame the client joins
     * @param nickname the actual nickname he has(could also be omitted)
     */
    public void setJoinedOpenGame(GameFile gameFile, String nickname) {
        server.removeFromLobby(serverConnection);
        sendToPub.enqueue("DPER " + nickname);
        this.gameFile = gameFile;
        state = "openGame";
        //server.removeSender(serverConnection.getSender());
        server.publicLobbyGuests.remove(nickname);
        separateLobbyCommand.setJoinedGame(gameFile, nickname);
        this.nickname = nickname;
    }

    /**
     * this method is used to set instructions to public lobby
     * from game or from separate lobby
     */
    public void returnToLobby() {
        server.addToLobby(serverConnection);
        server.publicLobbyGuests.add(nickname);
        sendToPub.enqueue("LPUB " + nickname);

        //server.addSender(serverConnection.getSender());
        serverMenuCommand.sendAllPublicGuests();
        state = "publicLobby";

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

    /**
     *
     * @return gameFile of the game or opened game(not playing yet)
     */
    public GameFile getGameFile() {
        return gameFile;
    }

    public ServerMenuCommand getServerMenuCommand() {
        return serverMenuCommand;
    }

    public SeparateLobbyCommand getSeparateLobbyCommand() {
        return separateLobbyCommand;
    }

    public ServerGameCommand getServerGameCommand() {
        return serverGameCommand;
    }


}
