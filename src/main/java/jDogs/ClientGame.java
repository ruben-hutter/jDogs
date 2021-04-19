package jDogs;

import jDogs.board.Board;
import jDogs.player.Player;
import java.util.ArrayList;

/**
 * Generates the board and the players for a game.
 */
public class ClientGame {

    private int numPlayers;
    private Board board;
    private Player[] players;
    private int turnNumber;
    private ArrayList<String> cards;

    public ClientGame(String[] playerNames) {
        numPlayers = playerNames.length;
        createGame(playerNames);

        for (int i = 0; i < playerNames.length; i++) {
            System.out.println(playerNames[i]);
        }
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
            System.out.print(player);
        }
        System.out.println(board);

    }

    /**
     * prints the cards the player holds
     */
    public void printCards() {
        for (String card : cards) {
            System.out.print(card + " ");
        }
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
    public void changePiecePosition(Player player, int pieceID, String newPosition) {

        switch(newPosition.substring(0, 1)) {
            case "A":
                player.changePositionClient(pieceID, board.allHomeTiles.
                        get(player.getAlliance())[pieceID - 1]);
                break;
            case "B":
                player.changePositionClient(pieceID, board.allTrackTiles[Integer
                        .parseInt(newPosition.substring(1))]);
                break;
            case "C":
                player.changePositionClient(pieceID, board.allHeavenTiles.
                        get(player.getAlliance())[Integer.parseInt(newPosition.substring(1))]);
                break;
        }
    }

    /**
     * Sets the cards for the game
     * @param substring the cards
     */
    public void setCards(String substring) {
        cards = getCardsArray(substring);
    }

    //String hand = number + " ACEE ACEE TENN TWOO EIGT NINE";
    private ArrayList<String> getCardsArray(String text) {

        int number = text.charAt(0) - 48;
        ArrayList<String> arrayList = new ArrayList<>();
        int position = 2;
        int count = 0;

        for (int i = 2; count < number - 1 && i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                arrayList.add(text.substring(position, i));
                position = i + 1;
                count++;
            }
        }
        arrayList.add(text.substring(position));
        return arrayList;
    }

    /**
     * Sets the actual turn number
     * @param i num between 6-2
     */
    private void setTurnNumber(int i) {
        turnNumber = i;
    }

    /**
     * Remove a card from the hand
     * @param card card to remove
     */
    public void remove(String card) {
        System.out.println("removed card " + card);
        System.out.println(cards.size());
        cards.remove(card);
        System.out.println(cards.size());
    }
}
