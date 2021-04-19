package jDogs.board;

import jDogs.Alliance_4;

/**
 * Element to create a complete heaven for player
 */
public class HeavenTile extends Tile {

    Alliance_4 alliance4;

    HeavenTile(int tileCoordinate, Alliance_4 alliance4) {
        super(tileCoordinate);
        this.alliance4 = alliance4;
    }

    public Alliance_4 getAlliance() {
        return alliance4;
    }
}
