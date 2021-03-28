package JDogs.ServerClientEnvironment;

/**
 * This protocol defines the commands to make the client and
 * server interact with each other correctly.
 */
public enum Protocol {
    USER,
    PASS,
    ACTI,
    QUIT,
    EXIT,
    PLAY,
    MOVE,
    STAT,
    MODE,
    WCHT,
    PCHT,
    STAR,
    CTTP,
    HELP;

    //public static Protocol protocol;

    /**
     * Checks if a given message refers to a Protocol field.
     * @param text input given by user client
     * @return true if the command is in Protocol
     */
    public static boolean isACommand(String text) {
        for (Protocol command : Protocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                //protocol = command;
                return true;
            }
        }
        return false;
    }
}
