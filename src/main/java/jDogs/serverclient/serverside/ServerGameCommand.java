package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Player;
import jDogs.serverclient.helpers.Queuejd;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * ServerGameCommand contains the game
 * commands which are sent from the
 * clients to communicate with the
 * server.
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
            case "MOVE": // MOVE SEVE 2 YELO-1 B23 YELO-2 B50
                // if checkCard() == true, continue
                // checkMove();
                String card = text.substring(5, 9);
                switch(card) {
                    case "SEVE":
                        int piecesToMove = Integer.parseInt(text.substring(10, 11));
                        int startIndex = 12;
                        for (int i = 0; i < piecesToMove; i++) {
                            //simpleMove(text.substring(startIndex, startIndex + 10));
                            //startIndex += 11;
                        }
                        break;
                    case "JACK":
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
                for (int i = 0; i < gameFile.getscArrayList().size(); i++) {
                    gameFile.getscArrayList().get(i).getSender()
                            .sendStringToClient("LCHT " + "<" + nickname + "> " + text.substring(5));
                }
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
        Alliance_4 alliance4 = null;
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
                actualPosition1 = player.recivePosition1Server(pieceID);
                actualPosition2 = player.recivePosition2Server(pieceID);
                hasMoved = player.reciveHasMoved(pieceID);
                startingPosition = player.getStartingPosition();
            }
        }

        // if card not ok with destination, return to client
        if (!checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition)) {
            sendToThisClient.enqueue("Check the card value with your desired destination");
            return;
        }

        // check if there are pieces between actualPosition and newPosition (if on track)
        if (!checkWhichMove()) {
            sendToThisClient.enqueue("You eliminate yourself!");
            return;
        }


    }

    /**
     * Checks if newPosition - actualPosition is ok with played card
     */
    private boolean checkCardWithNewPosition(String card, String actualPosition1,
            int actualPosition2, String newPosition1, int newPosition2, int startingPosition) {
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
            return false;
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // go heaven
            int difference = startingPosition - actualPosition2 + newPosition2 + 1;
            // sets the difference to a possible card value
            if (difference < 0) {
                difference += 64;
            }
            for (int cardValue : cardValues) {
                if (cardValue == difference) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private int[] getCardValues(String card) {
        int[] possibleValues;
        switch(card) {
            case "ACE1":
                break;
            case "AC11":
                break;
            case "TWOO":
                break;
            case "THRE":
                break;
            case "FOUR":
                break;
            case "FIVE":
                break;
            case "SIXX":
                break;
            case "SEVE":
                //int piecesToMove = Integer.parseInt(text.substring(10, 11));
                //int startIndex = 12;
                //for (int i = 0; i < piecesToMove; i++) {
                    //simpleMove(text.substring(startIndex, startIndex + 10));
                    //startIndex += 11;
                //}
                break;
            case "EIGT":
                break;
            case "NINE":
                break;
            case "TENN":
                break;
            case "JACK":
                break;
            case "QUEN":
                break;
            case "KING":
                break;
            case "JOKE":
                break;
            default:
                sendToThisClient.enqueue("Invalid card!");
                return null;
        }
        return new int[0];//possibleValues;
    }

    private boolean checkWhichMove() {
        // TODO not kill yourself; no block going heaven of passing track
        return false;
    }

    /**
     * Move a piece without changing position of a third piece of eliminating someone
     * @param pieceAndDestination substring of MOVE command, example: YELO-1 B23
     */
    private void simpleMove(String pieceAndDestination) {
        String alliance = pieceAndDestination.substring(0,4);
        int pieceID = Integer.parseInt(pieceAndDestination.substring(5, 6));
        String newPosition = pieceAndDestination.substring(7);
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
                throw new IllegalStateException("Unexpected value: " + alliance);
        }
        for (Player player : gameState.playersState) {
            if (player.getAlliance() == alliance4) {
                player.changePositionServer(pieceID, newPosition);
            }
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
}
