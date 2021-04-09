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

    HomeTile[][] allHomeTiles;
    TrackTile[] allTrackTiles;
    HeavenTile[][] allHeavenTiles;

    public Board(int numOfPlayers) {
        allHomeTiles = createAllHomeTiles(numOfPlayers);
        allTrackTiles = createAllTrackTiles(numOfPlayers);
        allHeavenTiles = createAllHeavenTiles(numOfPlayers);
    }

    private HomeTile[][] createAllHomeTiles(int numOfPlayers) {
        allHomeTiles = new HomeTile[numOfPlayers];
        allHomeTiles[0] = new HomeTile(Alliance.YELLOW);
        allHomeTiles[1] = new HomeTile(Alliance.GREEN);
        if (numOfPlayers == 6) {
            allHomeTiles[2] = new HomeTile(Alliance.WHITE);
            allHomeTiles[3] = new HomeTile(Alliance.BLUE);
            allHomeTiles[4] = new HomeTile(Alliance.RED);
            allHomeTiles[5] = new HomeTile(Alliance.BLACK);
        } else {
            allHomeTiles[2] = new HomeTile(Alliance.BLUE);
            allHomeTiles[3] = new HomeTile(Alliance.RED);
        }
        return allHomeTiles;
    }

    private TrackTile[] createAllTrackTiles(int numOfPlayers) {
        TrackTile[] allTrackTiles = new TrackTile[NUM_TRACK_TILES_X_PLAYER * numOfPlayers];
    }

    private HeavenTile[][] createAllHeavenTiles(int numOfPlayers) {
        allHeavenTiles = new HeavenTile[numOfPlayers][];
        for (int i = 0; i < numOfPlayers; i++) {
            allHeavenTiles[i] = new HeavenTile[Board.NUM_HEAVEN_TILES];
            for (int j = 0; j < Board.NUM_HEAVEN_TILES; j++) {
            }
        }
        return allHeavenTiles;
    }

    public HomeTile getHomeTiles(Alliance alliance) {
        for (HomeTile homeTile : allHomeTiles) {
            if (homeTile.getAlliance() == alliance) {
                return homeTile;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nHome:\n");
        for (HomeTile allHomeTile : allHomeTiles) {
            sb.append(allHomeTile).append("\n");
        }
        sb.append("\nTrack:\n");
        sb.append(allTrackTiles).append("\n");
        sb.append("\nHeaven:\n");
        for (HeavenTiles allHeavenTile : allHeavenTiles) {
            sb.append(allHeavenTile).append("\n");
        }
        return sb.toString();
    }
}