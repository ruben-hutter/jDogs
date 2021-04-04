package jDogs.player;

import jDogs.Alliance;
import jDogs.Piece;
import jDogs.board.Board;

public class Player {

    Piece[] pieces;
    String playerName;
    Alliance alliance;

    public Player(String playerName, Alliance alliance) {
        this.playerName = playerName;
        this.alliance = alliance;
        pieces = createPieces();
    }

    private Piece[] createPieces() {
        Piece[] pieces = new Piece[Board.NUM_HOME_TILES];
        for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
            pieces[i] = new Piece(alliance, i + 1);
        }
        return pieces;
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    @Override
    public String toString() {
        return getPlayerName() + ": " + getAlliance();
    }
}