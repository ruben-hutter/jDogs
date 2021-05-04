package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
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
        return toCheckMove;
    }

    /**
     * Checks:
     * if for the given card you can go to the desired position
     * if somebody is eliminated by the action
     * @param completeMove card piece destination
     * @param nickname name of player
     * @return 0 if move is done correctly, x for 0 < x if move not possible
     */
    protected int checkMove(String completeMove, String nickname) { // TWOO YELO-1 B04
        if (completeMove.length() == 15) {
            String card = null;
            int pieceID = -1;
            String newPosition1 = null;
            int newPosition2 = -1;
            String actualPosition1 = null;
            int actualPosition2 = -1;
            boolean hasMoved = false;
            int startingPosition = -1;
            Player ownPlayer = null;
            int ownTeamID = -1;
            try {
                card = completeMove.substring(0, 4);
                String alliance = completeMove.substring(5, 9);
                Alliance_4 alliance4 = rulesCheckHelper.convertAlliance(alliance);
                pieceID = Integer.parseInt(completeMove.substring(10, 11));
                PlayersActualInfo playersActualInfo = rulesCheckHelper.getPlayerInfo(pieceID,
                        alliance4);
                newPosition1 = completeMove.substring(12, 13);
                newPosition2 = Integer.parseInt(completeMove.substring(13));
                ownPlayer = playersActualInfo.getPlayer();
                actualPosition1 = playersActualInfo.getActualPosition1();
                actualPosition2 = playersActualInfo.getActualPosition2();
                hasMoved = playersActualInfo.getHasMoved();
                startingPosition = playersActualInfo.getStartingPosition();
                ownTeamID = playersActualInfo.getTeamID();

                if (newPosition1.equals("A")) {
                    return 1;
                }
            } catch (Exception e) {
                return 2;
            }

            // prevent players from moving with others pieces
            Player nowPlaying = gameState.getPlayer(nickname);
            if (teamMode) {
                if ((ownPlayer != nowPlaying && !nowPlaying.getFinished()) || (ownPlayer != nowPlaying
                        && ownTeamID != nowPlaying.getTeamID())) {
                    return 3;
                }
            } else {
                if (ownPlayer != nowPlaying) {
                    return 4;
                }
            }

            // if card not ok with destination, return to client
            if (!checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                    newPosition2, startingPosition, hasMoved, ownPlayer, pieceID, rulesCheckHelper)) {
                return 5;
            }

            // if move passes an occupied starting position, and that piece haven't moved
            assert actualPosition1 != null;
            if (checkForBlock(card, actualPosition1, actualPosition2, newPosition1, newPosition2,
                    ownPlayer, rulesCheckHelper)) {
                return 6;
            }

            // check if there is a piece on destination
            if (!rulesCheckHelper.checkWhichMove(ownPlayer, pieceID, newPosition1, newPosition2)) {
                return 7;
            }

            rulesCheckHelper.updateGame(nickname, mainGame, cardToEliminate);

        } else {
            return 8;
        }
        return 0;
    }

    /**
     * Checks move when card JACK is played
     * @param twoPieces pieces to switch position
     * @param nickname players name
     * @return 0 if move is done correctly, x for 0 < x if move not possible
     */
    protected int checkMoveJack(String twoPieces, String nickname) { // JACK YELO-1 BLUE-2
        try {
            if (twoPieces.length() == 18) {
                String ownAlliance = twoPieces.substring(5, 9);
                int ownPieceID = Integer.parseInt(twoPieces.substring(10, 11));
                String ownActualPosition1 = null;
                int ownActualPosition2 = -1;
                Player ownPlayer = null;
                Alliance_4 ownAlliance4 = rulesCheckHelper.convertAlliance(ownAlliance);
                int ownTeamID = -1;

                String otherAlliance = twoPieces.substring(12, 16);
                int otherPieceID = Integer.parseInt(twoPieces.substring(17));
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
                    } else if (player.getAlliance() == otherAlliance4) {
                        otherPlayer = player;
                        otherActualPosition1 = player.receivePosition1Server(otherPieceID);
                        otherActualPosition2 = player.receivePosition2Server(otherPieceID);
                        otherHasMoved = player.receiveHasMoved(otherPieceID);
                    }
                }
                // prevent players from moving with others pieces
                Player nowPlaying = gameState.getPlayer(nickname);
                if (teamMode) {
                    if ((ownPlayer != nowPlaying && !nowPlaying.getFinished()) || (ownPlayer !=
                            nowPlaying && ownTeamID != nowPlaying.getTeamID())) {
                        return 2;
                    }
                } else {
                    if (ownPlayer != nowPlaying) {
                        return 1;
                    }
                }
                assert ownActualPosition1 != null;
                assert otherActualPosition1 != null;
                if (ownActualPosition1.equals("A") || otherActualPosition1.equals("A")
                        || ownActualPosition1.equals("C") || otherActualPosition1.equals("C")
                        || !otherHasMoved) {
                    return 3;
                } else {
                    rulesCheckHelper.simpleMove(ownPlayer, ownPieceID, otherActualPosition1,
                            otherActualPosition2);
                    rulesCheckHelper.simpleMove(otherPlayer, otherPieceID, ownActualPosition1,
                            ownActualPosition2);

                    rulesCheckHelper.updateGame(nickname, mainGame, cardToEliminate);
                }
            }
        } catch (Exception e) {
            return 4;
        }
        return 0;
    }

    /**
     * Checks move when card SEVE is played
     * @param completeMove given move
     * @param nickname player's name
     * @return 0 if move is done correctly, x for 0 < x if move not possible
     */
    protected int checkMoveSeven(String completeMove, String nickname) {
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
            ArrayList<Piece> piecesToEliminate = new ArrayList<>();
            ArrayList<Piece> singleEliminations;
            for (int i = 0; i < piecesToMove; i++) {
                move = completeMove.substring(startIndex, startIndex + 10);
                alliance = move.substring(0, 4);
                alliance4 = rulesCheckHelper.convertAlliance(alliance);
                pieceID = Integer.parseInt(move.substring(5, 6));
                PlayersActualInfo playersActualInfo = rulesCheckHelper.getPlayerInfo(pieceID, alliance4);
                newPosition1 = move.substring(7, 8);
                newPosition2 = Integer.parseInt(move.substring(8));
                ownPlayer = playersActualInfo.getPlayer();
                actualPosition1 = playersActualInfo.getActualPosition1();
                actualPosition2 = playersActualInfo.getActualPosition2();
                hasMoved = playersActualInfo.getHasMoved();
                startingPosition = playersActualInfo.getStartingPosition();
                ownTeamID = playersActualInfo.getTeamID();
                moveValue = checkSingleSeven(nickname, newPosition1, newPosition2, ownPlayer,
                        actualPosition1, actualPosition2, hasMoved, startingPosition, ownTeamID);
                if (moveValue < 0) {
                    return 1;
                }
                countToSeven += moveValue;
                if (countToSeven > 7) {
                    return 2;
                }
                singleEliminations = piecesOnPath(newPosition1, newPosition2, ownPlayer,
                        actualPosition1, actualPosition2, startingPosition, pieceID);
                if (singleEliminations == null) {
                    return 3;
                }
                piecesToEliminate.addAll(singleEliminations);
                startIndex += 11;
            }

            // if you move a total of 7 correctly
            if (countToSeven == 7) {
                startIndex = 7;
                Player player = gameState.getPlayer(nickname);
                for (int i = 0; i < piecesToMove; i++) {
                    move = completeMove.substring(startIndex, startIndex + 10);
                    pieceID = Integer.parseInt(move.substring(5, 6));
                    newPosition1 = move.substring(7, 8);
                    newPosition2 = Integer.parseInt(move.substring(8));

                    rulesCheckHelper.simpleMove(player, pieceID, newPosition1, newPosition2);
                    startIndex += 11;
                }
                for (Piece piece : piecesToEliminate) {
                    rulesCheckHelper.eliminatePiece(piece);
                }

                rulesCheckHelper.updateGame(nickname, mainGame, cardToEliminate);

            } else {
                return 4;
            }
        } catch (Exception e) {
            return 5;
        }
        return 0;
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
            int startingPosition, int ownTeamID) {
        try {
            // checks if pieces are own or from team
            Player nowPlaying = gameState.getPlayer(nickname);
            if (teamMode) {
                if (ownPlayer != nowPlaying && ownTeamID != nowPlaying.getTeamID()) {
                    return -1;
                }
            } else {
                if (ownPlayer != nowPlaying) {
                    return -1;
                }
            }

            int difference;
            assert actualPosition1 != null;
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
                difference = Math.floorMod(startingPosition - actualPosition2, 64);
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
            String actualPosition1, int actualPosition2, int startingPosition, int pieceID) {
        ArrayList<Piece> piecesToEliminate = new ArrayList<>();

        assert actualPosition1 != null;
        if (actualPosition1.equals("B") && newPosition1.equals("B")) {
            // track -> track
            if (piecesOnPathHelper(actualPosition2, newPosition2, ownPlayer, piecesToEliminate)) {
                return null;
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // track -> heaven
            if (piecesOnPathHelper(actualPosition2, startingPosition, ownPlayer,
                    piecesToEliminate)) {
                return null;
            }
            for (Piece piece : ownPlayer.pieces) {
                if (piece.getPieceID() != pieceID && piece.getPositionServer1().equals("C")
                        && piece.getPositionServer2() <= newPosition2) {
                    return null;
                }
            }
        } else if (actualPosition1.equals("C") && newPosition1.equals("C")) {
            // heaven -> heaven
            for (Piece piece : ownPlayer.pieces) {
                if (piece.getPieceID() != pieceID && piece.getPositionServer1().equals("C")
                        && piece.getPositionServer2() <= newPosition2) {
                    return null;
                }
            }
        }
        return piecesToEliminate;
    }

    /**
     * Helper method for pieces on path check.
     * @param actualPosition2 piece's position 0-63
     * @param destinationOnTrack last position on track
     * @param ownPlayer player moving
     * @param piecesToEliminate array with possible pieces that are eliminated by the move
     * @return true if invalid move, false if not
     */
    private boolean piecesOnPathHelper(int actualPosition2, int destinationOnTrack, Player ownPlayer,
            ArrayList<Piece> piecesToEliminate) {
        int difference;
        Piece pieceOnPath;
        difference = Math.floorMod(destinationOnTrack - actualPosition2, 64);
        for (int i = 1; i <= difference; i++) {
            pieceOnPath = gameState.trackPositionOccupied((actualPosition2 + i) % 64);
            if (pieceOnPath != null && pieceOnPath.getPieceAlliance()
                    != ownPlayer.getAlliance()) {
                if (!pieceOnPath.getHasMoved()) {
                    return true;
                } else {
                    piecesToEliminate.add(pieceOnPath);
                }
            } else if (pieceOnPath != null && pieceOnPath.getPieceAlliance()
                    == ownPlayer.getAlliance()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if newPosition is ok with played cardmainGame
     * @param card played card
     * @param actualPosition1 A, B or C
     * @param actualPosition2 int between 0-3 or on track 0-63
     * @param newPosition1 A, B or C
     * @param newPosition2 int between 0-3 or on track 0-63
     * @param startingPosition player's startingPosition (0, 16, ...)
     * @param hasMoved false if player is or has just left home
     * @return false if card can't correspond with destination
     */
    private boolean checkCardWithNewPosition(String card, String actualPosition1,
            int actualPosition2, String newPosition1, int newPosition2, int startingPosition,
            boolean hasMoved, Player ownPlayer, int pieceID, RulesCheckHelper rulesCheckHelper) {
        int[] cardValues = rulesCheckHelper.getCardValues(card);
        int difference;
        if (cardValues == null) {
            return false;
        }
        if (actualPosition1.equals("A") && newPosition1.equals("B")) {
            // you play an exit card and you exit on your starting position
            Piece pieceOnStart = gameState.trackPositionOccupied(newPosition2);
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
                        if (difference == cardValue) {
                            return true;
                        }
                    } else if (cardValue == 4) {
                        difference = Math.floorMod(newPosition2 - actualPosition2, 64);
                        if (difference == cardValue) {
                            return true;
                        }
                    }
                }
            } else {
                difference = Math.floorMod(newPosition2 - actualPosition2, 64);
                for (int cardValue : cardValues) {
                    if (cardValue == difference) {
                        return true;
                    }
                }
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // go heaven
            if (!hasMoved) {
                return false;
            }
            for (Piece piece : ownPlayer.pieces) {
                if (piece.getPieceID() != pieceID && piece.getPositionServer1().equals("C")
                        && piece.getPositionServer2() <= newPosition2) {
                    return false;
                }
            }
            if (card.equals("FOUR")) {
                for (int cardValue : cardValues) {
                    if (cardValue == 4) {
                        difference = Math.floorMod(startingPosition - actualPosition2, 64);
                        if (cardValue == difference + newPosition2 + 1) {
                            return true;
                        }
                    } else if (cardValue == -4) {
                        difference = startingPosition - actualPosition2 - newPosition2 - 1;
                        if (cardValue == difference) {
                            return true;
                        }
                    }
                }
            } else {
                difference = Math.floorMod(startingPosition - actualPosition2, 64) + newPosition2 + 1;
                for (int cardValue : cardValues) {
                    if (cardValue == difference) {
                        return true;
                    }
                }
            }
        } else if (actualPosition1.equals("C") && newPosition1.equals("C")) {
            for (Piece piece : ownPlayer.pieces) {
                if (piece.getPieceID() != pieceID && piece.getPositionServer1().equals("C")
                        && piece.getPositionServer2() <= newPosition2) {
                    return false;
                }
            }
            difference = newPosition2 - actualPosition2;
            for (int cardValue : cardValues) {
                if (cardValue == difference) {
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
    private boolean checkForBlock(String card, String actualPosition1, int actualPosition2,
            String newPosition1, int newPosition2, Player player, RulesCheckHelper rulesCheckHelper) {
        int[] startingPositions = new int[] {0, 16, 32, 48};
        int[] cardValues = rulesCheckHelper.getCardValues(card);
        Piece pieceOnStart;
        int difference;
        if (actualPosition1.equals("B") && newPosition1.equals("B")) {
            // continue on track
            if (card.equals("FOUR")) {
                assert cardValues != null;
                for (int cardValue : cardValues) {
                    if (cardValue == -4) {
                        for (int startingPosition : startingPositions) {
                            for (int i = -1; i >= cardValue; i--) {
                                if ((actualPosition2 + i) % 64 == startingPosition) {
                                    pieceOnStart = gameState.trackPositionOccupied(startingPosition);
                                    if (pieceOnStart != null && pieceOnStart.getPieceAlliance().
                                            getStartingPosition() == startingPosition) {
                                        return true;
                                    }
                                }
                            }
                        }
                    } else if (cardValue == 4) {
                        if (checkForBlockHelper(actualPosition2, startingPositions, cardValue)) {
                            return true;
                        }
                    }
                }
            } else {
                difference = Math.floorMod(newPosition2 - actualPosition2, 64);
                return checkForBlockHelper(actualPosition2, startingPositions, difference);
            }
        } else if (actualPosition1.equals("B") && newPosition1.equals("C")) {
            // go heaven
            for (Piece piece : player.pieces) {
                if (piece.getPositionServer1().equals("B") && !piece.getHasMoved()) {
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
                    if (pieceOnStart.getPieceAlliance().getStartingPosition() == startingPosition) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
