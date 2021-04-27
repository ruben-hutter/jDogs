package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.Alliance_4;
import jDogs.player.Piece;
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

    private final Server server;
    private final ServerConnection serverConnection;
    private final MessageHandlerServer messageHandlerServer;
    private final Queuejd sendToThisClient;
    private final Queuejd sendToAll;
    private boolean loggedIn;
    private String nickname;
    private final ServerParser serverParser;
    private GameFile gameFile;
    private GameState gameState;
    private static final Logger logger = LogManager.getLogger(ServerGameCommand.class);
    private ArrayList<Player> players;
    private MainGame mainGame;
    private String cardToEliminate;
    private Alliance_4 alliance4;
    private RulesCheck rulesCheck;

    public ServerGameCommand(Server server, ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer, Queuejd sendToThisClient, Queuejd sendToAll) {
        this.server = server;
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.loggedIn = false;
        this.serverParser = new ServerParser(server, serverConnection);
        this.players = null;
        this.cardToEliminate = null;
        this.rulesCheck = new RulesCheck(sendToThisClient);
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
                this.gameFile.sendMessageToParticipants("INFO " + nickname + " left game session");
                this.gameFile.cancel();
                break;
            case "EXIT":
                //stop serverConnection
                this.gameFile.sendMessageToParticipants("INFO " + nickname + " left game session");
                this.gameFile.cancel();
                this.serverConnection.kill();
                break;

            case "MOVE":
                if (text.length() >= 9 && mainGame.getActualPlayer().equals(nickname)) {
                    System.out.println("servergamecomm " + text);
                    if (text.substring(5, 9).equals("SURR")) {
                        gameFile.getPlayer(nickname).setAllowedToPlay(false);
                        gameState.getCards().get(nickname).clear();
                        sendToThisClient.enqueue("INFO excluded for this round");
                        mainGame.turnComplete(nickname);
                        break;
                    }

                    String playerName = serverConnection.getNickname();
                    logger.debug("Player nickname: " + playerName);
                    Player player = gameFile.getPlayer(playerName);
                    logger.debug("Player: " + player);
                    cardToEliminate = text.substring(5, 9);
                    String toCheckMove = rulesCheck.checkCard(text, gameState, nickname);
                    if (toCheckMove == null) {
                        sendToThisClient.enqueue("INFO Invalid card or no hand");
                        sendToThisClient.enqueue("TURN");
                        logger.debug("You don't have this card on your hand");
                        return;
                    }

                    // special cases (move command syntax different from normal)
                    String card = toCheckMove.substring(0, 4);
                    switch (card) {
                        case "SEVE":
                            rulesCheck.checkMoveSeven(toCheckMove, gameState, gameFile, mainGame,
                                    nickname);
                            break;
                        case "JACK":
                            rulesCheck.checkMoveJack(toCheckMove, gameState, gameFile, mainGame,
                                    nickname);
                            break;
                        default:
                            rulesCheck.checkMove(toCheckMove, gameState, gameFile, mainGame,
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
                gameFile.sendMessageToParticipants(
                        "LCHT " + "<" + nickname + "> " + text.substring(5));
                break;
            //send message to everyone logged in, in lobby, separated or playing
            case "PCHT":
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
            if (Server.getInstance().runningGames.get(i).getGameId().equals(actualGame)) {
                return Server.getInstance().runningGames.get(i);
            }
        }
        return null;
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
        this.gameFile = mainGame.getGameFile();
        this.gameState = mainGame.getGameState();
        players = mainGame.getGameFile().getPlayers();
        this.nickname = serverConnection.getNickname();
    }
}
