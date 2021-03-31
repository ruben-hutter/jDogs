package JDogs.ServerClientEnvironment.ServerSide;

/**
 * this enum contains all words used as commands
 * received by the server and which deal with the menu/lobby.
 */
public enum ServerMenuProtocol {

    USER,
    ACTI,
    SESS,
    PCHT,
    WCHT,
    STAT,
    QUIT,
    STAR,
    PLAY,
    MODE,
    JOIN;


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
