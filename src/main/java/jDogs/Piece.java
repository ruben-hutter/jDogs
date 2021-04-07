package jDogs;

import jDogs.board.Tile;

public class Piece {

    protected final Alliance pieceAlliance;
    protected final int pieceID;
    protected final int startingPosition;
    protected final boolean hasMoved;
    private Tile position;

    public Piece(final Alliance pieceAlliance, final int startingPosition, final int pieceID) {
        this.pieceAlliance = pieceAlliance;
        this.startingPosition = startingPosition;
        this.pieceID = pieceID;
        hasMoved = false;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public String toString() {
        return getPieceAlliance() + "-" + pieceID;
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
