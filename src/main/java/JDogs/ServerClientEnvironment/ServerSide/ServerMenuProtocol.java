package JDogs.ServerClientEnvironment.ServerSide;

//this protocol is an enum for all menu and lobby commands

public enum ServerMenuProtocol {

    USER,
    ACTI,
    SESS,
    WCHT,
    STAT,
    QUIT,
    STAR,
    PLAY,
    MODE,
    JOIN;



public static boolean isACommand(String text) {
    for (ServerMenuProtocol command : ServerMenuProtocol.values()) {
        if (command.toString().equals(text.substring(0, 4))) {
            return true;
        }
    }
    return false;
}

}
