package jDogs.serverclient.clientside;

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
     * Command to leave game or pendent game
     * ,because game terminated or pendent game was deleted by host
     */
    QUIT,
    /**
     * Command to show a list of all active users.
     */
    ACTI,
    /**
     * Command to start a new game.
     */
    PLAY,
    /**
     * Command to request the game statistics.
     */
    STAT,
    /**
     * Information that client received a public chat message to all players.
     */
    PCHT,
    /**
     * Information that client received a private message from a player.
     */
    WCHT,
    /**
     * Information that client received a message from a separate lobby he joins.
     */
    LCHT,
    /**
     * Information for host to start a new game.
     */
    STAR,
    /**
     * Information about a new or new active users in Lobby of a pendent game
     */
    JOIN,
    /**
     * Information update of new active user or all active users in lobby(at the beginning)
     * e.g. "LPUB username"
     */
    LPUB,
    /**
     * A new OpenGame to display is sent
     */
    OGAM,
    /**
     * Delete open game from lobby display
     */
    DOGA,
    /**
     * Delete a person from pendent game
     */
    DPER,
    /**
     * A new Game with this Client is started, get required data of game from server
     */
    GAME,
    /**
     * Common Information from Server
     */
    INFO;



    //HELP;

    /**
     *
     * @param text check if a string matches the list
     * @return true if matches, else does not match
     */
    public static boolean isACommand(String text) {
        int i = 8;
        for (ClientMenuProtocol command : ClientMenuProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
    }



}
