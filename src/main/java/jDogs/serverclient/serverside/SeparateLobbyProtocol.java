package jDogs.serverclient.serverside;

public enum SeparateLobbyProtocol {

    /**
     * Command to send a public chat message to all players
     */
    PCHT,
    /**
     * Command to send a private message to a player.
     */
    WCHT,
    /**
     * Command to return to stop joining game and return to lobby
     */
    QUIT,
    /**
     * Command to stop joining game and delete ServerConnection
     */
    EXIT,
    /**
     * Command to confirm whether the game should be started under the given conditions.
     */
    STAR,
    /**
     * Command send to this player all fellow participants of separate lobby
     */
    LPUB,
    /**
     * receive a new team combination from a client
     */
    TEAM,
    /**
     * send highScoreList to user
     */
    SCOR;

    /**
     * Gives the protocol command for a given string command
     * @param stringCommand the substring(0, 4) of a sent message
     * @return an existing command or null
     */
    public static SeparateLobbyProtocol toCommand(String stringCommand) {
        for (SeparateLobbyProtocol command : SeparateLobbyProtocol.values()) {
            if (command.toString().equals(stringCommand)) {
                return command;
            }
        }
        return null;
    }
}
