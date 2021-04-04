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
        StringBuilder sb = new StringBuilder("\nJDogs\n");
        sb.append("\nHome:\n");
        sb.append(yellowHome + "\n");
        sb.append(greenHome + "\n");
        sb.append(blueHome + "\n");
        sb.append(redHome+ "\n");
        sb.append("\nTrack:\n");
        sb.append(trackTiles + "\n");
        sb.append("\nHeaven:\n");
        sb.append(yellowHeaven + "\n");
        sb.append(greenHeaven + "\n");
        sb.append(blueHeaven + "\n");
        sb.append(redHeaven + "\n");
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