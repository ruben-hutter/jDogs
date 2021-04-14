package jDogs;

import jDogs.board.Board;
import jDogs.player.Player;

/**
 * Generates the board and the players for a game.
 */
public class ClientGame {

    public int numPlayers;
    Board board;
    Player[] players;

    public ClientGame(String[] playerNames) {
        numPlayers = playerNames.length;
        createGame(playerNames);
    }

    /**
     * Creates a game for the given players
     * @param playerNames a String that contains all the names
     */
    private void createGame(String[] playerNames) {
        // creates the board
        board = new Board(numPlayers);

        // creates all the players
        players = new Player[numPlayers];
        int counter = 0;
        for (Alliance_4 alliance4 : Alliance_4.values()) {
            players[counter] = new Player(playerNames[counter], alliance4, board);
            counter++;
        }
    }

    /**
     * Prints the actual game state
     */
    public void printGameState() {
        // prints out the player with the associated color and the actual board status
        for (Player player : players) {
            System.out.println(player);
        }
        System.out.println(board);
    }
}
