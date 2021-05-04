package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Piece;
import jDogs.player.Player;
import jDogs.serverclient.helpers.Queuejd;
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
     * @return int value of card
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
                return null;
        }
        return possibleValues;
    }

    /**
     * Takes all the infos to check a move and saves it to
     * a PlayersActualInfo object.
     * @param pieceID int between 1-4
     * @return an object with all the infos
     */
    protected PlayersActualInfo getPlayerInfo(int pieceID, Alliance_4 alliance4) {
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
        return new PlayersActualInfo(ownPlayer, actualPosition1, actualPosition2, hasMoved,
                startingPosition, teamID);
    }

    /**
     * Checks if a player can move the chosen color
     * @return true if the move is ok, false if not
     */
    protected boolean checkMovedAlliance() {
        return false;
    }

    /**
     * Checks if on the destination is already a piece
     * @param player this player
     * @param pieceID int 1-4
     * @param newPosition1 A, B or C
     * @param newPosition2 int between 0-3 or 0-63 on track
     * @return false if you eliminate yourself
     */
    protected boolean checkWhichMove(Player player, int pieceID, String newPosition1,
            int newPosition2) {
        Piece pieceToEliminate = mainGame.getGameState().trackPositionOccupied(newPosition2);
        if (pieceToEliminate != null) {
            attackMove(player, pieceID, newPosition1, newPosition2, pieceToEliminate);
        } else {
            simpleMove(player, pieceID, newPosition1, newPosition2);
        }
        return true;
    }

    /**
     * Move a piece and eliminate enemy
     * @param player player moving pieces
     * @param pieceID 1-4
     * @param newPosition1 A, B or C
     * @param newPosition2 0-63
     * @param toEliminate the eliminated piece
     */
    protected void attackMove(Player player, int pieceID, String newPosition1, int newPosition2,
            Piece toEliminate) {
        simpleMove(player, pieceID, newPosition1, newPosition2);
        eliminatePiece(toEliminate);
    }

    /**
     * Move a piece without eliminating any
     * @param player player moving pieces
     * @param pieceID 1-4
     * @param newPosition1 A, B or C
     * @param newPosition2 0-63
     */
    protected void simpleMove(Player player, int pieceID, String newPosition1, int newPosition2) {

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
        mainGame.sendMessageToParticipants("MOVE " + pieceAlliance + "-" + pieceID + " "
                + newPosition1 + newPosition2);
    }

    /**
     * Puts a given piece back to home.
     * @param piece eliminated piece
     */
    protected void eliminatePiece(Piece piece) {
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
        mainGame.sendMessageToParticipants("MOVE " + pieceAlliance + "-" + pieceID + " "
                + newPosition1 + newPosition2);
    }

    /**
     * Updates the game on client side. Eliminate played card and
     * sends to the client the updated hand,
     * gives to the clients the new board state.
     * @param nickname player's name
     * @param mainGame were the game has been started
     */
    protected void updateGame(String nickname, MainGame mainGame, String cardToEliminate) {
        mainGame.sendMessageToParticipants("BORD");
        //eliminate card
        mainGame.getGameState().getCards().get(nickname).remove(cardToEliminate);
        mainGame.getPlayer(nickname).sendMessageToClient("CARD " + cardToEliminate);
        mainGame.sendMessageToParticipants("HAND");

        mainGame.turnComplete(nickname);

        // check if there is a winner
        mainGame.getGameState().checkForVictory();
    }
}
