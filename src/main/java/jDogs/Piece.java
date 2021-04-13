package jDogs;

import jDogs.board.Tile;

public class Piece {

    protected final Alliance_4 pieceAlliance4;
    protected final int pieceID;
    protected final int startingPosition;
    protected final boolean hasMoved;
    private Tile position;

    public Piece(final Alliance_4 pieceAlliance4, final int startingPosition, final int pieceID) {
        this.pieceAlliance4 = pieceAlliance4;
        this.startingPosition = startingPosition;
        this.pieceID = pieceID;
        hasMoved = false;
    }

    public Alliance_4 getPieceAlliance() {
        return pieceAlliance4;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public String toString() {
        return getPieceAlliance() + "-" + pieceID + " ";
    }

    public int getPieceID() {
        return pieceID;
    }

    public void setPosition(Tile position) {
        this.position = position;
    }

    public Tile getPosition() {
        return position;
    }
}
