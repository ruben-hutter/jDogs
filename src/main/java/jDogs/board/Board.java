package jDogs.board;

import jDogs.Alliance;

/**
 * Creates board initial setup
 * <li>1 x TrackTiles</li>
 * <li>4 x HomeTiles</li>
 * <li>5 x HeavenTiles</li>
 */
public class Board {

    public static final int NUM_TRACK_TILES = 64;
    public static final int NUM_HEAVEN_TILES = 4;
    public static final int NUM_HOME_TILES = 4;

    public Board() {
        createBoard();
    }

    public void createBoard() {
        // set 4 homes
        HomeTiles yellowHome = new HomeTiles(Alliance.YELLOW);
        HomeTiles greenHome = new HomeTiles(Alliance.GREEN);
        HomeTiles blueHome = new HomeTiles(Alliance.BLUE);
        HomeTiles redHome = new HomeTiles(Alliance.RED);

        // set track
        TrackTiles trackTiles = new TrackTiles();

        // set 4 heavens
        HeavenTiles yellowHeaven = new HeavenTiles(Alliance.YELLOW);
    }
}



/*
Home:
        yellow-1  yellow-2
        green-1   green-2   green-3
        . . . .
        . . . .
Track:
        . . . . . . . . red-1 . . . (x64)
Heaven:
        . . . .
        . . . .
        . . . .
        . . . .
 */