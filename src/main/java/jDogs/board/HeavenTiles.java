package jDogs.board;

import jDogs.Alliance;

public class HeavenTiles {

    Tile[] heavenTiles;
    Alliance alliance;

    HeavenTiles(Alliance alliance) {
        this.alliance = alliance;
        heavenTiles = createHeavenTiles();
    }

    private Tile[] createHeavenTiles() {
        final Tile[] heavenTiles = new Tile[Board.NUM_HEAVEN_TILES];
        for (int i = 0; i < Board.NUM_HEAVEN_TILES; i++) {
            heavenTiles[i] = new Tile(i);
        }
        return heavenTiles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : heavenTiles) {
            sb.append(tile + " ");
        }
        return sb.toString();
    }
}
