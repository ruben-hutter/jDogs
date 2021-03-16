package utils.JDogs.Board;
//this file represents the small board
public class SmallBoard implements BoardAPI {

    public static final int BOARD_LENGTH = 0;
    public static final int BOARD_WIDTH = 0;

    Square[][] boardSquares;

    SmallBoard() {
        this.boardSquares = new Square[BOARD_LENGTH][BOARD_WIDTH];
    }

    @Override
    public boolean isOccupied() {
        return false;
    }

    @Override
    public void update() {

    }
}
