package JDogs.ServerClientEnvironment.ServerSide;

//this is an enum for all game commands
public enum GameProtocol {



    EXIT,
    PLAY,
    MOVE,
    CTTP;



public static boolean isACommand(String text) {
    for (GameProtocol command : GameProtocol.values()) {
        if (command.toString().equals(text.substring(0, 4))) {
            return true;
        }
    }
    return false;
}

}
