package jDogs.gui;

/**
 * this class represents a 2D-field on the gui board
 * with an x value and y value
 */
public class FieldOnBoard {
    private int x;
    private int y;

    /**
     * constructor of FieldOnBoard
     * @param x x-Position
     * @param y y-Position
     */
   public FieldOnBoard(int x,int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * return x-value
     * @return x-value
     */
    public int getX() {
        return x;
    }

    /**
     * return y-value
     * @return y-value
     */
    public int getY() {
        return y;
    }
}
