package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Piece;
import jDogs.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RulesCheckHelper {

    private static final Logger logger = LogManager.getLogger(RulesCheckHelper.class);
    private final MainGame mainGame;

    public RulesCheckHelper(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    /**
     * Converts a String alliance to an Alliance_4 instance
     * @param allianceString alliance that you play (YELO, REDD, ...)
     * @return an Alliance_4 instance
     */
    protected Alliance_4 convertAlliance(String allianceString) {
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
                throw new IllegalStateException("Unexpected value: " + allianceString);
        }
        return alliance;
    }

    /**
     * Converts an Alliance_4 instance to a String alliance
     * @param alliance4 the Alliance_4 instance
     * @return alliance in String form (YELO, REDD, ...)
     */
    protected String convertAlliance(Alliance_4 alliance4) {
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
     * Gives you the int value of your String card
     * @param card String card
     * @return int value/s of card
     */
    protected int[] getCardValues(String card) {
        int[] possibleValues;
        switch (card) {
            case "ACEE":
                possibleValues = new int[]{1, 11};
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
                throw new IllegalStateException("Unexpected value: " + card);
        }
        return possibleValues;
    }

    /**
     * Takes all the infos to check a move and saves it to
     * a PlayersActualInfo object.
     * @param pieceID int between 1-4
     * @param alliance4 alliance of piece
     * @param mainGame object that contains the main game infos
     * @return an object with all the infos
     */
    protected PiecesActualInfo getPieceInfo(int pieceID, Alliance_4 alliance4, MainGame mainGame) {
        Player ownPlayer = null;
        String actualPosition1 = null;
        int actualPosition2 = -1;
        boolean hasMoved = false;
        int startingPosition = -1;
        int teamID = -1;

        for (Player player : mainGame.getPlayersArray()) {
            if (player.getAlliance() == alliance4) {
                logger.debug("Alliance Player: " + player.getAlliance());
                ownPlayer = player;
                actualPosition1 = player.receivePosition1Server(pieceID);
                logger.debug("actual position1: " + actualPosition1);
                actualPosition2 = player.receivePosition2Server(pieceID);
                logger.debug("actual position2: " + actualPosition2);
                hasMoved = player.receiveHasMoved(pieceID);
                startingPosition = player.getStartingPosition();
                teamID = player.getTeamID();
            }
        }
        return new PiecesActualInfo(ownPlayer, actualPosition1, actualPosition2, hasMoved,
                startingPosition, teamID);
    }

    /**
     * Make a move on the tempGameState to check SEVE moves
     * @param player owner of marble
     * @param pieceID int between 1-4
     * @param newPosition1 B or C
     * @param newPosition2 int between 0-63
     * @param mainGame tempMainGame
     */
    protected void simpleMoveSEVE(Player player, int pieceID, String newPosition1, int newPosition2,
            MainGame mainGame) {
        Piece piece = player.getPiece(pieceID);
        String pieceAlliance = convertAlliance(piece.getPieceAlliance());

        // change hasMoved state to true if piece moves for first time on track
        if (piece.getPositionServer1().equals("B") && !piece.getHasMoved()) {
            piece.changeHasMoved();
        }

        // updates piece position server
        player.changePositionServer(pieceID, newPosition1, newPosition2);

        // updates piecesOnTrack in gameState
        mainGame.getGameState().updatePiecesOnTrack(piece, newPosition1);
    }

    /**
     * Eliminates a piece on the tempGameState to check SEVE moves
     * @param piece the piece to eliminate
     * @param mainGame tempMainGame
     */
    protected void eliminatePieceSEVE(Piece piece, MainGame mainGame) {
        int pieceID = piece.getPieceID();
        String newPosition1 = "A";
        int newPosition2 = pieceID - 1;
        piece.setPositionServer(newPosition1, newPosition2);
        mainGame.getGameState().updatePiecesOnTrack(piece, "A");
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
        // change hasMoved state to false
        piece.changeHasMoved();
    }

    /**
     * Checks if a piece is on the way in heaven
     * @param player owner of the piece
     * @param actualHeavenPosition -1 if the piece isn't already in heaven or the actualPosition2
     * @param newPosition2 the destination in heaven
     * @return true if you would jump over your pieces, false if not
     */
    protected boolean piecesInHeaven(Player player, int actualHeavenPosition, int newPosition2) {
        Piece[] heaven = new Piece[4];
        for (Piece piece : player.pieces) {
            if (piece.getPositionServer1().equals("C")) {
                heaven[piece.getPositionServer2()] = piece;
            }
        }
        int difference = newPosition2 - actualHeavenPosition;
        for (int i = 1; i <= difference; i++) {
            if (heaven[actualHeavenPosition + i] != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if on the destination is already a piece
     * @param player owner of marble
     * @param pieceID int 1-4
     * @param newPosition1 A, B or C
     * @param newPosition2 int between 0-3 or 0-63 on track
     * @param updateClient an object that contains a return value and, if the move is legal,
     * the moves and elimination moves to communicate to the clients
     * @return false if you eliminate yourself
     */
    protected boolean checkWhichMove(Player player, int pieceID, String newPosition1,
            int newPosition2, UpdateClient updateClient) {
        Piece pieceToEliminate = null;
        if (newPosition1.equals("B")) {
            pieceToEliminate = mainGame.getGameState().trackPositionOccupied(newPosition2);
        }
        if (pieceToEliminate != null) {
            System.err.println("Piece to eliminate: " + pieceToEliminate);
            if (pieceToEliminate.getPieceAlliance() == player.getAlliance()) {
                return false;
            } else {
                attackMove(player, pieceID, newPosition1, newPosition2, pieceToEliminate,
                        updateClient);
            }
        } else {
            simpleMove(player, pieceID, newPosition1, newPosition2, updateClient);
        }
        return true;
    }

    /**
     * Move a piece and eliminate enemy
     * @param player owner of marble
     * @param pieceID 1-4
     * @param newPosition1 B or C
     * @param newPosition2 0-63
     * @param toEliminate the eliminated piece
     * @param updateClient an object that contains a return value and, if the move is legal,
     * the moves and elimination moves to communicate to the clients
     */
    protected void attackMove(Player player, int pieceID, String newPosition1, int newPosition2,
            Piece toEliminate, UpdateClient updateClient) {
        simpleMove(player, pieceID, newPosition1, newPosition2, updateClient);
        eliminatePiece(toEliminate, updateClient);
    }

    /**
     * Move a piece without eliminating any
     * @param player owner of marble
     * @param pieceID 1-4
     * @param newPosition1 B or C
     * @param newPosition2 0-63
     * @param updateClient an object that contains a return value and, if the move is legal,
     * the moves and elimination moves to communicate to the clients
     */
    protected void simpleMove(Player player, int pieceID, String newPosition1, int newPosition2,
            UpdateClient updateClient) {

        Piece piece = player.getPiece(pieceID);
        String pieceAlliance = convertAlliance(piece.getPieceAlliance());

        // change hasMoved state to true if piece moves for first time on track
        if (piece.getPositionServer1().equals("B") && !piece.getHasMoved()) {
            piece.changeHasMoved();
        }

        // updates piece position server
        player.changePositionServer(pieceID, newPosition1, newPosition2);

        // updates piecesOnTrack in gameState
        mainGame.getGameState().updatePiecesOnTrack(piece, newPosition1);

        // updates client side
        updateClient.addToMoves("MOVE " + pieceAlliance + "-" + pieceID + " "
                + newPosition1 + newPosition2);
    }

    /**
     * Puts a given piece back to home.
     * @param piece eliminated piece
     * @param updateClient an object that contains a return value and, if the move is legal,
     * the moves and elimination moves to communicate to the clients
     */
    protected void eliminatePiece(Piece piece, UpdateClient updateClient) {
        int pieceID = piece.getPieceID();
        String newPosition1 = "A";
        int newPosition2 = pieceID - 1;
        piece.setPositionServer(newPosition1, newPosition2);
        mainGame.getGameState().updatePiecesOnTrack(piece, "A");
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
        // change hasMoved state to false
        piece.changeHasMoved();

        // updates client side
        updateClient.addToMoves("MOVE " + pieceAlliance + "-" + pieceID + " "
                + newPosition1 + newPosition2);
    }

    /**
     * Updates the game on client side. Eliminate played card and
     * sends to the client the updated hand,
     * gives to the clients the new board state.
     * @param nickname player's name
     * @param cardToEliminate the played card to eliminate from player's hand
     */
    protected void updateGame(String nickname, String cardToEliminate, UpdateClient updateClient) {
        //eliminate card
        mainGame.getGameState().getCards().get(nickname).remove(cardToEliminate);
        updateClient.addCardToEliminate(cardToEliminate);

        // check if there is a winner
        if (mainGame.getGameState().checkForVictory()) {
            updateClient.setWinners(mainGame.getGameState().getWinners());
        }
    }
}
