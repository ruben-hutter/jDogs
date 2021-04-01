package JDogs.ServerClientEnvironment.ServerSide;

/**
 * this enum contains all words used as commands
 * received by the server and which deal with
 * the game session.
 */
public enum ServerGameProtocol {


    /**
     * Command to leave the current game and return to the lobby.
     */
    EXIT,
    /**
     * Command to choose the game mode: Number of players, composition of the team, design of the board.
     */
    PLAY,
    /**
     * Command to make a move.
     * Parameters are: cardvalue, startposition, goalposition.
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
     * Command to provide information to actualize the whole board.
     */
    ANEW;


    /**
     *
     * @param text check if a string matches the list
     * @return if matches, else does not match
     */
    public static boolean isACommand(String text) {
        for (ServerGameProtocol command : ServerGameProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
    }

}
