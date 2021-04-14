package jDogs;

/**
 * Saves the Piece Alliances (color)
 * and the relative startingPosition on the Track
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

    public int getStartingPosition() {
        return startingPosition;
    }
}
