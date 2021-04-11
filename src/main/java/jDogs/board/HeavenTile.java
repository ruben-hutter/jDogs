package jDogs.board;

import jDogs.Alliance;

public class HeavenTile extends Tile {

    Alliance alliance;

    HeavenTile(int tileCoordinate, Alliance alliance) {
        super(tileCoordinate);
        this.alliance = alliance;
    }

    public Alliance getAlliance() {
        return alliance;
    }
}
