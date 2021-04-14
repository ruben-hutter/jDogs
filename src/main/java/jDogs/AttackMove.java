package jDogs;

import jDogs.board.Board;

public class AttackMove extends Move {

    final Piece attackedPiece;

    AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
        super(board, movedPiece, destinationCoordinate);
        this.attackedPiece = attackedPiece;
    }
}
