package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Piece;
import jDogs.player.Player;
import jDogs.serverclient.helpers.Queuejd;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RulesCheck {

    private static final Logger logger = LogManager.getLogger(RulesCheck.class);
    private final Queuejd sendToThisClient;
    private String cardToEliminate;
    Alliance_4 alliance4;
    private GameState gameState;
    private GameFile gameFile;
    private MainGame mainGame;

    public RulesCheck(Queuejd sendToThisClient) {
        this.sendToThisClient = sendToThisClient;
    }

    /**
     * Checks if the given card is in the players hand
     * @param text MOVE command from user
     * @return null if invalid card or the move (with translation if JOKE)
     */
    protected String checkCard(String text, GameState gameState, String nickname) {
        String card = text.substring(5, 9);
        if (card.equals("ACE1") || card.equals("AC11")) {
            card = "ACEE";
        }
        logger.debug("Card in checkCard: " + card);
        String toCheckMove = null;
        ArrayList<String> hand = gameState.getCards().get(nickname);

        if (hand == null) {
            return null;
        }

        logger.debug("Player's hand: " + hand);
        logger.debug("Deck contains card? " + hand.contains(card));
        if (hand.contains(card)) {
            switch (card) {
                case "JOKE":
                    String value = text.substring(10, 14);
                    toCheckMove = value + " " + text.substring(15);
                    logger.debug(
                            "This string is send to checkMove (without Joker): " + toCheckMove);
                    break;
                case "ACEE":
                    String ass = text.substring(5, 9);
                    cardToEliminate = "ACEE";
                    toCheckMove = ass + " " + text.substring(10);
                    logger.debug("This string is send to checkMove (without ACEE): " + toCheckMove);
                    break;
                default:
                    toCheckMove = card + " " + text.substring(10);
                    logger.debug("This string is send to checkMove: " + toCheckMove);
            }
        }
        return toCheckMove;
    }

    /**
     * Checks:
     * if for the given card you can go to the desired position
     * if somebody is eliminated by the action
     * @param completeMove card piece destination
     */
    protected void checkMove(String completeMove, GameState gameState, GameFile gameFile,
            MainGame mainGame, String nickname) { // TWOO YELO-1 B04
        this.gameState = gameState;
        this.gameFile = gameFile;
        this.mainGame = mainGame;
        if (completeMove.length() == 15) {
            String card = null;
            int pieceID = 0;
            String newPosition1 = null;
            int newPosition2 = 0;
            String actualPosition1 = null;
            int actualPosition2 = 0;
            boolean hasMoved = false;
            int startingPosition = 0;
            Player ownPlayer = null;
            try {
                card = completeMove.substring(0, 4);
                String alliance = completeMove.substring(5, 9);
                pieceID = Integer.parseInt(completeMove.substring(10, 11));
                newPosition1 = completeMove.substring(12, 13);
                newPosition2 = Integer.parseInt(completeMove.substring(13));
                actualPosition1 = "";
                actualPosition2 = -1;
                hasMoved = false;
                startingPosition = -1;
                ownPlayer = null;

                if (newPosition1.equals("A")) {
                    sendToThisClient.enqueue("INFO You can't move a piece in home.");
                    return;
                }

                alliance4 = convertAlliance(alliance);

                for (Player player : gameState.getPlayersState()) {
                    if (player.getAlliance() == alliance4) {
                        logger.debug("Alliance Player: " + player.getAlliance());
                        ownPlayer = player;
                        actualPosition1 = player.receivePosition1Server(pieceID);
                        logger.debug("actual position1: " + actualPosition1);
                        actualPosition2 = player.receivePosition2Server(pieceID);
                        logger.debug("actual position2: " + actualPosition2);
                        hasMoved = player.receiveHasMoved(pieceID);
                        startingPosition = player.getStartingPosition();
                    }
                }
            } catch (Exception e) {
                sendToThisClient.enqueue("INFO format exception in checkMove");
                return;
            }
            //TODO find better place for this; if player finished, and teamMode on, he can play 2 colors
            //prevent players from moving with others pieces
            if (ownPlayer != gameFile.getPlayer(nickname)) {
                sendToThisClient.enqueue("INFO you cannot move this color");
                return;
            }

            // if card not ok with destination, return to client
            if (!checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                    newPosition2, startingPosition, hasMoved)) {
                sendToThisClient.enqueue("INFO Check the card value with your desired destination");
                return;
            }

            // if move passes an occupied starting position, and that piece haven't moved
            if (checkForBlock(card, actualPosition1, actualPosition2, newPosition1, newPosition2,
                    ownPlayer)) {
                sendToThisClient.enqueue("INFO Someone blocks you");
                return;
            }

            // check if there is a piece on destination
            if (!checkWhichMove(ownPlayer, pieceID, newPosition1, newPosition2)) {
                sendToThisClient.enqueue("You eliminate yourself!");
                return;
            }

            // remove block if piece moves for first time on track
            if (!hasMoved && actualPosition1.equals("B") && newPosition1.equals("B")) {
                ownPlayer.getPiece(pieceID).changeHasMoved();
            }

            gameFile.sendMessageToParticipants("BORD");
            //eliminate card
            gameState.getCards().get(nickname).remove(cardToEliminate);
            gameFile.getPlayer(nickname).sendMessageToClient("CARD " + cardToEliminate);
            gameFile.sendMessageToParticipants("HAND");

            cardToEliminate = null;
            mainGame.turnComplete(nickname);

        } else {
            sendToThisClient.enqueue("INFO entered command does`t fit the length(15) for"
                    + "checkmove()");
        }
    }

    /**
     * Checks move when card JACK is played
     * @param twoPieces pieces to switch position
     */
    protected void checkMoveJack(String twoPieces, GameState gameState, GameFile gameFile,
            MainGame mainGame, String nickname) { // JACK YELO-1 BLUE-2
        this.gameState = gameState;
        this.gameFile = gameFile;
        this.mainGame = mainGame;
        try {
            if (twoPieces.length() == 23) {
                String ownAlliance = twoPieces.substring(5, 9);
                int ownPieceID = Integer.parseInt(twoPieces.substring(10, 11));
                String ownActualPosition1 = "";
                int ownActualPosition2 = -1;
                Player ownPlayer = null;
                boolean ownHasMoved = false;
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
                        ownActualPosition1 = player.receivePosition1Server(ownPieceID);
                        ownActualPosition2 = player.receivePosition2Server(ownPieceID);
                        ownHasMoved = player.receiveHasMoved(ownPieceID);
                    }
                }

                // TODO if playing in teamMode
                if (ownPlayer != gameFile.getPlayer(nickname)) {
                    sendToThisClient.enqueue("INFO you cannot move this color");
                    return;
                }

                otherAlliance4 = convertAlliance(otherAlliance);

                for (Player player : gameState.getPlayersState()) {
                    if (player.getAlliance() == otherAlliance4) {
                        otherPlayer = player;
                        otherActualPosition1 = player.receivePosition1Server(otherPieceID);
                        otherActualPosition2 = player.receivePosition2Server(otherPieceID);
                        otherHasMoved = player.receiveHasMoved(otherPieceID);
                        otherStartingPosition = player.getStartingPosition();
                    }
                }

                if (ownActualPosition1.equals("A") || otherActualPosition1.equals("A")
                        || ownActualPosition1.equals("C") || otherActualPosition1.equals("C")
                        || (otherActualPosition1.equals("B")
                        && otherActualPosition2 == otherStartingPosition
                        && !otherHasMoved)) {
                    sendToThisClient.enqueue("INFO You can't switch this pieces!");
                } else {
                    assert ownPlayer != null;
                    simpleMove(ownPlayer, ownPieceID, otherActualPosition1, otherActualPosition2);
                    assert otherPlayer != null;
                    simpleMove(otherPlayer, otherPieceID, ownActualPosition1, ownActualPosition2);

                    // remove block if piece moves for first time on track
                    if (!ownHasMoved && ownActualPosition1.equals("B") && otherActualPosition1.equals("B")) {
                        ownPlayer.getPiece(ownPieceID).changeHasMoved();
                    }

                    gameFile.sendMessageToParticipants("BORD");
                    //eliminate card
                    gameState.getCards().get(nickname).remove(cardToEliminate);
                    gameFile.getPlayer(nickname).sendMessageToClient("CARD " + cardToEliminate);
                    gameFile.sendMessageToParticipants("HAND");

                    cardToEliminate = null;
                    mainGame.turnComplete(nickname);
                }
            }
        } catch (Exception e) {
            sendToThisClient.enqueue("INFO wrong format for jack");
        }
    }

    // TODO difference between mod 0 and 1 (teams or not)
    /**
     * Checks move when card SEVE is played
     * @param completeMove given move
     */
    protected void checkMoveSeven(String completeMove, ServerConnection serverConnection,
            String nickname) { // SEVE 2 YELO-1 B20 GREN-2 C01
        try {
            int piecesToMove = Integer.parseInt(completeMove.substring(5, 6));
            int startIndex = 7;
            int countToSeven = 0;
            int moveValue;
            ArrayList<Piece> piecesToEliminate = new ArrayList<>();
            for (int i = 0; i < piecesToMove; i++) {
                moveValue = checkSingleSeven(completeMove.substring(startIndex, startIndex + 10));
                if (moveValue < 0) {
                    sendToThisClient.enqueue("INFO At least one invalid destination or piece!");
                    return;
                }
                countToSeven += moveValue;
                if (countToSeven > 7) {
                    sendToThisClient.enqueue("INFO You moved more than 7!");
                    return;
                }
                ArrayList<Piece> singleEliminations = piecesOnPath(completeMove.substring(startIndex,
                        startIndex + 10));
                if (singleEliminations == null) {
                    sendToThisClient.enqueue("INFO You can't jump over your pieces in heaven!");
                    return;
                }
                piecesToEliminate.addAll(singleEliminations);
                startIndex += 11;
            }

            if (countToSeven == 7) {
                startIndex = 7;
                Player player;
                for (int i = 0; i < piecesToMove; i++) {
                    String move = completeMove.substring(startIndex, startIndex + 10);
                    // TODO change if teamMode on
                    player = gameFile.getPlayer(serverConnection.getNickname());
                    int pieceID = Integer.parseInt(move.substring(5, 6));
                    String newPosition1 = move.substring(7, 8);
                    int newPosition2 = Integer.parseInt(move.substring(8));
                    Piece piece = player.getPiece(pieceID);
                    String actualPosition1 = player.receivePosition1Server(pieceID);
                    int actualPosition2 = player.receivePosition2Server(pieceID);
                    /*
                    if (checkForBlock(card, actualPosition1, actualPosition2, newPosition1, newPosition2,
                            player)) {
                        sendToThisClient.enqueue("INFO There is a block, you can't pass!");
                        return;
                    }
                     */
                    simpleMove(player, pieceID, newPosition1, newPosition2);
                    // remove block if piece moves for first time on track
                    if (!piece.getHasMoved() && piece.getPositionServer1().equals("B")
                            && newPosition1.equals("B")) {
                        piece.changeHasMoved();
                    }
                    startIndex += 11;
                }
                for (Piece piece : piecesToEliminate) {
                    eliminatePiece(piece);
                }
            }

            gameFile.sendMessageToParticipants("BORD");
            //eliminate card
            gameState.getCards().get(nickname).remove(cardToEliminate);
            gameFile.getPlayer(nickname).sendMessageToClient("CARD " + cardToEliminate);
            gameFile.sendMessageToParticipants("HAND");

            cardToEliminate = null;
            mainGame.turnComplete(nickname);
        } catch (Exception e) {
            sendToThisClient.enqueue("INFO wrong format for seven");
        }
    }

    /**
     * Checks one of the moves given with card SEVE
     * @param move only one "normal" move
     * @return -1 if not legal or card value
     */
    private int checkSingleSeven(String move) { // YELO-1 B20
        try {
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
                    actualPosition1 = player.receivePosition1Server(pieceID);
                    logger.debug("actual position1: " + actualPosition1);
                    actualPosition2 = player.receivePosition2Server(pieceID);
                    logger.debug("actual position2: " + actualPosition2);
                    hasMoved = player.receiveHasMoved(pieceID);
                    startingPosition = player.getStartingPosition();
                }
            }

            // TODO if teamMode
            if (ownPlayer != gameFile.getPlayer(nickname)) {
                return -1;
            }

            int difference;
            if (actualPosition1.equals("B") && newPosition1.equals("B")
                    || (actualPosition1.equals("C") && newPosition1.equals("C"))) {
                // track -> track or heaven -> heaven
                difference = Math.floorMod(newPosition2 - actualPosition2, 64);
                return difference;
            } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
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
        } catch (Exception e) {
            return -1;
        }
    }

    private ArrayList<Piece> piecesOnPath(String move) {
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
        ArrayList<Piece> piecesToEliminate = new ArrayList<>();

        alliance4 = convertAlliance(alliance);

        for (Player player : players) {
            if (player.getAlliance() == alliance4) {
                logger.debug("Alliance Player: " + player.getAlliance());
                ownPlayer = player;
                actualPosition1 = player.receivePosition1Server(pieceID);
                logger.debug("actual position1: " + actualPosition1);
                actualPosition2 = player.receivePosition2Server(pieceID);
                logger.debug("actual position2: " + actualPosition2);
                hasMoved = player.receiveHasMoved(pieceID);
                startingPosition = player.getStartingPosition();
            }
        }

        int difference;
        if (actualPosition1.equals("B") && newPosition1.equals("B")) {
            // track -> track
            difference = newPosition2 - actualPosition2;
            if (difference < 0) {
                difference += 64;
            }
            for (int i = 1; i <= difference; i++) {
                Piece pieceOnPath = gameState.newPositionOccupied(ownPlayer, actualPosition1,
                        (actualPosition2 + i) % 64);
                if (pieceOnPath != null && pieceOnPath.getPieceAlliance() != ownPlayer.getAlliance()) {
                    piecesToEliminate.add(pieceOnPath);
                }
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // track -> heaven
            difference = startingPosition - actualPosition2;
            if (difference < 0) {
                difference += 64;
            }
            for (int i = 1; i <= difference; i++) {
                Piece pieceOnPath = gameState.newPositionOccupied(ownPlayer, actualPosition1,
                        (actualPosition2 + i) % 64);
                if (pieceOnPath != null && pieceOnPath.getPieceAlliance() != ownPlayer.getAlliance()) {
                    piecesToEliminate.add(pieceOnPath);
                }
            }
            for (int i = 0; i <= newPosition2; i++) {
                Piece pieceOnPath = gameState.newPositionOccupied(ownPlayer, newPosition1, i);
                if (pieceOnPath != null) {
                    return null;
                }
            }
        } else if (actualPosition1.equals("C") && newPosition1.equals("C")) {
            // heaven -> heaven
            for (int i = actualPosition2; i <= newPosition2; i++) {
                Piece pieceOnPath = gameState.newPositionOccupied(ownPlayer, newPosition1, i);
                if (pieceOnPath != null) {
                    return null;
                }
            }
        }
        return piecesToEliminate;
    }

    /**
     * Converts a String alliance to an Alliance_4 instance
     * @param allianceString alliance that you play (YELO, REDD, ...)
     * @return an Alliance_4 instance
     */
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

    /**
     * Converts an Alliance_4 instance to a String alliance
     * @param alliance4 the Alliance_4 instance
     * @return alliance in String form (YELO, REDD, ...)
     */
    private String convertAlliance(Alliance_4 alliance4) {
        String pieceAlliance;
        switch(alliance4) {
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
            default:
                throw new IllegalStateException("Unexpected value: " + alliance4);
        }
        return pieceAlliance;
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
            int difference;
            if (card.equals("FOUR")) {
                for (int cardValue : cardValues) {
                    if (cardValue == -4) {
                        difference = (newPosition2 - actualPosition2) % 64;
                        if (difference == cardValue) {
                            return true;
                        }
                    }
                }
            }
            // sets the difference to a possible card value
            difference = Math.floorMod(newPosition2 - actualPosition2, 64);
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
            }
            // TODO piecesOnPath();
            if (card.equals("FOUR")) {
                for (int cardValue : cardValues) {
                    if (cardValue == 4) {
                        difference = Math.floorMod(startingPosition - actualPosition2, 64);
                        return cardValue == difference + newPosition2 + 1;
                    } else if (cardValue == -4) {
                        difference = startingPosition - actualPosition2 - newPosition2 - 1;
                        return cardValue == difference;
                    }
                }
            } else {
                difference = Math.floorMod(startingPosition - actualPosition2, 64);
                difference += newPosition2 + 1;
                for (int cardValue : cardValues) {
                    return cardValue == difference;
                }
            }
        } else if (actualPosition1.equals("C") && newPosition1.equals("C")) {
            return card.equals("ACE1") || card.equals("TWOO") || card.equals("THRE");
        }
        return false;
    }

    /**
     * Checks if there is a block on the way to new position
     * @return true if you are blocked
     */
    private boolean checkForBlock(String card, String actualPosition1,
            int actualPosition2, String newPosition1, int newPosition2, Player player) {
        int [] startingPositions = new int[] {0, 16, 32, 48};
        Piece pieceOnStart;
        int convertActualPos;
        if (actualPosition1.equals("B") && newPosition1.equals("B")) {
            // continue on track
            // TODO four case -4
            for (int startingPosition : startingPositions) {
                if (newPosition2 > actualPosition2) {
                    if (actualPosition2 < startingPosition && startingPosition < newPosition2) {
                        pieceOnStart = gameState.newPositionOccupied(player, newPosition1,
                                startingPosition);
                        if (pieceOnStart != null && pieceOnStart.getPieceAlliance()
                                == alliance4.getAlliance(startingPosition)) {
                            if (!pieceOnStart.getHasMoved()) {
                                return true;
                            }
                        }
                    }
                } else {
                    convertActualPos = actualPosition2 - 64;
                    if (convertActualPos < startingPosition && startingPosition < newPosition2) {
                        pieceOnStart = gameState.newPositionOccupied(player, newPosition1,
                                startingPosition);
                        if (pieceOnStart != null && pieceOnStart.getPieceAlliance()
                                == alliance4.getAlliance(startingPosition)) {
                            if (!pieceOnStart.getHasMoved()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // TODO
        }
        return false;
    }

    /**
     * Checks if on the destination is already a piece
     * @return false if you eliminate yourself
     */
    private boolean checkWhichMove(Player player, int pieceID, String newPosition1,
            int newPosition2) {
        Piece pieceOnNewPosition = gameState.newPositionOccupied(player, newPosition1, newPosition2);
        if (pieceOnNewPosition != null) {
            if (player.getAlliance() == pieceOnNewPosition.getPieceAlliance()) {
                return false;
            } else {
                attackMove(player, pieceID, newPosition1, newPosition2, pieceOnNewPosition);
                return true;
            }
        }
        simpleMove(player, pieceID, newPosition1, newPosition2);
        return true;
    }

    /**
     * Gives you the int value of your String card
     * @param card String card
     * @return int value of card
     */
    private int[] getCardValues(String card) {
        int[] possibleValues;
        switch (card) {
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

        String pieceAlliance = convertAlliance(piece.getPieceAlliance());

        // updates client side
        gameFile.sendMessageToParticipants("MOVE " + pieceAlliance + "-" + pieceID + " " + newPosition1
                + newPosition2);
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
        gameFile.sendMessageToParticipants("MOVE " + pieceAlliance + "-" + pieceID + " " + newPosition1
                + newPosition2);
    }
}
