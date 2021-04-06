package jDogs.player;

import jDogs.Alliance;
import jDogs.Piece;
import jDogs.board.Board;
import jDogs.board.HomeTiles;

/**
 * This class defines a player
 * Giving a name and an alliance it initializes
 * the pieces for the player and sets them on the home tiles.
 */
public class Player {

    private static int counter = 0;

    public Piece[] pieces;
    String playerName;
    Alliance alliance;
    Board board;

    public Player(String playerName, Alliance alliance, Board board) {
        this.playerName = playerName;
        this.alliance = alliance;
        this.board = board;
        pieces = createPieces(counter);
        setPiecesOnHome();
        counter += Board.NUM_TRACK_TILES_X_PLAYER;
    }

    /**
     * Creates 4 pieces for a player
     * @return an array with the 4 pieces
     */
    private Piece[] createPieces(int startingPosition) {
        Piece[] pieces = new Piece[Board.NUM_HOME_TILES];
        for (int i = 0; i < pieces.length; i++) {
            pieces[i] = new Piece(alliance, startingPosition,i + 1);
        }
        return pieces;
    }

    /**
     * Sets the newly created pieces on their home tiles.
     */
    private void setPiecesOnHome() {
        HomeTiles homeTiles = board.getHomeTiles(alliance);
        for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
            homeTiles.getHomeTile(i).setPiece(pieces[i]);
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    @Override
    public String toString() {
        return getPlayerName() + ": " + getAlliance();
    }
}