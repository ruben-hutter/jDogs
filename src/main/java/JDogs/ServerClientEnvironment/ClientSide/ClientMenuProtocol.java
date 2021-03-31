package JDogs.ServerClientEnvironment.ClientSide;

/**
 * this enum contains all words used as commands
 * received by the client and which deal with
 * the menu/lobby.
 *
 */
public enum ClientMenuProtocol {
    /**
     * Command to set username.
     */
    USER,
    /**
     * Command to show a list of active users.
     */
    ACTI,
    /**
     * Command to disconnect client from the server.
     */
    QUIT,
    /**
     * Command to start a new game.
     */
    PLAY,
    /**
     * Command to request the game statistics.
     */
    STAT,
    /**
     * Command to choose the game mode: Number of players, composition of the team, design of the board.
     */
    MODE,
    /**
     * Command to send a private message to a player.
     */
    WCHT,
    /**
     * Command to start a new game.
     */
    STAR,
    /**
     *Command to show the open games which can be joined.
     */
    SESS,
    /**
     * Command to join a game.
     */
    JOIN;

    //HELP;

    /**
     *
     * @param text check if a string matches the list
     * @return if matches, else does not match
     */
    public static boolean isACommand(String text) {
        for (ClientMenuProtocol command : ClientMenuProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
    }



}
