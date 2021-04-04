package jDogs.board;

import jDogs.Alliance;

public class HeavenTiles {

    Tile[] heavenTiles;
    Alliance alliance;

    HeavenTiles(Alliance alliance) {
        heavenTiles = createHeavenTiles();
    }

    // should be void?
    private Tile[] createHeavenTiles() {
        final Tile[] heavenTiles = new Tile[Board.NUM_HEAVEN_TILES];
        for (int i = 0; i < Board.NUM_HEAVEN_TILES; i++) {
            heavenTiles[i] = new Tile(i);
        }
        return heavenTiles;
    }
}
