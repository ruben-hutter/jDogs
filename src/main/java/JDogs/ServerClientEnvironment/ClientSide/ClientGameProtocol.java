package JDogs.ServerClientEnvironment.ClientSide;

/**
 * this enum contains all words used as commands
 * received by the client and which deal with
 * the game session.
 *
 */
public enum ClientGameProtocol {


    PLAY,
    EXIT,
    MOVE,
    CTTP,
    TURN,
    ANEW;


    /**
     *
     * @param text check if a string matches the list
     * @return if matches, else does not match
     */
    public static boolean isACommand(String text) {
        for (ClientGameProtocol command : ClientGameProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
    }
}
