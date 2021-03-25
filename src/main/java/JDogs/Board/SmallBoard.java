package JDogs.Board;
//this file represents the small board
public class SmallBoard extends Board {
    //at least 16*16 Squares
    public static final int BOARD_LENGTH = 0;
    public static final int BOARD_WIDTH = 0;

    Square[][] boardSquares;

    SmallBoard() {
        this.boardSquares = new Square[BOARD_LENGTH][BOARD_WIDTH];
    }

    @Override
    public Tile[] getGameArray() {
        return new Tile[0];
    }

}
