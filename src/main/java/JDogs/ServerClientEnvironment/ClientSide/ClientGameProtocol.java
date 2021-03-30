package JDogs.ServerClientEnvironment.ClientSide;


public enum ClientGameProtocol {


    PLAY,
    EXIT,
    MOVE,
    CTTP,
    TURN,
    ANEW;




    public static boolean isACommand(String text) {
        for (ClientGameProtocol command : ClientGameProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
    }
}
