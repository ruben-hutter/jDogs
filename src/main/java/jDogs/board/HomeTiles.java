package jDogs.board;

import jDogs.Alliance;
import jDogs.pieces.Piece;

public class HomeTiles {

    Tile[] homeTiles;
    Alliance alliance;

    HomeTiles(Alliance alliance) {
        homeTiles = createHomeTiles();
    }

    private static Tile[] createHomeTiles() {
        final Tile[] homeTiles = new Tile[BoardUtils.NUM_TILES_HOME];
        for (int i = 0; i < BoardUtils.NUM_TILES_HOME; i++) {
            homeTiles[i] = new Tile(i, );
        }
        return homeTiles;
    }
}
Start:
        yellow-1  yellow-2
        green-1   green-2   green-3
        . . . .
        . . . .
Spielfeld:
        . . . . . . . . red-1 . . . (x64)
Ziel:
        . . . .
        . . . .
        . . . .
        . . . .