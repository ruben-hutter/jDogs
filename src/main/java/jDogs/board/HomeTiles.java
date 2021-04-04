package jDogs.board;

import jDogs.Alliance;

public class HomeTiles {

    Tile[] homeTiles;
    Alliance alliance;

    public HomeTiles(Alliance alliance) {
        homeTiles = createHomeTiles();
        this.alliance = alliance;
    }

    // should be void?
    private Tile[] createHomeTiles() {
        final Tile[] homeTiles = new Tile[Board.NUM_HOME_TILES];
        for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
            homeTiles[i] = new Tile(i);
        }
        return homeTiles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : homeTiles) {
            sb.append(tile.toString() + " ");
        }
        return sb.toString();
    }
}
