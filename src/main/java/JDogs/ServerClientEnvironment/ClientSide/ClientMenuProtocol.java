package JDogs.ServerClientEnvironment.ClientSide;

/**
 * this enum contains all words used as commands
 * received by the client and which deal with
 * the menu/lobby.
 *
 */
public enum ClientMenuProtocol {

    USER,
    ACTI,
    QUIT,
    PLAY,
    STAT,
    MODE,
    WCHT,
    STAR,
    SESS,
    JOIN;

    //HELP;

    /**
     *
     * @param text check if a string matches the list
     * @return if matches, else does not match
     */
    public static boolean isACommand(String text) {
        for (ClientMenuProtocol command : ClientMenuProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
    }



}
