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
            System.out.println("client game " + players[counter].getPlayerName());
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

    /**
     * Gives the player for a given alliance
     * @param alliance4 players alliance
     * @return a player
     */
    public Player getPlayer(Alliance_4 alliance4) {
        for (Player player : players) {
            if (player.getAlliance() == alliance4) {
                return player;
            }
        }
        return null;
    }

    /**
     * Changes the position of a piece on client side
     * @param player the player that changes a piece position
     * @param pieceID witch of the 4 pieces
     * @param newPosition where to put the piece
     */
    // TODO hasMoved
    public void changePiecePosition(Player player, int pieceID, String newPosition) {
        switch(newPosition.charAt(0)) {
            case 'A':
                player.changePositionClient(pieceID, board.allHomeTiles.
                        get(player.getAlliance())[pieceID - 1]);
                break;
            case 'B':
                player.changePositionClient(pieceID, board.allTrackTiles[Integer
                        .parseInt(newPosition.substring(1)) % board.allTrackTiles.length]);
                break;
            case 'C':
                player.changePositionClient(pieceID, board.allHeavenTiles.
                        get(player.getAlliance())[Integer.parseInt(newPosition.substring(1))]);
                break;
        }
    }
}
