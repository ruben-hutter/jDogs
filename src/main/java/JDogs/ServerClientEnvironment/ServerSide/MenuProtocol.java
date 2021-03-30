package JDogs.ServerClientEnvironment.ServerSide;

//this protocol is an enum for all menu and lobby commands

public enum MenuProtocol {

    USER,
    ACTI,
    QUIT,
    PLAY,
    STAT,
    MODE,
    WCHT,
    STAR;
    //HELP;



public static boolean isACommand(String text) {
    for (MenuProtocol command : MenuProtocol.values()) {
        if (command.toString().equals(text.substring(0, 4))) {
            return true;
        }
    }
    return false;
}

}
