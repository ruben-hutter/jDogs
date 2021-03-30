package JDogs.ServerClientEnvironment.ServerSide;

/**
 * this enum contains all words used as commands
 * received by the server and which deal with
 * the game session.
 */
public enum ServerGameProtocol {



    EXIT,
    PLAY,
    MOVE,
    CTTP,
    CARD,
    HOME,
    VICT,
    TURN,
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
