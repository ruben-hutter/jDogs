package jDogs.player;

import jDogs.Alliance_4;
import jDogs.ClientGame;
import jDogs.board.Board;
import jDogs.board.Tile;
import org.jetbrains.annotations.NotNull;

public class Piece implements Comparable<Piece> {

    protected final Alliance_4 pieceAlliance4;
    protected final int pieceID;
    protected final int startingPosition;
    protected final boolean hasMoved;
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

    public boolean getHasMoved() {
        return hasMoved;
    }

    public int getPieceID() {
        return pieceID;
    }

    public void setPositionServer(String position1, int position2) {
        positionServer1 = position1;
        positionServer2 = position2;
        // TODO hasMoved
    }

    public void setPositionClient(Tile position) {
        this.position = position;
    }

    public String getPosition1Server() {
        return positionServer1;
    }

    public int getPosition2Server() {
        return positionServer2;
    }

    public Tile getPositionClient() {
        return position;
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
