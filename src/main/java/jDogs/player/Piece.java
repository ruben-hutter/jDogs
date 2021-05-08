package jDogs.player;

import jDogs.Alliance_4;
import jDogs.ClientGame;
import jDogs.board.Board;
import jDogs.board.Tile;
import org.jetbrains.annotations.NotNull;

/**
 * Defines a piece. Has an ID, an Alliance, a starting position,
 * it can be moved and it has a position (one used on server side
 * and one for the client)
 */
public class Piece implements Comparable<Piece> {

    protected final Alliance_4 pieceAlliance4;
    protected final int startingPosition;
    protected final int pieceID;
    private boolean hasMoved;
    private Tile position;
    private String positionServer1;
    private int positionServer2;

    public Piece(final Alliance_4 pieceAlliance4, final int startingPosition, final int pieceID) {
        this.pieceAlliance4 = pieceAlliance4;
        this.startingPosition = startingPosition;
        this.pieceID = pieceID;
        hasMoved = false;
    }

    /**
     * Gives the alliance for this piece in a 4 player game
     * @return alliance4
     */
    public Alliance_4 getPieceAlliance() {
        return pieceAlliance4;
    }

    /**
     * Returns if the piece has been moved from his
     * starting position on the track
     * @return true if moved, false if not
     */
    public boolean getHasMoved() {
        return hasMoved;
    }

    /**
     * Changes the property hasMoved to the opposite
     * of the actual state.
     */
    public void changeHasMoved() {
        hasMoved = !hasMoved;
    }

    /**
     * Gets the ID of this piece
     * @return an int between 1-4
     */
    public int getPieceID() {
        return pieceID;
    }

    /**
     * Changes the position for the server
     * @param position1 A, B or C for home, track or heaven
     * @param position2 number between 0-3 or 0-63 with 4 player
     */
    public void setPositionServer(String position1, int position2) {
        positionServer1 = position1;
        positionServer2 = position2;
    }

    /**
     * Changes the position for the client
     * @param position a Tile (home, track or heaven)
     */
    public void setPositionClient(Tile position) {
        this.position = position;
    }

    /**
     * Gets the first part of the piece position
     * on server side
     * @return A, B or C
     */
    public String getPositionServer1() {
        return positionServer1;
    }

    /**
     * Gets the second part of the piece position
     * on server side
     * @return number between 0-3 or 0-63 with 4 player
     */
    public int getPositionServer2() {
        return positionServer2;
    }

    /**
     * Gets the position on client side
     * @return a Tile (home, track or heaven)
     */
    public Tile getPositionClient() {
        return position;
    }

    /**
     * Gets the starting position of this piece
     * @return this starting position (0, 16, 32 or 48)
     */
    public int getStartingPosition() {
        return startingPosition;
    }

    @Override
    public String toString() {
        return getPieceAlliance() + "-" + pieceID + " ";
    }

    @Override
    public int compareTo(@NotNull Piece o) {
        if (positionServer2 < o.positionServer2) {
            return -1;
        } else if (positionServer2 > o.positionServer2) {
            return 1;
        }
        return 0;
    }
}
