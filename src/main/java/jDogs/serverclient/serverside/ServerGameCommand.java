package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.serverclient.helpers.Queuejd;

/**
 * ServerGameCommand contains the game
 * commands which are sent from the
 * clients to communicate with the
 * server.
 *
 */

public class ServerGameCommand {
    private final Server server;
    private final ServerConnection serverConnection;
    private final MessageHandlerServer messageHandlerServer;
    private final Queuejd sendToThisClient;
    private final Queuejd sendToAll;
    private boolean loggedIn;
    private String nickname;
    private final ServerParser serverParser;
    private String actualGame;
    private GameFile gameFile;

    public ServerGameCommand(Server server, ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer, Queuejd sendToThisClient, Queuejd sendToAll) {
        this.server = server;
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.loggedIn = false;
        this.nickname = null;
        this.serverParser = new ServerParser(server,serverConnection);
    }

    public void execute(String text) {
        String command = text.substring(0,4);

        switch(command) {
            case "QUIT":
                // TODO stop ServerConnection and Client
            case "EXIT":
                // TODO startExit();
                //finish game
                break;
            case "MOVE":
                // TODO startMove()
                break;

            case "CTTP":
                // TODO start CTTP
                //change cards
                break;

            case "LCHT":
                //sendToAll.enqueue("PCHT " + "<" + nickname + ">" + text.substring(4));
                
                System.out.println("LCHT: " + text.substring(5));
                gameFile.sendMessageToParticipants("LCHT " + "<" + nickname + "> " + text.substring(5));

                break;

            case "PCHT":
                //send message to everyone logged in, in lobby, separated or playing

                sendToAll.enqueue("PCHT " + "<" + nickname + "> " + text.substring(5));
                break;
        }

    }


    /**
     *
     * @param actualGame is the ongoing game which should be found
     * @return the game or null
     */

    private MainGame getRunningGame(String actualGame) {
        for (int i = 0; i < Server.getInstance().runningGames.size(); i++) {
            if (Server.getInstance().runningGames.get(i).getGameId() == actualGame) {
                return Server.getInstance().runningGames.get(i);
            }
        }
        return null;
    }

    public void setGameFile(GameFile gameFile) {
        this.gameFile = gameFile;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }
}
