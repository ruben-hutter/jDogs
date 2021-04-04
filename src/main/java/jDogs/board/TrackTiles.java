package jDogs.board;

public class TrackTiles {

    Tile[] trackTiles;

    TrackTiles() {
        trackTiles = createTrackTiles();
    }

    private Tile[] createTrackTiles() {
        final Tile[] trackTiles = new Tile[Board.NUM_TRACK_TILES];
        for (int i = 0; i < Board.NUM_TRACK_TILES; i++) {
            trackTiles[i] = new Tile(i);
        }
        return trackTiles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : trackTiles) {
            sb.append(tile + " ");
        }
        return sb.toString();
    }
}
