package jDogs.board;

import jDogs.Alliance;

/**
 * Creates board initial setup
 * <li>1 x TrackTiles</li>
 * <li>4 x HomeTiles</li>
 * <li>4 x HeavenTiles</li>
 */
public class Board {

    public static final int NUM_TRACK_TILES_X_PLAYER = 16;
    public static final int NUM_HEAVEN_TILES = 4;
    public static final int NUM_HOME_TILES = 4;

    HomeTiles[] allHomeTiles;
    TrackTiles trackTiles;
    HeavenTiles[] allHeavenTiles;

    public Board(int numOfPlayers) {
        allHomeTiles = createAllHomeTiles(numOfPlayers);
        trackTiles = new TrackTiles(numOfPlayers);
        allHeavenTiles = createAllHeavenTiles(numOfPlayers);
    }

    private HomeTiles[] createAllHomeTiles(int numOfPlayers) {
        allHomeTiles = new HomeTiles[numOfPlayers];
        allHomeTiles[0] = new HomeTiles(Alliance.YELLOW);
        allHomeTiles[1] = new HomeTiles(Alliance.GREEN);
        if (numOfPlayers == 6) {
            allHomeTiles[2] = new HomeTiles(Alliance.WHITE);
            allHomeTiles[3] = new HomeTiles(Alliance.BLUE);
            allHomeTiles[4] = new HomeTiles(Alliance.RED);
            allHomeTiles[5] = new HomeTiles(Alliance.BLACK);
        } else {
            allHomeTiles[2] = new HomeTiles(Alliance.BLUE);
            allHomeTiles[3] = new HomeTiles(Alliance.RED);
        }
        return allHomeTiles;
    }

    private HeavenTiles[] createAllHeavenTiles(int numOfPlayers) {
        allHeavenTiles = new HeavenTiles[numOfPlayers];
        allHeavenTiles[0] = new HeavenTiles(Alliance.YELLOW);
        allHeavenTiles[1] = new HeavenTiles(Alliance.GREEN);
        if (numOfPlayers == 6) {
            allHeavenTiles[2] = new HeavenTiles(Alliance.WHITE);
            allHeavenTiles[3] = new HeavenTiles(Alliance.BLUE);
            allHeavenTiles[4] = new HeavenTiles(Alliance.RED);
            allHeavenTiles[5] = new HeavenTiles(Alliance.BLACK);
        } else {
            allHeavenTiles[2] = new HeavenTiles(Alliance.BLUE);
            allHeavenTiles[3] = new HeavenTiles(Alliance.RED);
        }
        return allHeavenTiles;
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
        for (HomeTiles allHomeTile : allHomeTiles) {
            sb.append(allHomeTile).append("\n");
        }
        sb.append("\nTrack:\n");
        sb.append(trackTiles).append("\n");
        sb.append("\nHeaven:\n");
        for (HeavenTiles allHeavenTile : allHeavenTiles) {
            sb.append(allHeavenTile).append("\n");
        }
        return sb.toString();
    }
}