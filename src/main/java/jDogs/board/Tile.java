package jDogs.board;

import com.google.common.collect.ImmutableMap;
import jDogs.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

/**
 * Tile is a square where a token can be placed
 */
public abstract class Tile {

    protected final int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }
    
    public static Tile createTile(final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    /**
     * Creates all possible tiles that exist on the game board
     * @return Map containing Integer references to EmptyTiles
     */
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        // Collections.unmodifiableMap(emptyTileMap);
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();
}
