package jDogs.serverclient.clientside;

import jDogs.serverclient.serverside.ServerGameProtocol;

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
     * Command to leave open game
     * because pendent game was deleted by host
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
     * Information requesting the game statistics.
     */
    STAT,
    /**
     * Information that client receives a public chat message to all players.
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
     * received an String of all players in openGame
     */
    PLAR,
    /**
     * update of new player in separate lobby
     */
    PLYR,
    /**
     * update of deleted player in separate lobby
     */
    DPLR,
    /**
     * display new team combination
     */
    TEAM,
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
     * Common Information from Server
     */
    INFO;

    /**
     *
     * @param text check if a string matches the list
     * @return true if matches, else does not match
     */
    public static boolean isACommand(String text) {
        for (ClientMenuProtocol command : ClientMenuProtocol.values()) {
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
    public static ClientMenuProtocol toCommand(String stringCommand) {
        for (ClientMenuProtocol command : ClientMenuProtocol.values()) {
            if (command.toString().equals(stringCommand)) {
                return command;
            }
        }
        return null;
    }
}
