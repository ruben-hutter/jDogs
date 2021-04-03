package jDogs.board;
//one of 64/96 normal Indentations

import jDogs.pieces.Piece;

public class TrackTiles {

    Tile[] trackTiles;

    TrackTiles() {
        trackTiles = createAllPossibleEmptyTiles();
    }

    private static Tile[] createAllPossibleEmptyTiles() {
        final Tile[] trackTiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            trackTiles[i] = new Tile(i);
        }
        return trackTiles;
    }
}
