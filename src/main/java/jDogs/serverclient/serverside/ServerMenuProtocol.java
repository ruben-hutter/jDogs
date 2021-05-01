package jDogs.serverclient.serverside;

/**
 * this enum contains all words used as commands
 * received by the server from clients and which deal with the menu/lobby.
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
     * Command to send all the open games which can be joined to this client.
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
     * Command to delete ServerConnection of this client
     */
    EXIT,
    /**
     * Command to tell that a client wants to join the game.
     */
    JOIN,
    /**
     * Command to set game up in lobby
     */
    OGAM,
    /**
     * Send Client all active Users in Public Lobby
     */
    LPUB;

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
