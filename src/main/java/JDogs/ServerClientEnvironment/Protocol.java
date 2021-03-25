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
                Protocol.manageCommand(text, command);
                return true;
            }
        }
        return false;
    }

    public static void manageCommand(String text, Protocol command) {
        switch (command) {
            case USER:
                System.out.println(USER);
                break;
            case PASS:
                System.out.println(PASS);
                break;
            case ACTI:
                System.out.println(ACTI);
                break;
            case QUIT:
                System.out.println(QUIT);
                break;
            case PLAY:
                System.out.println(PLAY);
                break;
            case STAT:
                System.out.println(STAT);
                break;
            case MODE:
                System.out.println(MODE);
                break;
            case WCHT:
                System.out.println(WCHT);
                break;
            case PCHT:
                System.out.println(PCHT);
                break;
            default:
                System.out.println("default");
        }
    }

    public static void test(String test) {
        System.out.println(test);
    }
}
