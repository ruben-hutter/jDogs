package jDogs;

/**
 * Saves the Piece Alliances (color)
 * and the relative startingPosition on the Track
 * with 6 players.
 */
public enum Alliance_6 {

    YELLOW(0),
    GREEN(16),
    WHITE(32),
    BLUE(48),
    RED(64),
    BLACK(80);

    int startingPosition;

    Alliance_6(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    /**
     * Returns the starting position for this alliance.
     * @return 0, 16, 32, 48, 64 or 80
     */
    public int getStartingPosition() {
        return startingPosition;
    }
}
