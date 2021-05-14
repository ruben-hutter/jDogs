package jDogs.serverclient.serverside;

/**
 * this enum contains all words used as commands
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
     * Command to choose the game mode: Number of players, composition of the team, design of the board.
     */
    PLAY,
    /**
     * Command to make a move.
     * Parameters are: cardvalue, piece-ID, goalposition.
     */
    MOVE,
    /**
     * Command to give a card to a player.
     */
    CTTP,
    /**
     * Command to deal cards to the players and tell them the number of the following round.
     */
    CARD,
    /**
     * Command to tell that a marble is sent home.
     */
    HOME,
    /**
     * Command to announce victory.
     */
    VICT,
    /**
     * Command to inform which player can now play.
     */
    TURN,
    /**
     * Stop Game and return to Lobby
     */
    STOP,
    /**
     * Command to provide information to actualize the whole board.
     */
    ANEW,

    // TODO check if necessary
    LCHT,
    PCHT;

    /**
     * Gives the protocol command for a given string command
     * @param stringCommand the substring(0, 4) of a sent message
     * @return an existing command or null
     */
    public static ServerGameProtocol toCommand(String stringCommand) {
        for (ServerGameProtocol command : ServerGameProtocol.values()) {
            if (command.equals(stringCommand)) {
                return command;
            }
        }
        return null;
    }
}
