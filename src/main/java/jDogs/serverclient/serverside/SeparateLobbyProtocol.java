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
     * Command to return to stop joining game
     */
    QUIT,
    /**
     * Command to confirm whether the game should be started under the given conditions.
     */
    STAR;


}
