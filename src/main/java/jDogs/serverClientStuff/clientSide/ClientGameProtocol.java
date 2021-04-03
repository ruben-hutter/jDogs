package jDogs.serverClientStuff.clientSide;

/**
 * this enum contains all words used as commands
 * received by the client and which deal with
 * the game session.
 *
 */
public enum ClientGameProtocol {

    /**
     * Command to start a new game.
     */
    PLAY,
    /**
     * Command to leave the current game and return to the lobby.
     */
    EXIT,
    /**
     * Command to make a move.
     * Parameters are: cardvalue, startposition, goalposition.
     */
    MOVE,
    /**
     * Command to give a card to a player.
     */
    CTTP,
    /**
     * Command to ask which player can play now.
     */
    TURN,
    /**
     * Command to ask for informations to actualize the whole board.
     */
    ANEW;


    /**
     *
     * @param text check if a string matches the list
     * @return if matches, else does not match
     */
    public static boolean isACommand(String text) {
        for (ClientGameProtocol command : ClientGameProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
    }
}
