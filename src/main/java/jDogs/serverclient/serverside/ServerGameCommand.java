package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.Alliance_4;
import jDogs.ClientGame;
import jDogs.player.Piece;
import jDogs.player.Player;
import jDogs.serverclient.clientside.ClientMenuCommand;
import jDogs.serverclient.helpers.Queuejd;
import java.lang.reflect.Array;
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
    private String actualGame;
    private GameFile gameFile;
    private GameState gameState;
    private static final Logger logger = LogManager.getLogger(ServerGameCommand.class);
    private ArrayList<Player> players;

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
        this.players = null;
    }

    public void execute(String text) {
        logger.debug("Entered ServerGameCommand with: " + text);
        String command = text.substring(0,4);

        switch(command) {
            case "QUIT":
                // TODO stop ServerConnection and Client
            case "EXIT":
                // TODO startExit();
                //finish game
                break;
            case "MOVE":

                String playerName = serverConnection.getNickname();
                logger.debug("Player nickname: " + playerName);
                Player player = gameState.getPlayer(playerName);
                logger.debug("Player: " + player);
                String toCheckMove = checkCard(player, text);
                if(toCheckMove == null){
                    //return
                    logger.debug("Invalid card");
                    return;
                }
                //String card = text.substring(5, 9);
                String card = toCheckMove.substring(5, 9);

                // special cases (move command syntax different from normal)

                switch (card) {
                    // TODO substitute sub 5 with new command from checkCard()
                    case "SEVE":
                        checkMoveSeven(text.substring(5)); // TODO difference between mod 0 and 1 (teams or not)
                        break;
                    case "JACK":
                        checkMoveJack(text.substring(5));
                        break;
                    case "SURR":
                        // TODO eliminate player for round
                        break;
                    default:
                        checkMove(text.substring(5));
                }
                // Server: give it to GameEngine if move is according to the rules
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

            case "PCHT":
                //send message to everyone logged in, in lobby, separated or playing

                sendToAll.enqueue("PCHT " + "<" + nickname + "> " + text.substring(5));
                break;
        }
    }

    /**
     * Checks if the given card is in the players hand
     * @param player
     * @param text
     * @return
     */
    private String checkCard(Player player, String text){
        String card = text.substring(5, 9);
        logger.debug("Card in checkCard: " + card);
        String toCheckMove = null;
        ArrayList<String> deck = player.getDeck();
        logger.debug("Player's hand: " + deck);
        if(deck.contains(card)){
            switch (card){
                case "JOKE":
                    String value = text.substring(10,14);
                    toCheckMove = "MOVE" + " " + value + " " + text.substring(15);
                    break;
                case "JACK":
                    String piece1 = text.substring(10,16);
                    String piece2 = text.substring(17,22);
                    toCheckMove = "MOVE" + " " + piece1 + " " + piece2;
                default:
                    toCheckMove =  "MOVE" + " " + card + " " + text.substring(10);
            }
        }
        return toCheckMove;

    }


    /**
     * Checks:
     * <li>if for the given card you can go to the desired position</li>
     * <li>if somebody is eliminated by the action</li>
     * @param completeMove card piece destination
     */
    private void checkMove(String completeMove) { // TWOO YELO-1 B04
        String card = completeMove.substring(0, 4);
        String alliance = completeMove.substring(5, 9);
        int pieceID = Integer.parseInt(completeMove.substring(10, 11));
        String newPosition1 = completeMove.substring(12, 13);
        int newPosition2 = Integer.parseInt(completeMove.substring(13));
        String actualPosition1 = "";
        int actualPosition2 = -1;
        boolean hasMoved = false;
        int startingPosition = -1;
        Player ownPlayer = null;
        Alliance_4 alliance4;

        if (newPosition1.equals("A")) {
            sendToThisClient.enqueue("INFO You can't move a piece in home.");
            return;
        }

        alliance4 = convertAlliance(alliance);

        for (Player player : players) {
            if (player.getAlliance() == alliance4) {
                logger.debug("Alliance Player: " + player.getAlliance());
                ownPlayer = player;
                actualPosition1 = player.recivePosition1Server(pieceID);
                logger.debug("actual position1: " + actualPosition1);
                actualPosition2 = player.recivePosition2Server(pieceID);
                logger.debug("actual position2: " + actualPosition2);
                hasMoved = player.reciveHasMoved(pieceID);
                startingPosition = player.getStartingPosition();
            }
        }

        // if card not ok with destination, return to client
        if (!checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved)) {
            sendToThisClient.enqueue("INFO Check the card value with your desired destination");
            return;
        }

        // TODO no block going heaven or passing track

        // check if there is a piece on destination
        if (!checkWhichMove(ownPlayer, pieceID, newPosition1, newPosition2)) {
            sendToThisClient.enqueue("You eliminate yourself!");
        }

        // TODO eliminate played card in simpleMove()
    }

    /**
     * Checks if newPosition is ok with played card
     */
    private boolean checkCardWithNewPosition(String card, String actualPosition1,
            int actualPosition2, String newPosition1, int newPosition2, int startingPosition,
            boolean hasMoved) {
        int[] cardValues = getCardValues(card);
        if (cardValues == null) {
            return false;
        }
        if (actualPosition1.equals("A") && newPosition1.equals("B")) {
            // you play an exit card and you exit on your starting position
            return (card.equals("ACE1") || card.equals("AC11") || card.equals("KING"))
                    && newPosition2 == startingPosition;
        } else if (actualPosition1.equals("B") && newPosition1.equals("B")) {
            // continue on track
            int difference = newPosition2 - actualPosition2;
            // sets the difference to a possible card value
            if (difference < 0) {
                difference += 64;
            }
            for (int cardValue : cardValues) {
                if (cardValue == difference) {
                    return true;
                }
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // go heaven
            int difference;
            if (!hasMoved) {
                return false;
            } else if (card.equals("FOUR")) {
                for (int cardValue : cardValues) {
                    if (cardValue == 4) {
                        difference = startingPosition - actualPosition2;
                        if (difference < 0) {
                            difference = difference + 64 + newPosition2 + 1;
                        } else {
                            difference = difference + newPosition2 + 1;
                        }
                        return cardValue == difference;
                    } else if (cardValue == -4) {
                        difference = startingPosition - actualPosition2 - newPosition2 - 1;
                        return cardValue == difference;
                    }
                }
            } else {
                difference = startingPosition - actualPosition2;
                if (difference < 0) {
                    difference = difference + 64 + newPosition2 + 1;
                } else {
                    difference = difference + newPosition2 + 1;
                }
                for (int cardValue : cardValues) {
                    return cardValue == difference;
                }
            }
        } else if (actualPosition1.equals("C") && newPosition1.equals("C")) {
            return card.equals("ACE1") || card.equals("TWOO") || card.equals("THRE");
        }
        return false;
    }

    private int[] getCardValues(String card) {
        int[] possibleValues;
        switch(card) {
            case "ACE1":
                possibleValues = new int[]{1};
                break;
            case "AC11":
                possibleValues = new int[]{11};
                break;
            case "TWOO":
                possibleValues = new int[]{2};
                break;
            case "THRE":
                possibleValues = new int[]{3};
                break;
            case "FOUR":
                possibleValues = new int[]{4, -4};
                break;
            case "FIVE":
                possibleValues = new int[]{5};
                break;
            case "SIXX":
                possibleValues = new int[]{6};
                break;
            case "EIGT":
                possibleValues = new int[]{8};
                break;
            case "NINE":
                possibleValues = new int[]{9};
                break;
            case "TENN":
                possibleValues = new int[]{10};
                break;
            case "QUEN":
                possibleValues = new int[]{12};
                break;
            case "KING":
                possibleValues = new int[]{13};
                break;
            default:
                sendToThisClient.enqueue("Invalid card!");
                return null;
        }
        return possibleValues;
    }

    /**
     * Checks move when card JACK is played
     * @param twoPieces pieces to switch position
     */
    private void checkMoveJack(String twoPieces) { // JACK YELO-1 BLUE-2
        String ownAlliance = twoPieces.substring(5, 9);
        int ownPieceID = Integer.parseInt(twoPieces.substring(10, 11));
        String ownActualPosition1 = "";
        int ownActualPosition2 = -1;
        Player ownPlayer = null;
        Alliance_4 ownAlliance4;

        String otherAlliance = twoPieces.substring(12, 16);
        int otherPieceID = Integer.parseInt(twoPieces.substring(17));
        String otherActualPosition1 = "";
        int otherActualPosition2 = -1;
        boolean otherHasMoved = false;
        int otherStartingPosition = -1;
        Player otherPlayer = null;
        Alliance_4 otherAlliance4;

        ownAlliance4 = convertAlliance(ownAlliance);

        for (Player player : gameState.getPlayersState()) {
            if (player.getAlliance() == ownAlliance4) {
                ownPlayer = player;
                ownActualPosition1 = player.recivePosition1Server(ownPieceID);
                ownActualPosition2 = player.recivePosition2Server(ownPieceID);
            }
        }

        otherAlliance4 = convertAlliance(otherAlliance);

        for (Player player : gameState.getPlayersState()) {
            if (player.getAlliance() == otherAlliance4) {
                otherPlayer = player;
                otherActualPosition1 = player.recivePosition1Server(otherPieceID);
                otherActualPosition2 = player.recivePosition2Server(otherPieceID);
                otherHasMoved = player.reciveHasMoved(otherPieceID);
                otherStartingPosition = player.getStartingPosition();
            }
        }

        if (ownActualPosition1.equals("A") || otherActualPosition1.equals("A")
                || ownActualPosition1.equals("C") || otherActualPosition1.equals("C")
                || (otherActualPosition1.equals("B") && otherActualPosition2 == otherStartingPosition
                && !otherHasMoved)) {
            sendToThisClient.enqueue("INFO You can't switch this pieces!");
        } else {
            assert ownPlayer != null;
            simpleMove(ownPlayer, ownPieceID, otherActualPosition1, otherActualPosition2);
            assert otherPlayer != null;
            simpleMove(otherPlayer, otherPieceID, ownActualPosition1, ownActualPosition2);
        }
    }

    private Alliance_4 convertAlliance(String allianceString) {
        Alliance_4 alliance;
        switch(allianceString) {
            case "YELO":
                alliance = Alliance_4.YELLOW;
                break;
            case "REDD":
                alliance = Alliance_4.RED;
                break;
            case "BLUE":
                alliance = Alliance_4.BLUE;
                break;
            case "GREN":
                alliance = Alliance_4.GREEN;
                break;
            default:
                // if command piece not correct, return to client
                sendToThisClient.enqueue("INFO Piece isn't entered correctly");
                return null;
        }
        return alliance;
    }

    private void checkMoveSeven(String completeMove) { // SEVE 2 YELO-1 B20 GREN-2 C01
        int piecesToMove = Integer.parseInt(completeMove.substring(5, 6));
        int startIndex = 7;
        int countToSeven = 0;
        int moveValue;
        for (int i = 0; i < piecesToMove; i++) {
            moveValue = checkSingleSeven(completeMove.substring(startIndex, startIndex + 10));
            if (moveValue < 0) {
                sendToThisClient.enqueue("INFO At least one invalid destination!");
                return;
            }
            countToSeven += moveValue;
            if (countToSeven > 7) {
                sendToThisClient.enqueue("INFO You moved more than 7!");
                return;
            }
            startIndex += 11;
        }

    }

    private int checkSingleSeven(String move) { // YELO-1 B20
        String alliance = move.substring(0, 4);
        int pieceID = Integer.parseInt(move.substring(5, 6));
        String newPosition1 = move.substring(7, 8);
        int newPosition2 = Integer.parseInt(move.substring(8));
        String actualPosition1 = "";
        int actualPosition2 = -1;
        boolean hasMoved = false;
        int startingPosition = -1;
        Player ownPlayer = null;
        Alliance_4 alliance4;

        alliance4 = convertAlliance(alliance);

        for (Player player : players) {
            if (player.getAlliance() == alliance4) {
                logger.debug("Alliance Player: " + player.getAlliance());
                ownPlayer = player;
                actualPosition1 = player.recivePosition1Server(pieceID);
                logger.debug("actual position1: " + actualPosition1);
                actualPosition2 = player.recivePosition2Server(pieceID);
                logger.debug("actual position2: " + actualPosition2);
                hasMoved = player.reciveHasMoved(pieceID);
                startingPosition = player.getStartingPosition();
            }
        }

        if (actualPosition1.equals("B") && newPosition1.equals("B")
                || (actualPosition1.equals("C") && newPosition1.equals("C"))) {
            // track -> track or heaven -> heaven
            return newPosition2 - actualPosition2;
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            int difference;
            // track -> heaven
            if (!hasMoved) {
                return -1;
            }
            difference = startingPosition - actualPosition2;
            if (difference < 0) {
                difference = difference + 64 + newPosition2 + 1;
            } else {
                difference = difference + newPosition2 + 1;
            }
            return difference;
        }
        return -1;
    }

    /**
     * Checks if on the destination is already a piece
     * @return
     */
    private boolean checkWhichMove(Player player, int pieceID, String newPosition1,
            int newPosition2) {
        Piece pieceOnNewPosition = gameState.newPositionOccupied(player, newPosition1, newPosition2);
        if (pieceOnNewPosition != null) {
            if (player.getAlliance() == pieceOnNewPosition.getPieceAlliance()) {
                return false;
            } else {
                attackMove(player, pieceID, newPosition1, newPosition2, pieceOnNewPosition);
            }
        }
        simpleMove(player, pieceID, newPosition1, newPosition2);
        return true;
    }

    /**
     * Move a piece and eliminate enemy
     */
    private void attackMove(Player player, int pieceID, String newPosition1, int newPosition2,
            Piece toEliminate) {
        simpleMove(player, pieceID, newPosition1, newPosition2);
        eliminatePiece(toEliminate);
        // card is eliminated in simpleMove()
    }

    /**
     * Move a piece without eliminating any
     */
    private void simpleMove(Player player, int pieceID, String newPosition1, int newPosition2) {
        // updates piece position server
        player.changePositionServer(pieceID, newPosition1, newPosition2);

        // updates piecesOnTrack in gameState
        Piece piece = player.getPiece(pieceID);
        gameState.updatePiecesOnTrack(piece, newPosition1);

        String pieceAlliance = "";
        switch(piece.getPieceAlliance()) {
            case YELLOW:
                pieceAlliance = "YELO";
                break;
            case GREEN:
                pieceAlliance = "GREN";
                break;
            case BLUE:
                pieceAlliance = "BLUE";
                break;
            case RED:
                pieceAlliance = "REDD";
                break;
        }
        // updates client side
        gameFile.sendMessageToParticipants("MOVE " + pieceAlliance + "-" + pieceID + newPosition1
                + newPosition2);

        // TODO eliminate card!!
    }

    private void eliminatePiece(Piece piece) {
        int pieceID = piece.getPieceID();
        String newPosition1 = "A";
        int newPosition2 = pieceID - 1;

        piece.setPositionServer(newPosition1, newPosition2);
        gameState.updatePiecesOnTrack(piece, "A");

        String pieceAlliance = "";
        switch(piece.getPieceAlliance()) {
            case YELLOW:
                pieceAlliance = "YELO";
                break;
            case GREEN:
                pieceAlliance = "GREN";
                break;
            case BLUE:
                pieceAlliance = "BLUE";
                break;
            case RED:
                pieceAlliance = "REDD";
                break;
        }
        // updates client side
        gameFile.sendMessageToParticipants("MOVE " + pieceAlliance + "-" + pieceID + newPosition1
                + newPosition2);
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

    public void setGameFile(GameFile gameFile) {
        this.gameFile = gameFile;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }
}
