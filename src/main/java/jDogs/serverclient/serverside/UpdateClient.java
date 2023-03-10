package jDogs.serverclient.serverside;

import java.util.ArrayList;

public class UpdateClient {

    private int returnValue;
    private final ArrayList<String> moves;
    private final String[] updateGame;
    private String winners;

    public UpdateClient() {
        moves = new ArrayList<>();
        updateGame = new String[] {"BORD", "CARD ", "HAND"};
    }

    /**
     * Sets the return value for this object
     * @param returnValue 0 if move is done correctly, greater than if move not possible
     */
    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * Gets the return value
     * @return 0 if move is done correctly, greater than if move not possible
     */
    public int getReturnValue() {
        return returnValue;
    }

    /**
     * Add a given move to the existing list
     * @param newMove MOVE + piece + newPosition
     */
    public void addToMoves(String newMove) {
        moves.add(newMove);
    }

    /**
     * Gets the moves that are sent to clients
     * @return a list of moves or an empty list
     */
    public ArrayList<String> getMoves() {
        return moves;
    }

    /**
     * Appends a card to the existing CARD command
     * @param card the played card
     */
    public void addCardToEliminate(String card) {
        updateGame[1] += card;
    }

    /**
     * Gets the updateGame messages
     * @return an array with the messages to finish a turn
     */
    public String[] getUpdateGame() {
        return updateGame;
    }

    /**
     * Sets the name of the winner/s
     * @param winners 1 or 2 names of winner/s
     */
    public void setWinners(String winners) {
        this.winners = winners;
    }

    /**
     * Gets the winner/s
     * @return 1 or 2 names of winner/s or null
     */
    public String getWinners() {
        return winners;
    }
}
