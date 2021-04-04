package jDogs;

import jDogs.board.Board;
import jDogs.board.HomeTiles;
import jDogs.player.Player;

/**
 * This Main class must be cleaned up.
 * The process of building the players and
 * associating them to the home tiles
 * has to be done in an other class
 */
public class Main {

    public static final int NUM_PLAYERS = 4;

    public static void main(String[] args) {

        // creates the board
        Board board = new Board();

        // creates all the players
        Player[] players = new Player[NUM_PLAYERS];
        players[0] = new Player("player1", Alliance.YELLOW);
        players[1] = new Player("player2", Alliance.GREEN);
        players[2] = new Player("player3", Alliance.BLUE);
        players[3] = new Player("player4", Alliance.RED);

        // sets the players pieces to the home tiles
        setPlayerToHome(players, board);

        //Cards cards = new Cards();

        // prints out the player with the associated color and the actual board status
        for (Player player : players) {
            System.out.println(player);
        }
        System.out.println(board);
    }

    public static void setPlayerToHome(Player[] players, Board board) {
        Piece[] piecesPlayerI;
        HomeTiles homeTilesPlayerI;
        for (int i = 0; i < players.length; i++) {
            piecesPlayerI = players[i].getPieces();
            homeTilesPlayerI = board.getHomeTiles(players[i].getAlliance());
            for (int j = 0; j < Board.NUM_HOME_TILES; j++) {
                homeTilesPlayerI.getHomeTile(j).setPiece(piecesPlayerI[j]);
            }
        }
    }
}
