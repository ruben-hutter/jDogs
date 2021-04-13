package jDogs;

import jDogs.board.Board;
import jDogs.board.Tile;
import jDogs.player.Player;
import java.util.Arrays;

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

        Piece pieceToMove = players[3].getPiece(1);
        Tile newPosition = board.allTrackTiles[pieceToMove.startingPosition];
        Tile oldPosition = pieceToMove.getPosition();
        pieceToMove.setPosition(newPosition);
        oldPosition.setPiece(null);
        newPosition.setPiece(pieceToMove);

        players[1].setPiece(1, board.allTrackTiles[players[1].startingPosition]);
        players[1].setPiece(1, board.allTrackTiles[70 % board.allTrackTiles.length]);
        players[3].setPiece(1, board.allHomeTiles.get(players[3].getAlliance())[0]);

        //Cards cards = new Cards();

        // prints out the player with the associated color and the actual board status
        for (Player player : players) {
            System.out.println(player);
        }
        System.out.println(board);
    }
}
