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

    HomeTiles yellowHome;
    HomeTiles greenHome;
    HomeTiles blueHome;
    HomeTiles redHome;
    TrackTiles trackTiles;
    HeavenTiles yellowHeaven;
    HeavenTiles greenHeaven;
    HeavenTiles blueHeaven;
    HeavenTiles redHeaven;

    public Board() {
        createBoard();
    }

    public void createBoard() {
        // set 4 homes
        yellowHome = new HomeTiles(Alliance.YELLOW);
        greenHome = new HomeTiles(Alliance.GREEN);
        blueHome = new HomeTiles(Alliance.BLUE);
        redHome = new HomeTiles(Alliance.RED);

        // set track
        trackTiles = new TrackTiles();

        // set 4 heavens
        yellowHeaven = new HeavenTiles(Alliance.YELLOW);
        greenHeaven = new HeavenTiles(Alliance.GREEN);
        blueHeaven = new HeavenTiles(Alliance.BLUE);
        redHeaven = new HeavenTiles(Alliance.RED);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("JDogs\n\n");
        sb.append("Home:\n");
        sb.append(yellowHome.toString() + "\n");
        sb.append(greenHome.toString() + "\n");
        sb.append(blueHome.toString() + "\n");
        sb.append(redHome.toString() + "\n");

        return sb.toString();
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