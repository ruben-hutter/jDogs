package jDogs.board;

import jDogs.serverclient.helpers.Alliance_4;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates board initial setup
 */
public class Board {

    public static final int NUM_TRACK_TILES_X_PLAYER = 16;
    public static final int NUM_HEAVEN_TILES = 4;
    public static final int NUM_HOME_TILES = 4;

    public HashMap<Alliance_4, HomeTile[]> allHomeTiles;
    public TrackTile[] allTrackTiles;
    public HashMap<Alliance_4, HeavenTile[]> allHeavenTiles;

    public Board(int numOfPlayers) {
        allHomeTiles = createAllHomeTiles();
        allTrackTiles = createAllTrackTiles(numOfPlayers);
        allHeavenTiles = createAllHeavenTiles();
    }

    /**
     * Creates 4 home tile arrays in a map
     * @return hashmap: k = alliance, v = heaven tile array
     */
    private HashMap<Alliance_4, HomeTile[]> createAllHomeTiles() {
        allHomeTiles = new HashMap<>();
        for (Alliance_4 alliance4 : Alliance_4.values()) {
            allHomeTiles.put(alliance4, createHomeForAlliance(alliance4));
        }
        return allHomeTiles;
    }

    /**
     * Creates home tiles for a player
     * @param alliance4 enum with 4 alliances
     * @return home tiles array
     */
    private HomeTile[] createHomeForAlliance(Alliance_4 alliance4) {
        HomeTile[] homeTiles = new HomeTile[Board.NUM_HOME_TILES];
        for (int i = 0; i < homeTiles.length; i++) {
            homeTiles[i] = new HomeTile(i, alliance4);
        }
        return homeTiles;
    }

    /**
     * Creates the correct number of track tiles
     * @param numOfPlayers the number of players in the game
     * @return an array of 64 or 80 track tiles
     */
    private TrackTile[] createAllTrackTiles(int numOfPlayers) {
        TrackTile[] trackTiles = new TrackTile[NUM_TRACK_TILES_X_PLAYER * numOfPlayers];
        for (int i = 0; i < trackTiles.length; i++) {
            trackTiles[i] = new TrackTile(i);
        }
        return trackTiles;
    }

    /**
     * Creates 4 heaven tile arrays in a map
     * @return hashmap: k = alliance, v = heaven tile array
     */
    private HashMap<Alliance_4, HeavenTile[]> createAllHeavenTiles() {
        allHeavenTiles = new HashMap<>();
        for (Alliance_4 alliance4 : Alliance_4.values()) {
            allHeavenTiles.put(alliance4, createAllHeavenTiles(alliance4));
        }
        return allHeavenTiles;
    }

    /**
     * Creates heaven tiles for a player
     * @param alliance4 enum with 4 alliances
     * @return heaven tiles array
     */
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