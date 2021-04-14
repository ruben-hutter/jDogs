package jDogs;

import jDogs.board.Board;

public class MajorMove extends Move {

    MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        super(board, movedPiece, destinationCoordinate);
    }
}
