package JDogs.ServerClientEnvironment.ServerSide;

//this is an enum for all game commands
public enum ServerGameProtocol {



    EXIT,
    PLAY,
    MOVE,
    CTTP,
    CARD,
    HOME,
    VICT,
    TURN,
    ANEW;



public static boolean isACommand(String text) {
    for (ServerGameProtocol command : ServerGameProtocol.values()) {
        if (command.toString().equals(text.substring(0, 4))) {
            return true;
        }
    }
    return false;
}

}
