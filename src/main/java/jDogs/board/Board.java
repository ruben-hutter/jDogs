package jDogs.board;

import jDogs.Alliance;

/**
 * Creates board initial setup
 * <li>1 x TrackTiles</li>
 * <li>4 x HomeTiles</li>
 * <li>4 x HeavenTiles</li>
 */
public class Board {

    public static final int NUM_TRACK_TILES = 64;
    public static final int NUM_HEAVEN_TILES = 4;
    public static final int NUM_HOME_TILES = 4;

    HomeTiles[] allHomeTiles = new HomeTiles[4];
    TrackTiles trackTiles;
    HeavenTiles[] allHeavenTiles = new HeavenTiles[4];

    public Board() {
        createBoard();
    }

    public void createBoard() {
        // set 4 homes
        allHomeTiles[0] = new HomeTiles(Alliance.YELLOW);
        allHomeTiles[1] = new HomeTiles(Alliance.GREEN);
        allHomeTiles[2] = new HomeTiles(Alliance.BLUE);
        allHomeTiles[3] = new HomeTiles(Alliance.RED);

        // set track
        trackTiles = new TrackTiles();

        // set 4 heavens
        allHeavenTiles[0] = new HeavenTiles(Alliance.YELLOW);
        allHeavenTiles[1] = new HeavenTiles(Alliance.GREEN);
        allHeavenTiles[2] = new HeavenTiles(Alliance.BLUE);
        allHeavenTiles[3] = new HeavenTiles(Alliance.RED);
    }

    public HomeTiles getHomeTiles(Alliance alliance) {
        for (HomeTiles homeTiles : allHomeTiles) {
            if (homeTiles.getAlliance() == alliance) {
                return homeTiles;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nHome:\n");
        for (int i = 0; i < allHomeTiles.length; i++) {
            sb.append(allHomeTiles[i] + "\n");
        }
        sb.append("\nTrack:\n");
        sb.append(trackTiles + "\n");
        sb.append("\nHeaven:\n");
        for (int i = 0; i < allHeavenTiles.length; i++) {
            sb.append(allHeavenTiles[i] + "\n");
        }
        return sb.toString();
    }
}