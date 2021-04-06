package jDogs;

/**
 * Saves the Piece Alliances (color)
 * and the relative startingPosition on the Track
 */
public enum Alliance {

    YELLOW(0),
    GREEN(16),
    BLUE(32),
    RED(48),
    WHITE(80),
    BLACK(90);

    private int startingPosition;

    // TODO find a way not to give a fix value to a color (increase it by 16 for every new color)
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
