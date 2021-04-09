package jDogs;

import jDogs.board.Board;
import jDogs.player.Player;

/**
 * Generates the board and the players for a game.
 */
public class Main {

    public static final int NUM_PLAYERS = 4;

    public static void main(String[] args) {

        // creates the board
        Board board = new Board(NUM_PLAYERS);

        // creates all the players (has to be created in the right order,
        // to give them the correct starting position
        Player[] players = new Player[NUM_PLAYERS];
        players[0] = new Player("player1", Alliance.YELLOW, board);
        players[1] = new Player("player2", Alliance.GREEN, board);
        players[2] = new Player("player3", Alliance.BLUE, board);
        players[3] = new Player("player4", Alliance.RED, board);

        //Cards cards = new Cards();

        // prints out the player with the associated color and the actual board status
        for (Player player : players) {
            System.out.println(player);
        }
        System.out.println(board);
    }
}
