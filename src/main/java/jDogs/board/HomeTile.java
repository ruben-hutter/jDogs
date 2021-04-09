package jDogs.board;

import jDogs.Alliance;

public class HomeTile extends Tile {

    Alliance alliance;
    HomeTile(int tileCoordinate, Alliance alliance) {
        super(tileCoordinate);
        this.alliance = alliance;
    }

    public Alliance getAlliance() {
        return alliance;
    }
}
