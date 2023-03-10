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
    LPUB,
    /**
     * send highScoreList to user
     */
    SCOR;

    /**
     * Gives the protocol command for a given string command
     * @param stringCommand the substring(0, 4) of a sent message
     * @return an existing command or null
     */
    public static ServerMenuProtocol toCommand(String stringCommand) {
        for (ServerMenuProtocol command : ServerMenuProtocol.values()) {
            if (command.toString().equals(stringCommand)) {
                return command;
            }
        }
        return null;
    }
}
