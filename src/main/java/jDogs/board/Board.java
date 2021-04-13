package jDogs.board;

import jDogs.Alliance;
import java.util.HashMap;
import java.util.Map;

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

    public HashMap<Alliance, HomeTile[]> allHomeTiles;
    public TrackTile[] allTrackTiles;
    public HashMap<Alliance, HeavenTile[]> allHeavenTiles;

    public Board(int numOfPlayers) {
        allHomeTiles = createAllHomeTiles(numOfPlayers);
        allTrackTiles = createAllTrackTiles(numOfPlayers);
        allHeavenTiles = createAllHeavenTiles(numOfPlayers);
    }

    private HashMap<Alliance, HomeTile[]> createAllHomeTiles(int numOfPlayers) {
        allHomeTiles = new HashMap<>();
        allHomeTiles.put(Alliance.YELLOW, createHomeForAlliance(Alliance.YELLOW));
        allHomeTiles.put(Alliance.GREEN, createHomeForAlliance(Alliance.GREEN));
        if (numOfPlayers == 6) {
            allHomeTiles.put(Alliance.WHITE, createHomeForAlliance(Alliance.WHITE));
            allHomeTiles.put(Alliance.BLUE, createHomeForAlliance(Alliance.BLUE));
            allHomeTiles.put(Alliance.RED, createHomeForAlliance(Alliance.RED));
            allHomeTiles.put(Alliance.BLACK, createHomeForAlliance(Alliance.BLACK));
            return allHomeTiles;
        }
        allHomeTiles.put(Alliance.BLUE, createHomeForAlliance(Alliance.BLUE));
        allHomeTiles.put(Alliance.RED, createHomeForAlliance(Alliance.RED));
        return allHomeTiles;
    }

    private HomeTile[] createHomeForAlliance(Alliance alliance) {
        HomeTile[] homeTiles = new HomeTile[Board.NUM_HOME_TILES];
        for (int i = 0; i < homeTiles.length; i++) {
            homeTiles[i] = new HomeTile(i, alliance);
        }
        return homeTiles;
    }

    private TrackTile[] createAllTrackTiles(int numOfPlayers) {
        TrackTile[] trackTiles = new TrackTile[NUM_TRACK_TILES_X_PLAYER * numOfPlayers];
        for (int i = 0; i < trackTiles.length; i++) {
            trackTiles[i] = new TrackTile(i);
        }
        return trackTiles;
    }

    private HashMap<Alliance, HeavenTile[]> createAllHeavenTiles(int numOfPlayers) {
        allHeavenTiles = new HashMap<>();
        allHeavenTiles.put(Alliance.YELLOW, createAllHeavenTiles(Alliance.YELLOW));
        allHeavenTiles.put(Alliance.GREEN, createAllHeavenTiles(Alliance.GREEN));
        if (numOfPlayers == 6) {
            allHeavenTiles.put(Alliance.WHITE, createAllHeavenTiles(Alliance.WHITE));
            allHeavenTiles.put(Alliance.BLUE, createAllHeavenTiles(Alliance.BLUE));
            allHeavenTiles.put(Alliance.RED, createAllHeavenTiles(Alliance.RED));
            allHeavenTiles.put(Alliance.BLACK, createAllHeavenTiles(Alliance.BLACK));
            return allHeavenTiles;
        }
        allHeavenTiles.put(Alliance.BLUE, createAllHeavenTiles(Alliance.BLUE));
        allHeavenTiles.put(Alliance.RED, createAllHeavenTiles(Alliance.RED));
        return allHeavenTiles;
    }

    private HeavenTile[] createAllHeavenTiles(Alliance alliance) {
        HeavenTile[] heavenTiles = new HeavenTile[Board.NUM_HOME_TILES];
        for (int i = 0; i < heavenTiles.length; i++) {
            heavenTiles[i] = new HeavenTile(i, alliance);
        }
        return heavenTiles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nHome:\n");
        for (Map.Entry<Alliance, HomeTile[]> homeTiles : allHomeTiles.entrySet()) {
            for (HomeTile homeTile : homeTiles.getValue()) {
                sb.append(homeTile);
            }
            sb.append("\n");
        }
        sb.append("\nTrack:\n");
        for (TrackTile trackTile : allTrackTiles) {
            sb.append(trackTile);
        }
        sb.append("\n");
        sb.append("\nHeaven:\n");
        for (Map.Entry<Alliance, HeavenTile[]> heavenTiles : allHeavenTiles.entrySet()) {
            for (HeavenTile heavenTile : heavenTiles.getValue()) {
                sb.append(heavenTile);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}