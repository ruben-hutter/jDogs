package jDogs.serverclient.clientside;

/**
 * This enum contains all words used as commands
 * received by the client and which deal with
 * the game session.
 */
public enum ClientGameProtocol {

    /**
     * Command to start a new game.
     */
    GAME,
    /**
     * Command to leave the current game and return to the lobby.
     */
    EXIT,
    /**
     * Information about other player who made a move.
     * Parameters are: piece-ID, destination.
     */
    MOVE,
    /**
     *Information about a jack move which cannot be displayed as a simple move
     * used Parameters are: color-pieceID1, color-pieceID2
     *
     */
    JACK,
    /**
     * Command to print out actual board state
     */
    BORD,
    /**
     * Information which card is removed on serverside from your hand
     */
    CARD,
    /**
     * Command to print out actual hand
     */
    HAND,
    /**
     * Command to give a card to this player.
     */
    CTTP,
    /**
     * without name: Command to tell this player can play now.
     * with name: Information which player`s turn it is
     */
    TURN,
    /**
     * Client receives the hand of cards of the upcoming round
     */
    ROUN,
    /**
     * Information to actualize the whole board.
     */
    ANEW,
    /**
     * Information about a winner or winning team
     */
    VICT,
    /**
     * Information about the error in the executed move
     */
    FAIL,

    // TODO delete this
    STOP;


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

    /**
     * Gives the protocol command for a given string command
     * @param stringCommand the substring(0, 4) of a sent message
     * @return an existing command or null
     */
    public static ClientGameProtocol toCommand(String stringCommand) {
        for (ClientGameProtocol command : ClientGameProtocol.values()) {
            if (command.toString().equals(stringCommand)) {
                return command;
            }
        }
        return null;
    }
}
