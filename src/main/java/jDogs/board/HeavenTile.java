package jDogs.board;

import jDogs.serverclient.helpers.Alliance_4;

/**
 * Element to create a complete heaven for player
 */
public class HeavenTile extends Tile {

    Alliance_4 alliance4;

    /**
     * Creates a heaven tile
     * @param tileCoordinate int between 0-3
     * @param alliance4 alliance
     */
    HeavenTile(int tileCoordinate, Alliance_4 alliance4) {
        super(tileCoordinate);
        this.alliance4 = alliance4;
    }

    public Alliance_4 getAlliance() {
        return alliance4;
    }
}
