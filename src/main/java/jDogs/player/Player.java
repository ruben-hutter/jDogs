package jDogs.player;

import jDogs.Alliance;
import jDogs.Piece;
import jDogs.board.Board;
import jDogs.board.HomeTile;
import jDogs.board.Tile;

/**
 * This class defines a player
 * Giving a name and an alliance it initializes
 * the pieces for the player and sets them on the home tiles.
 */
public class Player {

    private static int startingPositionCounter = 0;

    public Piece[] pieces;
    String playerName;
    Alliance alliance;
    Board board;
    public int startingPosition;

    public Player(String playerName, Alliance alliance, Board board) {
        this.playerName = playerName;
        this.alliance = alliance;
        this.board = board;
        startingPosition = startingPositionCounter;
        pieces = createPieces(startingPosition);
        setPiecesOnHome();
        startingPositionCounter += Board.NUM_TRACK_TILES_X_PLAYER;
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
     * Put pieces on their home.
     */
    private void setPiecesOnHome() {
        for (int i = 0; i < pieces.length; i++) {
            board.allHomeTiles.get(alliance)[i].setPiece(pieces[i]);
            pieces[i].setPosition(board.allHomeTiles.get(alliance)[i]);
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    /**
     * Get the piece you want, giving the id.
     * @param pieceID a number between 1-4
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

    public void setPiece(int pieceID, Tile newPosition) {
        Piece pieceToMove = getPiece(pieceID);
        Tile oldPosition = pieceToMove.getPosition();
        pieceToMove.setPosition(newPosition);
        oldPosition.setPiece(null);
        newPosition.setPiece(pieceToMove);
    }

    @Override
    public String toString() {
        return getPlayerName() + ": " + getAlliance();
    }
}