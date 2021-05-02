package jDogs.serverclient.serverside;

import jDogs.player.Player;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ServerGameCommand contains the game
 * commands which are sent from the
 * clients to communicate with the
 * server.
 *
 */
public class ServerGameCommand {

    private final ServerConnection serverConnection;
    private final MessageHandlerServer messageHandlerServer;
    private boolean loggedIn;
    private final ServerParser serverParser;
    private static final Logger logger = LogManager.getLogger(ServerGameCommand.class);
    private ArrayList<Player> players;
    private MainGame mainGame;
    private RulesCheck rulesCheck;
    private String mainGameID;

    public ServerGameCommand(ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer) {
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.loggedIn = false;
        this.serverParser = new ServerParser(serverConnection);
        this.players = null;
    }

    /**
     * this method executes all commands
     * import for playing a game on serverside
     * @param text command which should be processed
     */
    public void execute(String text) {
        logger.debug("Entered ServerGameCommand with: " + text);
        String command = text.substring(0, 4);

        switch (command) {
            case "QUIT":
                this.mainGame.sendMessageToParticipants("INFO " + serverConnection.getNickname() + " left game session");
                this.mainGame.delete();
                break;
            case "EXIT":
                //stop serverConnection
                this.mainGame.sendMessageToParticipants("INFO " + serverConnection.getNickname() + " left game session");
                this.mainGame.delete();
                this.serverConnection.kill();
                break;

            case "MOVE":
                if (text.length() >= 9 && mainGame.getActualPlayer().equals(serverConnection.getNickname())) {
                    if (text.substring(5, 9).equals("SURR")) {
                        mainGame.getPlayer(serverConnection.getNickname()).setAllowedToPlay(false);
                        mainGame.getGameState().getCards().get(serverConnection.getNickname()).clear();
                        serverConnection.sendToClient("INFO excluded for this round");
                        mainGame.turnComplete(serverConnection.getNickname());
                        break;
                    }

                    String playerName = serverConnection.getNickname();
                    logger.debug("Player nickname: " + playerName);
                    Player player = mainGame.getPlayer(playerName);
                    logger.debug("Player: " + player);
                    String toCheckMove = rulesCheck.checkCard(text, serverConnection.getNickname());
                    if (toCheckMove == null) {
                        serverConnection.sendToClient("INFO Invalid card or no hand");
                        serverConnection.sendToClient("TURN");
                        logger.debug("You don't have this card on your hand");
                        return;
                    }

                    // special cases (move command syntax different from normal)
                    String card = toCheckMove.substring(0, 4);
                    switch (card) {
                        case "SEVE":
                            rulesCheck.checkMoveSeven(toCheckMove,
                                    serverConnection.getNickname());
                            break;
                        case "JACK":
                            rulesCheck.checkMoveJack(toCheckMove, mainGame,
                                    serverConnection.getNickname());
                            break;
                        default:
                            rulesCheck.checkMove(toCheckMove,
                                    serverConnection.getNickname());
                    }
                }
                break;
            case "CTTP":
                // TODO start CTTP
                //change cards
                break;
            case "LCHT":
                //sendToAll.enqueue("PCHT " + "<" + nickname + ">" + text.substring(4));
                System.out.println("LCHT: " + text.substring(5));
                mainGame.sendMessageToParticipants(
                        "LCHT " + "<" + serverConnection.getNickname() + "> " + text.substring(5));
                break;
            //send message to everyone logged in, in lobby, separated or playing
            case "PCHT":
                serverConnection.sendToAll("PCHT " + "<" + serverConnection.getNickname() + "> " + text.substring(5));
                break;
        }
    }
    public void setMainGame(String mainGameID) {
        this.mainGameID = mainGameID;
        this.mainGame = Server.getInstance().getRunningGame(mainGameID);
        this.rulesCheck = new RulesCheck(serverConnection, mainGame);

    }
}
