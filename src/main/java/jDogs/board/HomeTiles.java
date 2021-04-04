package jDogs.board;

import jDogs.Alliance;

public class HomeTiles {

    Tile[] homeTiles;
    Alliance alliance;

    public HomeTiles(Alliance alliance) {
        this.alliance = alliance;
        homeTiles = createHomeTiles();
    }

    private Tile[] createHomeTiles() {
        final Tile[] homeTiles = new Tile[Board.NUM_HOME_TILES];
        for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
            homeTiles[i] = new Tile(i);
        }
        return homeTiles;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public Tile getHomeTile(int i) {
        return homeTiles[i];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : homeTiles) {
            sb.append(tile + " ");
        }
        return sb.toString();
    }
}
