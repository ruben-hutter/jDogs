package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.Alliance_4;
import jDogs.serverclient.helpers.Queuejd;
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
    private String nickname;
    private final ServerParser serverParser;
    private GameState gameState;
    private static final Logger logger = LogManager.getLogger(ServerGameCommand.class);
    private ArrayList<Player> players;
    private MainGame mainGame;
    private String cardToEliminate;
    private Alliance_4 alliance4;
    private RulesCheck rulesCheck;
    private String mainGameID;

    public ServerGameCommand(ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer) {
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.loggedIn = false;
        this.serverParser = new ServerParser(serverConnection);
        this.players = null;
        this.cardToEliminate = null;
        this.rulesCheck = new RulesCheck(serverConnection);
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
                this.mainGame.sendMessageToParticipants("INFO " + nickname + " left game session");
                this.mainGame.delete();
                break;
            case "EXIT":
                //stop serverConnection
                this.mainGame.sendMessageToParticipants("INFO " + nickname + " left game session");
                this.mainGame.delete();
                this.serverConnection.kill();
                break;

            case "MOVE":
                if (text.length() >= 9 && mainGame.getActualPlayer().equals(nickname)) {
                    if (text.substring(5, 9).equals("SURR")) {
                        mainGame.getPlayer(nickname).setAllowedToPlay(false);
                        gameState.getCards().get(nickname).clear();
                        serverConnection.sendToClient("INFO excluded for this round");
                        mainGame.turnComplete(nickname);
                        break;
                    }

                    String playerName = serverConnection.getNickname();
                    logger.debug("Player nickname: " + playerName);
                    Player player = mainGame.getPlayer(playerName);
                    logger.debug("Player: " + player);
                    cardToEliminate = text.substring(5, 9);
                    String toCheckMove = rulesCheck.checkCard(text, gameState, nickname);
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
                            rulesCheck.checkMoveSeven(toCheckMove, gameState, mainGame,
                                    nickname);
                            break;
                        case "JACK":
                            rulesCheck.checkMoveJack(toCheckMove, gameState, mainGame,
                                    nickname);
                            break;
                        default:
                            rulesCheck.checkMove(toCheckMove, gameState, mainGame,
                                    nickname);
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
                        "LCHT " + "<" + nickname + "> " + text.substring(5));
                break;
            //send message to everyone logged in, in lobby, separated or playing
            case "PCHT":
                serverConnection.sendToAll("PCHT " + "<" + nickname + "> " + text.substring(5));
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
            if (Server.getInstance().runningGames.get(i).getGameId().equals(actualGame)) {
                return Server.getInstance().runningGames.get(i);
            }
        }
        return null;
    }

    public void setMainGame(String mainGameID) {
        this.mainGameID = mainGameID;
        this.mainGame = Server.getInstance().getRunningGame(mainGameID);
    }
}
