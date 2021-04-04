package jDogs.board;

import jDogs.Alliance;
import jDogs.pieces.Piece;

public class HomeTiles {

    Tile[] homeTiles;
    Alliance alliance;

    HomeTiles(Alliance alliance) {
        homeTiles = createHomeTiles();
    }

    // should be void?
    private Tile[] createHomeTiles() {
        final Tile[] homeTiles = new Tile[Board.NUM_HOME_TILES];
        for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
            homeTiles[i] = new Tile(i, new Piece(alliance, false, i));
        }
        return homeTiles;
    }
}
