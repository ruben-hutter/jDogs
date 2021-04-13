package jDogs;

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

    public int getStartingPosition() {
        return startingPosition;
    }
}
