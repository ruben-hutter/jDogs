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
    PLAY,
    STAT,
    MODE,
    WCHT,
    PCHT;

    public static boolean isACommand(String text) {
        for (Protocol command : Protocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                Protocol.manageCommand(text, command.ordinal());
                return true;
            }
        }
        return false;
    }

    public static void manageCommand(String text, int ordinal) {

        switch (ordinal) {

            case 0:
                System.out.println(0);
                break;
            case 1:
                System.out.println(1);
                break;
            default:
                System.out.println("default");
        }
    }

    public static void test(String test) {
        System.out.println(test);
    }
}
