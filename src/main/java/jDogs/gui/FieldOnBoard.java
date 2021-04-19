package jDogs.gui;

/**
 * this class represents a 2D-field on the gui board
 * with an x value and y value
 */
public class FieldOnBoard {
    private int x;
    private int y;

   public FieldOnBoard(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
