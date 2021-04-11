package jDogs.player;

import jDogs.Alliance;
import jDogs.Piece;
import jDogs.board.Board;
import jDogs.board.HomeTile;

/**
 * This class defines a player
 * Giving a name and an alliance it initializes
 * the pieces for the player and sets them on the home tiles.
 */
public class Player {

    private static int startingPosition = 0;

    public Piece[] pieces;
    String playerName;
    Alliance alliance;
    Board board;

    public Player(String playerName, Alliance alliance, Board board) {
        this.playerName = playerName;
        this.alliance = alliance;
        this.board = board;
        pieces = createPieces(startingPosition);
        setPiecesOnHome();
        startingPosition += Board.NUM_TRACK_TILES_X_PLAYER;
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
        HomeTile homeTile = board.getHomeTiles(alliance);
        for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
            homeTile.getHomeTile(i).setPiece(pieces[i]);
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

    /**
     *
     * @param pieceID
     * @return Piece with fitting pieceID
     */
    public Piece getPiece(int pieceID) {
        for (Piece piece : pieces) {
            if (piece.getPieceID() == pieceID) {
                return piece;
            }
        }
        return null;
    }
}