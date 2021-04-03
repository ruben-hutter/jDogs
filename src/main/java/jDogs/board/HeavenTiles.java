package jDogs.board;

import jDogs.pieces.Piece;

public class HeavenTiles {

    Tile[] heavenTiles;

    HeavenTiles() {
        heavenTiles = createHeavenTiles();
    }

    private static Tile[] createHeavenTiles() {
        final Tile[] heavenTiles = new Tile[BoardUtils.NUM_TILES_HEAVEN];
        for (int i = 0; i < BoardUtils.NUM_TILES_HEAVEN; i++) {
            heavenTiles[i] = new Tile(i);
        }
        return heavenTiles;
    }
}
