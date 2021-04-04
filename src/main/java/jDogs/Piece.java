package jDogs;

public class Piece {

    protected final Alliance pieceAlliance;
    protected final boolean hasMoved;
    protected final int pieceID;

    public Piece(final Alliance pieceAlliance, final int pieceID) {
        this.pieceAlliance = pieceAlliance;
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
}
