package jDogs.board;

import jDogs.pieces.Piece;

//represents of of 4/6 end Tiles
public class HeavenTile extends Tile {


    HeavenTile(int tileCoordinate) {
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
