package jDogs.board;

import jDogs.pieces.Piece;

public class HomeTile extends Tile {

    HomeTile(int tileCoordinate) {
        super(tileCoordinate);
    }

    @Override
    public boolean isTileOccupied() {
        return false;
    }

    @Override
    public Piece getPiece() {
        return null;
    }
}
