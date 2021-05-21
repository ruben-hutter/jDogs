package jDogs.serverclient.helpers;

/**
 * Saves the Piece Alliances (color)
 * and the relative startingPosition on the Track
 * with 4 players.
 */
public enum Alliance_4 {

    YELLOW(0),
    GREEN(16),
    BLUE(32),
    RED(48);

    int startingPosition;

    Alliance_4(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    /**
     * Returns the starting position for this alliance.
     * @return 0, 16, 32 or 48
     */
    public int getStartingPosition() {
        return startingPosition;
    }
}
