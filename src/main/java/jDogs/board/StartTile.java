package jDogs.board;
// one of 4/6 start indentations

import jDogs.pieces.Piece;

public class StartTile extends Tile {

    StartTile(int tileCoordinate) {
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
