package jDogs.serverclient.serverside;

/**
 * this enum contains all words used as commands
 * received by the server and which deal with the menu/lobby.
 */
public enum ServerMenuProtocol {

    /**
     * Command to set username.
     */
    USER,
    /**
     * Command to show a list of active users.
     */
    ACTI,
    /**
     * Command to show the open games which can be joined.
     */
    SESS,
    /**
     * Command to send a public chat message to all players
     */
    PCHT,
    /**
     * Command to send a private message to a player.
     */
    WCHT,
    /**
     * Command to request the game statistics.
     */
    STAT,
    /**
     * Command to disconnect client from the server.
     */
    QUIT,
    /**
     * Command to confirm whether the game should be started under the given conditions.
     */
    STAR,
    /**
     * Command to start a new game.
     */
    PLAY,
    /**
     * Command to choose the game mode: Number of players, composition of the team, design of the board.
     */
    MODE,
    /**
     * Command to tell if a client has joined the game or not.
     */
    JOIN;




    /**
     *
     * @param text check if a string matches the list
     * @return if matches, else does not match
     */
    public static boolean isACommand(String text) {
        for (ServerMenuProtocol command : ServerMenuProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
}

}
