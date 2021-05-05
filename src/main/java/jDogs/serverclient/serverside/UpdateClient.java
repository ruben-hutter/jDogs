package jDogs.serverclient.serverside;

import java.util.ArrayList;

public class UpdateClient {

    private int returnValue;
    private ArrayList<String> simpleMoves;
    private ArrayList<String> piecesToEliminate;

    public UpdateClient() {
        returnValue = -1;
        simpleMoves = new ArrayList<>();
        piecesToEliminate = new ArrayList<>();
    }

    /**
     * Sets the return value for this object
     * @param returnValue 0 if move is done correctly, x for x > 0 if move not possible
     */
    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * Gets the return value
     * @return 0 if move is done correctly, x for x > 0 if move not possible
     */
    public int getReturnValue() {
        return returnValue;
    }

    /**
     * Add a given move to the existing list
     * @param newMove MOVE + piece + newPosition
     */
    public void addToSimpleMoves(String newMove) {
        simpleMoves.add(newMove);
    }

    /**
     * Gets the moves that are sent to clients
     * @return a list of moves or an empty list
     */
    public ArrayList<String> getSimpleMoves() {
        return simpleMoves;
    }

    /**
     * Add a given elimination move to the existing list
     * @param newEliminationMove MOVE + piece + homePosition
     */
    public void addToPiecesToEliminate(String newEliminationMove) {
        piecesToEliminate.add(newEliminationMove);
    }

    /**
     * Gets the moves to eliminate pieces that are sent to clients
     * @return a list of moves or an empty list
     */
    public ArrayList<String> getPiecesToEliminate() {
        return piecesToEliminate;
    }
}
