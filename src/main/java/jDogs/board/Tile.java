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

    /**
     * Returns if a piece is placed on this Tile
     * @return true if a piece is on this tile
     */
    public boolean isTileOccupied() {
        return piece != null;
    }

    /**
     * Get the piece that is on this tile
     * @return a piece or null
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Sets a piece on this tile
     * @param piece the piece that is placed on the tile
     */
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
