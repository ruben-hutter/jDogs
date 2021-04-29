package jDogs.serverclient.serverside;

public enum SeparateLobbyProtocol {

    /**
     * Command to show a list of active users.
     */
    ACTI,
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
     * Command to return to stop joining game and return to lobby
     */
    QUIT,
    /**
     * Command to stop joining game and kill ServerConnection
     */
    EXIT,
    /**
     * Command to confirm whether the game should be started under the given conditions.
     */
    STAR,
    /**
     * Command send to this player all fellow participants of separate lobby
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
