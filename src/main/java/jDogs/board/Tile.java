package jDogs.board;

import jDogs.pieces.Piece;

/**
 * Tile is a square where a token can be placed
 */
public class Tile {

    protected final int tileCoordinate;
    protected Piece piece;

    Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    Tile(final int tileCoordinate, Piece piece) {
        this.tileCoordinate = tileCoordinate;
        this.piece = piece;
    }

    public boolean isTileOccupied() {
        if (piece != null) {
            return true;
        }
        return false;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public String toString() {
        return tileCoordinate + " - " + piece;
    }
}
