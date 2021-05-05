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
                    UpdateClient returnValue;
                    switch (card) {
                        case "SEVE":
                            returnValue = rulesCheck.checkMoveSeven(toCheckMove, serverConnection.
                                    getNickname());
                            switch (returnValue.getReturnValue()) {
                                case 0:
                                    sendMovesToParticipants(returnValue);
                                    break;
                                case 1:
                                    serverConnection.sendToClient("INFO At least one invalid"
                                            + "destination or piece!");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 2:
                                    serverConnection.sendToClient("INFO You moved more than 7!");
                                    serverConnection.sendToClient("TURN");
                                case 3:
                                    serverConnection.sendToClient("INFO You can't jump over your own"
                                            + "pieces!");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 4:
                                    serverConnection.sendToClient("INFO You don't move a total of 7!");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 5:
                                    serverConnection.sendToClient("INFO wrong format for seven");
                                    serverConnection.sendToClient("TURN");
                                    break;
                            }
                            break;
                        case "JACK":
                            returnValue = rulesCheck.checkMoveJack(toCheckMove, serverConnection.
                                    getNickname());
                            switch (returnValue.getReturnValue()) {
                                case 0:
                                    sendMovesToParticipants(returnValue);
                                    break;
                                case 1:
                                    serverConnection.sendToClient("INFO You have not finished");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 2:
                                    serverConnection.sendToClient("INFO You cannot move this color");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 3:
                                    serverConnection.sendToClient("INFO You can't switch this"
                                            + "pieces!");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 4:
                                    serverConnection.sendToClient("INFO wrong format for jack");
                                    serverConnection.sendToClient("TURN");
                                    break;
                            }
                            break;
                        default:
                            returnValue = rulesCheck.checkMove(toCheckMove, serverConnection.
                                    getNickname());
                            switch(returnValue.getReturnValue()) {
                                case 0:
                                    sendMovesToParticipants(returnValue);
                                    break;
                                case 1:
                                    serverConnection.sendToClient("INFO You can't move a piece in"
                                            + "home");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 2:
                                    serverConnection.sendToClient("INFO Format exception in"
                                            + "checkMove");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 3:
                                    serverConnection.sendToClient("INFO You have not finished");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 4:
                                    serverConnection.sendToClient("INFO You cannot move this color");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 5:
                                    serverConnection.sendToClient("INFO Check your move's validity");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 6:
                                    serverConnection.sendToClient("INFO Someone blocks you");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 7:
                                    serverConnection.sendToClient("INFO You eliminate yourself!");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 8:
                                    serverConnection.sendToClient("INFO Entered command does`t fit"
                                            + "the length(15) for checkMove()");
                                    serverConnection.sendToClient("TURN");
                                    break;
                                case 9:
                                    String winner = null;
                                    Server.getInstance().storeGame(mainGame.getGameId(), winner);
                                    break;
                            }
                            break;
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

    /**
     * Sets the parameters for a game
     * @param mainGameID the chosen name for the game
     */
    public void setMainGame(String mainGameID) {
        this.mainGameID = mainGameID;
        this.mainGame = Server.getInstance().getRunningGame(mainGameID);
        this.rulesCheck = new RulesCheck(mainGame);
    }

    /**
     * Sends all the moves done on server side to the clients
     * @param sendMovesToParticipants object that contains all the different moves
     */
    private void sendMovesToParticipants(UpdateClient sendMovesToParticipants) {
        for (String move : sendMovesToParticipants.getMoves()) {
            mainGame.sendMessageToParticipants(move);
        }
        // send new board state
        mainGame.sendMessageToParticipants(sendMovesToParticipants.getUpdateGame()[0]);
        // eliminate card from hand
        serverConnection.sendToClient(sendMovesToParticipants.getUpdateGame()[1]);
        // send actual hand
        mainGame.sendMessageToParticipants(sendMovesToParticipants.getUpdateGame()[2]);
    }
}
