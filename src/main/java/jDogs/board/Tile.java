package jDogs.board;

import jDogs.player.Piece;

/**
 * Tile is a square where a token can be placed
 */
public abstract class Tile {

    protected final int tileCoordinate;
    protected Piece piece;

    Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public boolean isTileOccupied() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public String toString() {
        if (isTileOccupied()) {
            return getPiece().toString();
        }
        return ". ";
    }
}
