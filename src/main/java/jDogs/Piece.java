package jDogs;

public class Piece {

    protected final Alliance pieceAlliance;
    protected final int pieceID;
    protected final int startingPosition;
    protected final boolean hasMoved;

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
}
