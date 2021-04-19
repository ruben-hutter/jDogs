package jDogs;

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

    /**
     * Give an alliance for a starting position
     * @param startingPosition 0, 16, 32 or 48
     * @return alliance
     */
    public Alliance_4 getAlliance(int startingPosition) {
        for (Alliance_4 alliance4 : Alliance_4.values()) {
            if (this.startingPosition == startingPosition) {
                return alliance4;
            }
        }
        return null;
    }
}
