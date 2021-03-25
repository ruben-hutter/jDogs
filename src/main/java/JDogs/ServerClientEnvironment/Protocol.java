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

    public static void main(String[] args) {

        Protocol protocol = Protocol.USER;  //TODO assign to user input

        switch (protocol.ordinal()) {

            case 0:
                System.out.println("Give me your username: ");
                break;
            case 1:
                System.out.println("Give me your password: ");
                break;
            default:
                System.out.println("Type a valid command!");
        }
    }
}
