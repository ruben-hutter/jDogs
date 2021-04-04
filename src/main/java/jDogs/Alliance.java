package jDogs;

/**
 * Saves the Piece Alliances (color)
 * and the relative startingPosition on the Track
 */
public enum Alliance {

    YELLOW(0),
    GREEN(16),
    BLUE(32),
    RED(48);

    private int startingPosition;

    Alliance(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    /**
     * Returns the starting position of the selected color
     * @return starting position as int
     */
    public int getStartingPosition() {
        return startingPosition;
    }
}
