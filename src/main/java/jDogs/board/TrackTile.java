package jDogs.board;
//one of 64/96 normal Indentations

import jDogs.pieces.Piece;

public class TrackTile extends Tile {


    TrackTile(int tileCoordinate) {
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
