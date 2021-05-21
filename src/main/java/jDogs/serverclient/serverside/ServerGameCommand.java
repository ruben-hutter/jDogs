package jDogs.serverclient.serverside;

import jDogs.player.Piece;
import jDogs.player.Player;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ServerGameCommand contains the game
 * commands which are sent from the
 * clients to communicate with the
 * server.
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

    /**
     * set up a serverGameCommand object
     * @param serverConnection sC object
     * @param messageHandlerServer mHS object
     */
    public ServerGameCommand(ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer) {
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.loggedIn = false;
        this.serverParser = new ServerParser(serverConnection);
        this.players = null;
    }

    /**
     * executes the commands that are received in ReceivedFromClient if they
     * corresponded to the formal criteria in ReceivedFromClient
     * @param text command and information
     */
    public void execute(String text) {
        logger.debug("Entered ServerGameCommand with: " + text);
        ServerGameProtocol command = ServerGameProtocol.toCommand(text.substring(0, 4));
        try {
            switch (Objects.requireNonNull(command)) {
                case QUIT:
                    this.mainGame.sendMessageToParticipants("INFO " + serverConnection.getNickname()
                            + " left game session");
                    this.mainGame.delete();
                    break;

                case EXIT:
                    //stop serverConnection
                    this.mainGame.sendMessageToParticipants("INFO " + serverConnection.getNickname()
                            + " left game session");
                    this.mainGame.delete();
                    this.serverConnection.kill();
                    break;

                case MOVE:
                    if (mainGame.getActualPlayer().equals(serverConnection.getNickname())
                            && mainGame.getPlayer(serverConnection.getNickname()).isAllowedToPlay()) {
                        // SURR
                        if (text.startsWith("SURR", 5) && text.length() == 9) {
                            mainGame.getPlayer(serverConnection.getNickname()).setAllowedToPlay(false);
                            mainGame.getGameState().getCards().get(serverConnection.getNickname())
                                    .clear();
                            serverConnection.sendToClient("FAIL excluded for this round");
                            mainGame.turnComplete(serverConnection.getNickname());
                            break;
                        }

                        // SKIP
                        if (text.startsWith("SKIP", 10) && text.length() == 14) {
                            if (rulesCheck.checkCard(text, serverConnection.getNickname()) != null) {
                                serverConnection.sendToClient("CARD " + text.substring(5, 9));
                                serverConnection.sendToClient("FAIL excluded for this hand");
                                mainGame.turnComplete(serverConnection.getNickname());
                            }
                            break;
                        }

                        if (text.length() > 18) {

                            String playerName = serverConnection.getNickname();
                            logger.debug("Player nickname: " + playerName);
                            Player player = mainGame.getPlayer(playerName);
                            logger.debug("Player: " + player);
                            String toCheckMove = rulesCheck
                                    .checkCard(text, serverConnection.getNickname());
                            if (toCheckMove == null) {
                                serverConnection.sendToClient("FAIL Invalid card or no hand");
                                serverConnection.sendToClient("TURN");
                                logger.debug("You don't have this card on your hand");
                                return;
                            }

                            // special cases (move command syntax different from normal)
                            String card = toCheckMove.substring(0, 4);
                            UpdateClient returnValue;
                            String winners;
                            switch (card) {
                                case "SEVE":
                                    returnValue = rulesCheck
                                            .checkMoveSeven(toCheckMove, serverConnection.
                                                    getNickname());
                                    switch (returnValue.getReturnValue()) {
                                        case 0:
                                            sendMovesToParticipants(returnValue);
                                            winners = returnValue.getWinners();
                                            if (winners != null) {
                                                mainGame.sendMessageToParticipants("VICT " + winners);
                                                Server.getInstance()
                                                        .storeGame(mainGame.getGameId(), winners);
                                            }
                                            break;
                                        case 1:
                                            serverConnection.sendToClient("FAIL At least one invalid"
                                                    + " destination or piece!");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 2:
                                            serverConnection
                                                    .sendToClient("FAIL You moved more than 7!");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 3:
                                            serverConnection
                                                    .sendToClient("FAIL You can't jump over your own"
                                                            + "pieces!");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 4:
                                            serverConnection
                                                    .sendToClient("FAIL You don't move a total of 7!");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 5:
                                            serverConnection
                                                    .sendToClient("FAIL wrong format for seven");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                    }
                                    break;
                                case "JACK":
                                    returnValue = rulesCheck
                                            .checkMoveJack(toCheckMove, serverConnection.
                                                    getNickname());
                                    switch (returnValue.getReturnValue()) {
                                        case 0:
                                            sendMovesToParticipants(returnValue);
                                            winners = returnValue.getWinners();
                                            if (winners != null) {
                                                mainGame.sendMessageToParticipants("VICT " + winners);
                                                Server.getInstance()
                                                        .storeGame(mainGame.getGameId(), winners);
                                            }
                                            break;
                                        case 1:
                                            serverConnection.sendToClient("FAIL You have not finished");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 2:
                                            serverConnection
                                                    .sendToClient("FAIL You cannot move this color");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 3:
                                            serverConnection.sendToClient("FAIL You can't switch this"
                                                    + " pieces!");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 4:
                                            serverConnection.sendToClient("FAIL wrong format for jack");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                    }
                                    break;
                                default:
                                    returnValue = rulesCheck.checkMove(toCheckMove, serverConnection.
                                            getNickname());
                                    switch (returnValue.getReturnValue()) {
                                        case 0:
                                            sendMovesToParticipants(returnValue);
                                            winners = returnValue.getWinners();
                                            if (winners != null) {
                                                mainGame.sendMessageToParticipants("VICT " + winners);
                                                Server.getInstance()
                                                        .storeGame(mainGame.getGameId(), winners);
                                            }
                                            break;
                                        case 1:
                                            serverConnection
                                                    .sendToClient("FAIL You can't move a piece in"
                                                            + " home");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 2:
                                            serverConnection.sendToClient("FAIL Format exception in"
                                                    + " checkMove");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 3:
                                            serverConnection.sendToClient("FAIL You have not finished");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 4:
                                            serverConnection
                                                    .sendToClient("FAIL You cannot move this color");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 5:
                                            serverConnection
                                                    .sendToClient("FAIL Check your move's validity");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 6:
                                            serverConnection.sendToClient("FAIL Someone blocks you");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 7:
                                            serverConnection
                                                    .sendToClient("FAIL You eliminate yourself!");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                        case 8:
                                            serverConnection
                                                    .sendToClient("FAIL Entered command does`t fit"
                                                            + " the length(15) for checkMove()");
                                            serverConnection.sendToClient("TURN");
                                            break;
                                    }
                                    break;
                            }
                        } else {
                            serverConnection.sendToClient("FAIL MOVE command too short");
                            serverConnection.sendToClient("TURN");
                        }
                    }
                    break;

                case MOPS:
                    cheatSet(text);
                    break;

                case CTTP:
                    // TODO start CTTP
                    //change cards
                    break;

                case PCHT:
                    mainGame.sendMessageToParticipants(
                            "PCHT " + "<" + serverConnection.getNickname() + "> " + text.substring(5));
                    break;
            }
        } catch (NullPointerException e) {
            System.err.println("Received unknown message from client: " + text);
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
            System.err.println("Move sent to participants: " + move);
        }
        // send new board state
        mainGame.sendMessageToParticipants(sendMovesToParticipants.getUpdateGame()[0]);
        // eliminate card from hand
        serverConnection.sendToClient(sendMovesToParticipants.getUpdateGame()[1]);
        // send actual hand
        mainGame.sendMessageToParticipants(sendMovesToParticipants.getUpdateGame()[2]);
        // send turn completed
        mainGame.turnComplete(serverConnection.getNickname());
    }

    /**
     * Makes a move cheating, bypassing the rules checks.
     * @param text given command / move
     */
    private void cheatSet(String text) {
        try {
            Player player = mainGame.getPlayerForAlliance(text.substring(5, 9), mainGame);
            int pieceID = Integer.parseInt(text.substring(10, 11));
            String newPosition1 = text.substring(12, 13);
            int newPosition2 = Integer.parseInt(text.substring(13));
            Piece piece = player.getPiece(pieceID);
            // update gameState
            player.changePositionServer(pieceID, newPosition1, newPosition2);
            // updates piecesOnTrack in gameState
            mainGame.getGameState().updatePiecesOnTrack(piece, newPosition1);
            // send move
            mainGame.sendMessageToParticipants("MOVE " + text.substring(5));
            // send new board state
            mainGame.sendMessageToParticipants("BORD");
            // check for winner
            if (mainGame.getGameState().checkForVictory()) {
                String winners = mainGame.getGameState().getWinners();
                if (winners != null) {
                    mainGame.sendMessageToParticipants("VICT " + winners);
                    Server.getInstance().storeGame(mainGame.getGameId(), winners);
                }
            }
        } catch (Exception e) {
            System.err.println("Received unknown message from client: " + text);
        }
    }
}
