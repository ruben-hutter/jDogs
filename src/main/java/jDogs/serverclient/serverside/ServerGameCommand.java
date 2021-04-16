package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.Alliance_4;
import jDogs.ClientGame;
import jDogs.player.Piece;
import jDogs.player.Player;
import jDogs.serverclient.helpers.Queuejd;
import java.lang.reflect.Array;
import java.util.ArrayList;

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
                // if String test = checkCard("MOVE JOKE TWOO YELO-1 B20") MOVE TWOO YEL...

                // special cases (move command syntax different from normal)
                String card = text.substring(5, 9);
                switch (card) {
                    case "SEVE":
                        int piecesToMove = Integer.parseInt(text.substring(10, 11));
                        int startIndex = 12;
                        for (int i = 0; i < piecesToMove; i++) {
                            //simpleMove(text.substring(startIndex, startIndex + 10));
                            //startIndex += 11;
                        }
                        break;
                    case "JACK":
                        checkMoveJack(text.substring(5));
                        break;
                    case "JOKE":
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
     * Move a piece and eliminate enemy
     */
    private void attackMove(Player player, int pieceID, String newPosition1, int newPosition2,
            Piece toEliminate) {
        // TODO where to put eliminated piece exactly
    }

    /**
     * Checks if the given card is in the players hand
     * @param player
     * @param card
     * @return
     */
    private boolean checkCard(Player player, String card){
        // TODO: Get player from server connection?
        ArrayList<String> deck = player.getDeck();
        return deck.contains(card);

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
        String newPosition1 = completeMove.substring(12);
        int newPosition2 = Integer.parseInt(completeMove.substring(13));
        String actualPosition1 = "";
        int actualPosition2 = -1;
        boolean hasMoved = false;
        int startingPosition = -1;
        Player ownPlayer = null;
        Alliance_4 alliance4;
        switch(alliance) {
            case "YELO":
                alliance4 = Alliance_4.YELLOW;
                break;
            case "REDD":
                alliance4 = Alliance_4.RED;
                break;
            case "BLUE":
                alliance4 = Alliance_4.BLUE;
                break;
            case "GREN":
                alliance4 = Alliance_4.GREEN;
                break;
            default:
                // if command piece not correct, return to client
                sendToThisClient.enqueue("Piece isn't entered correctly");
                return;
        }
        for (Player player : gameState.playersState) {
            if (player.getAlliance() == alliance4) {
                ownPlayer = player;
                actualPosition1 = player.recivePosition1Server(pieceID);
                actualPosition2 = player.recivePosition2Server(pieceID);
                hasMoved = player.reciveHasMoved(pieceID);
                startingPosition = player.getStartingPosition();
            }
        }

        // if card not ok with destination, return to client
        if (!checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved)) {
            sendToThisClient.enqueue("Check the card value with your desired destination");
            return;
        }

        // check if there is a piece on destination
        if (!checkWhichMove(ownPlayer, pieceID, newPosition1, newPosition2)) {
            sendToThisClient.enqueue("You eliminate yourself!");
        }

        // TODO eliminate played card
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
            return (card.equals("ACE1") || card.equals("AC11") || card.equals("KING")
                    || card.equals("JOKE")) && newPosition2 == startingPosition;
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
        boolean ownHasMoved = false;
        int ownStartingPosition = -1;
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

        switch(ownAlliance) {
            case "YELO":
                ownAlliance4 = Alliance_4.YELLOW;
                break;
            case "REDD":
                ownAlliance4 = Alliance_4.RED;
                break;
            case "BLUE":
                ownAlliance4 = Alliance_4.BLUE;
                break;
            case "GREN":
                ownAlliance4 = Alliance_4.GREEN;
                break;
            default:
                // if command piece not correct, return to client
                sendToThisClient.enqueue("Piece isn't entered correctly");
                return;
        }
        for (Player player : gameState.playersState) {
            if (player.getAlliance() == ownAlliance4) {
                ownPlayer = player;
                ownActualPosition1 = player.recivePosition1Server(ownPieceID);
                ownActualPosition2 = player.recivePosition2Server(ownPieceID);
                ownHasMoved = player.reciveHasMoved(ownPieceID);
                ownStartingPosition = player.getStartingPosition();
            }
        }

        switch(otherAlliance) {
            case "YELO":
                otherAlliance4 = Alliance_4.YELLOW;
                break;
            case "REDD":
                otherAlliance4 = Alliance_4.RED;
                break;
            case "BLUE":
                otherAlliance4 = Alliance_4.BLUE;
                break;
            case "GREN":
                otherAlliance4 = Alliance_4.GREEN;
                break;
            default:
                // if command piece not correct, return to client
                sendToThisClient.enqueue("Piece isn't entered correctly");
                return;
        }
        for (Player player : gameState.playersState) {
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
            sendToThisClient.enqueue("You can't switch this pieces!");
        } else {
            assert ownPlayer != null;
            simpleMove(ownPlayer, ownPieceID, otherActualPosition1, otherActualPosition2);
            assert otherPlayer != null;
            simpleMove(otherPlayer, otherPieceID, ownActualPosition1, ownActualPosition2);
        }
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

    // TODO no block going heaven or passing track

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
        // TODO test if the right message is given to the client
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
