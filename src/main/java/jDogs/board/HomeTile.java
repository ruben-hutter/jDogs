package jDogs.board;

import jDogs.serverclient.helpers.Alliance_4;

/**
 * Element to create a complete home for player
 */
public class HomeTile extends Tile {

    Alliance_4 alliance4;

    /**
     * Creates a home tile
     * @param tileCoordinate int between 0-3
     * @param alliance4 alliance
     */
    HomeTile(int tileCoordinate, Alliance_4 alliance4) {
        super(tileCoordinate);
        this.alliance4 = alliance4;
    }

    /**
     * Gives the alliance of the tile
     * @return alliance object
     */
    public Alliance_4 getAlliance() {
        return alliance4;
    }
}
