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
    MOVE,
    STAT,
    MODE,
    WCHT,
    PCHT,
    STAR,
    CTTP,
    HELP;

    public static Protocol protocol;

    public static boolean isACommand(String text) {
        for (Protocol command : Protocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                protocol = command;
                return true;
            }
        }
        return false;
    }
}
