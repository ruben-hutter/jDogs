package utils.JDogs.Board;
//represents the big board with 96 NormIndentations + 6*4 BoxIndentations + 6*4 GoalIndentations
public class BigBoard extends Board {

    public static final int BOARD_LENGTH = 0;
    public static final int BOARD_WIDTH = 0;

    Square[][] boardSquares;

    BigBoard() {
        this.boardSquares = new Square[BOARD_LENGTH][BOARD_WIDTH];
    }

    @Override
    public Tile[] getGameArray() {
        return new Tile[0];
    }
}

