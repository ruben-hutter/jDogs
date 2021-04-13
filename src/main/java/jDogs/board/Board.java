package jDogs.board;

import jDogs.Alliance_4;
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

    public HashMap<Alliance_4, HomeTile[]> allHomeTiles;
    public TrackTile[] allTrackTiles;
    public HashMap<Alliance_4, HeavenTile[]> allHeavenTiles;

    public Board(int numOfPlayers) {
        allHomeTiles = createAllHomeTiles(numOfPlayers);
        allTrackTiles = createAllTrackTiles(numOfPlayers);
        allHeavenTiles = createAllHeavenTiles(numOfPlayers);
    }

    private HashMap<Alliance_4, HomeTile[]> createAllHomeTiles(int numOfPlayers) {
        allHomeTiles = new HashMap<>();
        allHomeTiles.put(Alliance_4.YELLOW, createHomeForAlliance(Alliance_4.YELLOW));
        allHomeTiles.put(Alliance_4.GREEN, createHomeForAlliance(Alliance_4.GREEN));
        allHomeTiles.put(Alliance_4.BLUE, createHomeForAlliance(Alliance_4.BLUE));
        allHomeTiles.put(Alliance_4.RED, createHomeForAlliance(Alliance_4.RED));
        return allHomeTiles;
    }

    private HomeTile[] createHomeForAlliance(Alliance_4 alliance4) {
        HomeTile[] homeTiles = new HomeTile[Board.NUM_HOME_TILES];
        for (int i = 0; i < homeTiles.length; i++) {
            homeTiles[i] = new HomeTile(i, alliance4);
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

    private HashMap<Alliance_4, HeavenTile[]> createAllHeavenTiles(int numOfPlayers) {
        allHeavenTiles = new HashMap<>();
        allHeavenTiles.put(Alliance_4.YELLOW, createAllHeavenTiles(Alliance_4.YELLOW));
        allHeavenTiles.put(Alliance_4.GREEN, createAllHeavenTiles(Alliance_4.GREEN));
        allHeavenTiles.put(Alliance_4.BLUE, createAllHeavenTiles(Alliance_4.BLUE));
        allHeavenTiles.put(Alliance_4.RED, createAllHeavenTiles(Alliance_4.RED));
        return allHeavenTiles;
    }

    private HeavenTile[] createAllHeavenTiles(Alliance_4 alliance4) {
        HeavenTile[] heavenTiles = new HeavenTile[Board.NUM_HEAVEN_TILES];
        for (int i = 0; i < heavenTiles.length; i++) {
            heavenTiles[i] = new HeavenTile(i, alliance4);
        }
        return heavenTiles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nHome:\n");
        for (Map.Entry<Alliance_4, HomeTile[]> homeTiles : allHomeTiles.entrySet()) {
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
        for (Map.Entry<Alliance_4, HeavenTile[]> heavenTiles : allHeavenTiles.entrySet()) {
            for (HeavenTile heavenTile : heavenTiles.getValue()) {
                sb.append(heavenTile);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}