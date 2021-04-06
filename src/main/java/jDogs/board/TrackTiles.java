package jDogs.board;

public class TrackTiles {

    Tile[] trackTiles;

    TrackTiles(int numOfPlayers) {
        trackTiles = createTrackTiles(numOfPlayers);
    }

    private Tile[] createTrackTiles(int numOfPlayers) {
        final Tile[] trackTiles = new Tile[numOfPlayers * Board.NUM_TRACK_TILES_X_PLAYER];
        for (int i = 0; i < trackTiles.length; i++) {
            trackTiles[i] = new Tile(i);
        }
        return trackTiles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : trackTiles) {
            sb.append(tile).append(" ");
        }
        return sb.toString();
    }
}
