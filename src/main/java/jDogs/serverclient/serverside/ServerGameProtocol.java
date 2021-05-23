package jDogs.serverclient.serverside;

/**
 * This enum contains all words used as commands
 * received by the server and which deal with
 * the game session.
 */
public enum ServerGameProtocol {

    /**
     * Command to leave game and shutdown ServerConnection
     */
    EXIT,
    /**
     * Command to leave the current game and return to the lobby.
     */
    QUIT,
    /**
     * Command to make a move.
     * Parameters are: cardvalue, piece-ID, goalposition.
     */
    MOVE,
    /**
     * Cheat command to move a piece where ever you want.
     */
    MOPS,
    /**
     * send a message to participants of game
     */
    PCHT;

    /**
     * Gives the protocol command for a given string command
     * @param stringCommand the substring(0, 4) of a sent message
     * @return an existing command or null
     */
    public static ServerGameProtocol toCommand(String stringCommand) {
        for (ServerGameProtocol command : ServerGameProtocol.values()) {
            if (command.toString().equals(stringCommand)) {
                return command;
            }
        }
        return null;
    }
}
