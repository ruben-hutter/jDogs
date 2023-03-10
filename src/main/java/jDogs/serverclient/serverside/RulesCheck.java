package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Alliance_4;
import jDogs.player.Piece;
import jDogs.player.Player;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RulesCheck {

    private static final Logger logger = LogManager.getLogger(RulesCheck.class);
    private String cardToEliminate;
    private final GameState gameState;
    private final MainGame mainGame;
    private final boolean teamMode;
    RulesCheckHelper rulesCheckHelper;
    private int actualCardValue;

    public RulesCheck(MainGame mainGame) {
        this.mainGame = mainGame;
        this.gameState = mainGame.getGameState();
        this.teamMode = mainGame.isTeamMode();
        rulesCheckHelper = new RulesCheckHelper(mainGame);
    }

    /**
     * Checks if the given card is in the players hand
     * @param text MOVE command from user
     * @param nickname player's nickname
     * @return null if invalid card or the move (with translation if JOKE)
     */
    protected String checkCard(String text, String nickname) {
        String card = text.substring(5, 9);
        this.cardToEliminate = card;
        logger.debug("Card in checkCard: " + card);
        String toCheckMove = null;
        ArrayList<String> hand = gameState.getCards().get(nickname);

        if (hand == null) {
            return null;
        }

        logger.debug("Player's hand: " + hand);
        logger.debug("Deck contains card? " + hand.contains(card));
        if (hand.contains(card)) {
            if ("JOKE".equals(card)) {
                String value = text.substring(10, 14);
                toCheckMove = value + " " + text.substring(15);
                logger.debug(
                        "This string is send to checkMove (without Joker): " + toCheckMove);
            } else {
                toCheckMove = card + " " + text.substring(10);
                logger.debug("This string is send to checkMove: " + toCheckMove);
            }
        }
        System.err.println("ToCheckMove: " + toCheckMove);
        return toCheckMove;
    }

    /**
     * Checks:
     * if for the given card you can go to the desired position
     * if somebody is eliminated by the action
     * @param completeMove card piece destination
     * @param nickname name of player
     * @return an object that contains a return value and, if the move is legal,
     * the moves and elimination moves to communicate to the clients
     */
    protected UpdateClient checkMove(String completeMove, String nickname) { // TWOO YELO-1 B04
        UpdateClient updateClient = new UpdateClient();
        if (completeMove.length() == 15) {
            String card;
            int pieceID;
            String newPosition1;
            int newPosition2;
            String actualPosition1;
            int actualPosition2;
            boolean hasMoved;
            int startingPosition;
            Player ownPlayer;
            int ownTeamID;
            try {
                card = completeMove.substring(0, 4);
                String alliance = completeMove.substring(5, 9);
                Alliance_4 alliance4 = rulesCheckHelper.convertAlliance(alliance);
                pieceID = Integer.parseInt(completeMove.substring(10, 11));
                PiecesActualInfo piecesActualInfo = rulesCheckHelper.getPieceInfo(pieceID,
                        alliance4, mainGame);
                newPosition1 = completeMove.substring(12, 13);
                newPosition2 = Integer.parseInt(completeMove.substring(13));
                ownPlayer = piecesActualInfo.getPlayer();
                actualPosition1 = piecesActualInfo.getActualPosition1();
                actualPosition2 = piecesActualInfo.getActualPosition2();
                hasMoved = piecesActualInfo.getHasMoved();
                startingPosition = piecesActualInfo.getStartingPosition();
                ownTeamID = piecesActualInfo.getTeamID();

                if (newPosition1.equals("A")) {
                    updateClient.setReturnValue(1);
                    return updateClient;
                }

                System.err.println("Piece's actualInfos:");
                System.err.println("- alliance4: " + alliance4);
                System.err.println("- pieceID: " + pieceID);
                System.err.println("- actualPosition1: " + actualPosition1);
                System.err.println("- actualPosition2: " + actualPosition2);
                System.err.println("- hasMoved: " + hasMoved);
                System.err.println("- teamID: " + ownTeamID);

            } catch (Exception e) {
                updateClient.setReturnValue(2);
                return updateClient;
            }

            // prevent players from moving with others pieces
            Player nowPlaying = gameState.getPlayer(nickname);
            if (teamMode) {
                if ((ownPlayer != nowPlaying && !nowPlaying.getFinished()) || (ownPlayer != nowPlaying
                        && ownTeamID != nowPlaying.getTeamID())) {
                    System.err.println("Marble owner's teamID: " + ownPlayer.getTeamID());
                    System.err.println("Playing: " + nowPlaying.getTeamID());
                    updateClient.setReturnValue(3);
                    return updateClient;
                }
            } else {
                if (ownPlayer != nowPlaying) {
                    System.err.println("Marble owner's teamID: " + ownPlayer.getTeamID());
                    System.err.println("Playing: " + nowPlaying.getTeamID());
                    updateClient.setReturnValue(4);
                    return updateClient;
                }
            }

            // if card not ok with destination, return to client
            if (!checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                    newPosition2, startingPosition, hasMoved, ownPlayer)) {
                updateClient.setReturnValue(5);
                return updateClient;
            }

            // if move passes an occupied starting position, and that piece haven't moved
            if (checkForBlock(card, actualPosition1, actualPosition2, newPosition1, newPosition2,
                    ownPlayer)) {
                updateClient.setReturnValue(6);
                return updateClient;
            }

            // check if there is a piece on destination
            if (!rulesCheckHelper.checkWhichMove(ownPlayer, pieceID, newPosition1, newPosition2,
                    updateClient)) {
                updateClient.setReturnValue(7);
                return updateClient;
            }

            rulesCheckHelper.updateGame(nickname, cardToEliminate, updateClient);

        } else {
            System.err.println("Given MOVE: " + completeMove);
            updateClient.setReturnValue(8);
            return updateClient;
        }
        updateClient.setReturnValue(0);
        return updateClient;
    }

    /**
     * Checks move when card JACK is played
     * @param completeMove pieces to switch position
     * @param nickname players name
     * @return an object that contains a return value and, if the move is legal,
     * the moves and elimination moves to communicate to the clients
     */
    protected UpdateClient checkMoveJack(String completeMove, String nickname) { // JACK YELO-1 BLUE-2
        UpdateClient updateClient = new UpdateClient();
        try {
            if (completeMove.length() == 18) {
                String ownAlliance = completeMove.substring(5, 9);
                int ownPieceID = Integer.parseInt(completeMove.substring(10, 11));
                String ownActualPosition1 = null;
                int ownActualPosition2 = -1;
                Player ownPlayer = null;
                Alliance_4 ownAlliance4 = rulesCheckHelper.convertAlliance(ownAlliance);
                int ownTeamID = -1;

                String otherAlliance = completeMove.substring(12, 16);
                int otherPieceID = Integer.parseInt(completeMove.substring(17));
                String otherActualPosition1 = null;
                int otherActualPosition2 = -1;
                boolean otherHasMoved = false;
                Player otherPlayer = null;
                Alliance_4 otherAlliance4 = rulesCheckHelper.convertAlliance(otherAlliance);

                for (Player player : gameState.getPlayers()) {
                    if (player.getAlliance() == ownAlliance4) {
                        ownPlayer = player;
                        ownActualPosition1 = player.receivePosition1Server(ownPieceID);
                        ownActualPosition2 = player.receivePosition2Server(ownPieceID);
                        ownTeamID = player.getTeamID();
                    }
                    if (player.getAlliance() == otherAlliance4) {
                        otherPlayer = player;
                        otherActualPosition1 = player.receivePosition1Server(otherPieceID);
                        otherActualPosition2 = player.receivePosition2Server(otherPieceID);
                        otherHasMoved = player.receiveHasMoved(otherPieceID);
                    }
                }

                System.err.println("First piece's actualInfos:");
                System.err.println("- alliance4: " + ownAlliance4);
                System.err.println("- pieceID: " + ownPieceID);
                System.err.println("- actualPosition1: " + ownActualPosition1);
                System.err.println("- actualPosition2: " + ownActualPosition2);
                System.err.println("- teamID: " + ownTeamID);
                System.err.println();
                System.err.println("Second piece's actualInfos:");
                System.err.println("- alliance4: " + otherAlliance4);
                System.err.println("- pieceID: " + otherPieceID);
                System.err.println("- actualPosition1: " + otherActualPosition1);
                System.err.println("- actualPosition2: " + otherActualPosition2);
                System.err.println("- hasMoved: " + otherHasMoved);

                // prevent players from moving with others pieces
                Player nowPlaying = gameState.getPlayer(nickname);
                if (teamMode) {
                    if ((ownPlayer != nowPlaying && !nowPlaying.getFinished()) || (ownPlayer !=
                            nowPlaying && ownTeamID != nowPlaying.getTeamID())) {
                        System.err.println("Marble owner's teamID: " + ownPlayer.getTeamID());
                        System.err.println("Playing: " + nowPlaying.getTeamID());
                        updateClient.setReturnValue(1);
                        return updateClient;
                    }
                } else {
                    if (ownPlayer != nowPlaying) {
                        System.err.println("Marble owner's teamID: " + ownPlayer.getTeamID());
                        System.err.println("Playing: " + nowPlaying.getTeamID());
                        updateClient.setReturnValue(2);
                        return updateClient;
                    }
                }
                assert ownActualPosition1 != null;
                assert otherActualPosition1 != null;
                if (ownActualPosition1.equals("A") || otherActualPosition1.equals("A")
                        || ownActualPosition1.equals("C") || otherActualPosition1.equals("C")
                        || !otherHasMoved) {
                    updateClient.setReturnValue(3);
                    return updateClient;
                } else {
                    rulesCheckHelper.simpleMove(ownPlayer, ownPieceID, otherActualPosition1,
                            otherActualPosition2, updateClient);
                    rulesCheckHelper.simpleMove(otherPlayer, otherPieceID, ownActualPosition1,
                            ownActualPosition2, updateClient);

                    rulesCheckHelper.updateGame(nickname, cardToEliminate, updateClient);
                }
            }
        } catch (Exception e) {
            System.err.println("Given MOVE: " + completeMove);
            updateClient.setReturnValue(4);
            return updateClient;
        }
        updateClient.setReturnValue(0);
        return updateClient;
    }

    /**
     * Checks move when card SEVE is played
     * @param completeMove given move
     * @param nickname player's name
     * @return an object that contains a return value and, if the move is legal,
     * the moves and elimination moves to communicate to the clients
     */
    protected UpdateClient checkMoveSeven(String completeMove, String nickname) {
        UpdateClient updateClient = new UpdateClient();
        try {
            int piecesToMove = Integer.parseInt(completeMove.substring(5, 6));
            int startIndex = 7;
            int countToSeven = 0;
            int moveValue;
            String move;
            String alliance;
            Alliance_4 alliance4;
            int pieceID;
            String newPosition1;
            int newPosition2;
            Player ownPlayer;
            String actualPosition1;
            int actualPosition2;
            boolean hasMoved;
            int startingPosition;
            int ownTeamID;
            PiecesActualInfo piecesActualInfo;
            ArrayList<Piece> piecesToEliminate = new ArrayList<>();
            ArrayList<Piece> singleEliminations;
            GameState tempGameState = new GameState(gameState);
            MainGame tempMainGame = tempGameState.getMainGame();
            for (int i = 0; i < piecesToMove; i++) {
                move = completeMove.substring(startIndex, startIndex + 10);
                alliance = move.substring(0, 4);
                alliance4 = rulesCheckHelper.convertAlliance(alliance);
                pieceID = Integer.parseInt(move.substring(5, 6));
                piecesActualInfo = rulesCheckHelper.getPieceInfo(pieceID, alliance4, tempMainGame);
                newPosition1 = move.substring(7, 8);
                newPosition2 = Integer.parseInt(move.substring(8));
                ownPlayer = piecesActualInfo.getPlayer();
                actualPosition1 = piecesActualInfo.getActualPosition1();
                actualPosition2 = piecesActualInfo.getActualPosition2();
                hasMoved = piecesActualInfo.getHasMoved();
                startingPosition = piecesActualInfo.getStartingPosition();
                ownTeamID = piecesActualInfo.getTeamID();

                System.err.println("Piece's actualInfos:");
                System.err.println("- alliance4: " + alliance4);
                System.err.println("- pieceID: " + pieceID);
                System.err.println("- actualPosition1: " + actualPosition1);
                System.err.println("- actualPosition2: " + actualPosition2);
                System.err.println("- hasMoved: " + hasMoved);
                System.err.println("- teamID: " + ownTeamID);

                moveValue = checkSingleSeven(nickname, newPosition1, newPosition2, ownPlayer,
                        actualPosition1, actualPosition2, hasMoved, startingPosition, ownTeamID,
                        tempGameState);
                if (moveValue < 0) {
                    updateClient.setReturnValue(1);
                    return updateClient;
                }
                countToSeven += moveValue;
                if (countToSeven > 7) {
                    System.err.println("CountToSeven: " + countToSeven);
                    updateClient.setReturnValue(2);
                    return updateClient;
                }
                singleEliminations = piecesOnPath(newPosition1, newPosition2, ownPlayer,
                        actualPosition1, actualPosition2, startingPosition, pieceID, tempGameState,
                        tempMainGame);
                if (singleEliminations == null) {
                    updateClient.setReturnValue(3);
                    return updateClient;
                }
                piecesToEliminate.addAll(singleEliminations);
                startIndex += 11;
            }

            // if you move a total of 7 correctly
            if (countToSeven == 7) {
                startIndex = 7;
                for (int i = 0; i < piecesToMove; i++) {
                    move = completeMove.substring(startIndex, startIndex + 10);
                    alliance = move.substring(0, 4);
                    alliance4 = rulesCheckHelper.convertAlliance(alliance);
                    pieceID = Integer.parseInt(move.substring(5, 6));
                    piecesActualInfo = rulesCheckHelper.getPieceInfo(pieceID, alliance4,
                            mainGame);
                    newPosition1 = move.substring(7, 8);
                    newPosition2 = Integer.parseInt(move.substring(8));
                    ownPlayer = piecesActualInfo.getPlayer();

                    rulesCheckHelper.simpleMove(ownPlayer, pieceID, newPosition1, newPosition2,
                            updateClient);
                    startIndex += 11;
                }
                for (Piece piece : piecesToEliminate) {
                    Piece pieceToEliminate = gameState.getPlayer(piece.getPieceAlliance()).
                            getPiece(piece.getPieceID());
                    rulesCheckHelper.eliminatePiece(pieceToEliminate, updateClient);
                }

                rulesCheckHelper.updateGame(nickname, cardToEliminate, updateClient);

            } else {
                System.err.println("CountToSeven: " + countToSeven);
                updateClient.setReturnValue(4);
                System.err.println("count to seven from rulescheck " + countToSeven);
                return updateClient;
            }
        } catch (Exception e) {
            System.err.println("Given MOVE: " + completeMove);
            updateClient.setReturnValue(5);
            return updateClient;
        }
        updateClient.setReturnValue(0);
        return updateClient;
    }

    /**
     * Checks one of the moves given with card SEVE
     * @param nickname player's name
     * @param newPosition1 B or C
     * @param newPosition2 int between 0-63
     * @param ownPlayer owner of this marble
     * @param actualPosition1 B or C
     * @param actualPosition2 int between 0-63
     * @param hasMoved true if the player has already moved on track
     * @param startingPosition 0, 16, 32 or 48
     * @param ownTeamID single player -1, team mode 0 or 1
     * @return -1 if move not legal or card value
     */
    private int checkSingleSeven(String nickname, String newPosition1, int newPosition2,
            Player ownPlayer, String actualPosition1, int actualPosition2, boolean hasMoved,
            int startingPosition, int ownTeamID, GameState tempGameState) {
        try {
            // checks if pieces are own or from team
            Player nowPlaying = tempGameState.getPlayer(nickname);
            if (teamMode) {
                if (ownPlayer != nowPlaying && ownTeamID != nowPlaying.getTeamID()) {
                    System.err.println("Marble owner's teamID: " + ownPlayer.getTeamID());
                    System.err.println("Playing: " + nowPlaying.getTeamID());
                    return -1;
                }
            } else {
                if (ownPlayer != nowPlaying) {
                    System.err.println("Marble owner's teamID: " + ownPlayer.getTeamID());
                    System.err.println("Playing: " + nowPlaying.getTeamID());
                    return -1;
                }
            }

            int difference;
            if (actualPosition1.equals("B") && newPosition1.equals("B")
                    || (actualPosition1.equals("C") && newPosition1.equals("C"))) {
                // track -> track or heaven -> heaven
                difference = Math.floorMod(newPosition2 - actualPosition2, 64);
                System.err.println("Difference: " + difference);
                return difference;
            } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
                // track -> heaven
                if (!hasMoved) {
                    return -1;
                }
                difference = Math.floorMod(startingPosition - actualPosition2, 64);
                System.err.println("Difference: " + (difference + newPosition2 + 1));
                return difference + newPosition2 + 1;
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Checks if pieces are on the way to destination and
     * if they are own pieces or pieces to eliminate.
     * Includes also the destination, so it checks also for blocks.
     * This method executes also the hypothetical moves on a tempGameState.
     * @param newPosition1 B or C
     * @param newPosition2 int between 0-63
     * @param ownPlayer owner of this marble
     * @param actualPosition1 B or C
     * @param actualPosition2 int between 0-63
     * @param startingPosition 0, 16, 32 or 48
     * @param pieceID int between 1-4
     * @return null if illegal move, empty list if nobody is eliminated
     * and with elements if somebody is eliminated
     */
    private ArrayList<Piece> piecesOnPath(String newPosition1, int newPosition2, Player ownPlayer,
            String actualPosition1, int actualPosition2, int startingPosition, int pieceID,
            GameState tempGameState, MainGame tempMainGame) {
        ArrayList<Piece> piecesToEliminate = new ArrayList<>();
        assert actualPosition1 != null;
        if (actualPosition1.equals("B") && newPosition1.equals("B")) {
            // track -> track
            if (piecesOnPathHelper(actualPosition2, newPosition2, ownPlayer, piecesToEliminate,
                    tempGameState)) {
                return null;
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // track -> heaven
            // checks track part
            if (piecesOnPathHelper(actualPosition2, startingPosition, ownPlayer,
                    piecesToEliminate, tempGameState)) {
                return null;
            }
            // checks heaven part
            if (rulesCheckHelper.piecesInHeaven(ownPlayer, -1, newPosition2)) {
                System.err.println("Piece on path in heaven");
                return null;
            }
        } else if (actualPosition1.equals("C") && newPosition1.equals("C")) {
            // heaven -> heaven
            if (rulesCheckHelper.piecesInHeaven(ownPlayer, actualPosition2, newPosition2)) {
                System.err.println("Piece on path in heaven");
                return null;
            }
        }
        // move the piece on the tempGameState
        rulesCheckHelper.simpleMoveSEVE(ownPlayer, pieceID, newPosition1, newPosition2, tempMainGame);

        // eliminate the pieces on the tempGameState
        for (Piece piece : piecesToEliminate) {
            rulesCheckHelper.eliminatePieceSEVE(piece, tempMainGame);
        }
        System.err.println(ownPlayer + " eliminated the following pieces with " + pieceID + ": "
                + piecesToEliminate);
        return piecesToEliminate;
    }

    /**
     * Helper method for pieces on path check.
     * @param actualPosition2 piece's position 0-63
     * @param destinationOnTrack last position on track
     * @param ownPlayer owner of this marble
     * @param piecesToEliminate array with possible pieces that are eliminated by the move
     * @return true if invalid move, false if valid
     */
    private boolean piecesOnPathHelper(int actualPosition2, int destinationOnTrack, Player ownPlayer,
            ArrayList<Piece> piecesToEliminate, GameState tempGameState) {
        int difference;
        Piece pieceOnPath;
        difference = Math.floorMod(destinationOnTrack - actualPosition2, 64);
        for (int i = 1; i <= difference; i++) {
            pieceOnPath = tempGameState.trackPositionOccupied((actualPosition2 + i) % 64);
            if (pieceOnPath != null && pieceOnPath.getPieceAlliance()
                    != ownPlayer.getAlliance()) {
                if (!pieceOnPath.getHasMoved()) {
                    System.err.println("You are blocked by: " + pieceOnPath);
                    return true;
                } else {
                    piecesToEliminate.add(pieceOnPath);
                }
            } else if (pieceOnPath != null && pieceOnPath.getPieceAlliance()
                    == ownPlayer.getAlliance()) {
                System.err.println("You can't jump over: " + pieceOnPath);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if newPosition is ok with played card
     * @param card played card
     * @param actualPosition1 A, B or C
     * @param actualPosition2 int between 0-3 or on track 0-63
     * @param newPosition1 B or C
     * @param newPosition2 int between 0-3 or on track 0-63
     * @param startingPosition player's startingPosition (0, 16, ...)
     * @param hasMoved false if player is or has just left home
     * @param ownPlayer owner of this marble
     * @return false if card can't correspond with destination
     */
    protected boolean checkCardWithNewPosition(String card, String actualPosition1,
            int actualPosition2, String newPosition1, int newPosition2, int startingPosition,
            boolean hasMoved, Player ownPlayer) {
        int[] cardValues = rulesCheckHelper.getCardValues(card);
        int difference;
        if (actualPosition1.equals("A") && newPosition1.equals("B")) {
            // you play an exit card and you exit on your starting position
            Piece pieceOnStart = gameState.trackPositionOccupied(newPosition2);
            System.err.println("You are blocked by: " + pieceOnStart);
            return (card.equals("ACEE") || card.equals("KING"))
                    && newPosition2 == startingPosition
                    && (pieceOnStart == null || pieceOnStart.getPieceAlliance()
                    != ownPlayer.getAlliance());
        } else if (actualPosition1.equals("B") && newPosition1.equals("B")) {
            // continue on track
            if (card.equals("FOUR")) {
                for (int cardValue : cardValues) {
                    if (cardValue == -4) {
                        difference = Math.floorMod(newPosition2 - actualPosition2, -64);
                        System.err.println("Difference with -4: " + difference);
                        if (difference == cardValue) {
                            actualCardValue = cardValue;
                            return true;
                        }
                    } else if (cardValue == 4) {
                        difference = Math.floorMod(newPosition2 - actualPosition2, 64);
                        System.err.println("Difference with 4: " + difference);
                        if (difference == cardValue) {
                            actualCardValue = cardValue;
                            return true;
                        }
                    }
                }
            } else {
                difference = Math.floorMod(newPosition2 - actualPosition2, 64);
                System.err.println("Difference: " + difference);
                for (int cardValue : cardValues) {
                    if (cardValue == difference) {
                        actualCardValue = cardValue;
                        return true;
                    }
                }
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // go heaven
            if (!hasMoved) {
                return false;
            }
            if (rulesCheckHelper.piecesInHeaven(ownPlayer, -1, newPosition2)) {
                System.err.println("Piece on path in heaven");
                return false;
            }
            if (card.equals("FOUR")) {
                for (int cardValue : cardValues) {
                    if (cardValue == 4) {
                        difference = Math.floorMod(startingPosition - actualPosition2, 64);
                        if (cardValue == difference + newPosition2 + 1) {
                            System.err.println("Difference: " + (difference + newPosition2 + 1));
                            actualCardValue = cardValue;
                            return true;
                        }
                    } else if (cardValue == -4) {
                        difference = startingPosition - actualPosition2 - newPosition2 - 1;
                        if (cardValue == difference) {
                            System.err.println("Difference: " + difference);
                            actualCardValue = cardValue;
                            return true;
                        }
                    }
                }
            } else {
                difference = Math.floorMod(startingPosition - actualPosition2, 64) + newPosition2 + 1;
                for (int cardValue : cardValues) {
                    if (cardValue == difference) {
                        System.err.println("Difference: " + difference);
                        actualCardValue = cardValue;
                        return true;
                    }
                }
            }
        } else if (actualPosition1.equals("C") && newPosition1.equals("C")) {
            if (rulesCheckHelper.piecesInHeaven(ownPlayer, actualPosition2, newPosition2)) {
                System.err.println("Piece on path in heaven");
                return false;
            }
            difference = newPosition2 - actualPosition2;
            for (int cardValue : cardValues) {
                if (cardValue == difference) {
                    System.err.println("Difference: " + difference);
                    actualCardValue = cardValue;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if there is a block on the way to new position
     * @param card played card
     * @param actualPosition1 A, B or C
     * @param actualPosition2 int between 0-3 or 0-63 on track
     * @param newPosition1 A, B or C
     * @param newPosition2 int between 0-3 or 0-63 on track
     * @param player this player
     * @return true if you are blocked, false if not
     */
    protected boolean checkForBlock(String card, String actualPosition1, int actualPosition2,
            String newPosition1, int newPosition2, Player player) {
        int[] startingPositions = new int[] {0, 16, 32, 48};
        Piece pieceOnStart;
        int difference;
        if (actualPosition1.equals("B") && newPosition1.equals("B")) {
            // continue on track
            if (card.equals("FOUR")) {
                if (actualCardValue == -4) {
                    for (int startingPosition : startingPositions) {
                        for (int i = -1; i >= actualCardValue; i--) {
                            if ((actualPosition2 + i) % 64 == startingPosition) {
                                pieceOnStart = gameState.trackPositionOccupied(startingPosition);
                                if (pieceOnStart != null && pieceOnStart.getPieceAlliance().
                                        getStartingPosition() == startingPosition) {
                                    System.err.println("You are blocked by: " + pieceOnStart);
                                    return true;
                                }
                            }
                        }
                    }
                } else if (actualCardValue == 4) {
                    return checkForBlockHelper(actualPosition2, startingPositions, actualCardValue);
                }
            } else {
                difference = Math.floorMod(newPosition2 - actualPosition2, 64);
                return checkForBlockHelper(actualPosition2, startingPositions, difference);
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // go heaven
            for (Piece piece : player.pieces) {
                if (piece.getPositionServer1().equals("B") && !piece.getHasMoved()) {
                    System.err.println("You are blocked by: " + piece);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method for checking blocks. Calculates if a piece is blocked,
     * for positive card values.
     * @param actualPosition2 piece's position between 0-63
     * @param startingPositions array which contains the 4 possible starting positions
     * @param steps number of steps from actualPosition2 to newPosition2
     * @return true if the piece is blocked, false if not
     */
    private boolean checkForBlockHelper(int actualPosition2, int[] startingPositions,
            int steps) {
        Piece pieceOnStart;
        for (int startingPosition : startingPositions) {
            for (int i = 1; i <= steps; i++) {
                if ((actualPosition2 + i) % 64 == startingPosition) {
                    pieceOnStart = gameState.trackPositionOccupied(startingPosition);
                    if (pieceOnStart != null && pieceOnStart.getPieceAlliance().getStartingPosition()
                            == startingPosition) {
                        System.err.println("You are blocked by: " + pieceOnStart);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
