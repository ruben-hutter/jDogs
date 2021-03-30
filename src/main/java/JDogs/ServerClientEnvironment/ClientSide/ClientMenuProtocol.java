package JDogs.ServerClientEnvironment.ClientSide;

import JDogs.ServerClientEnvironment.ServerSide.ServerGameProtocol;

public enum ClientMenuProtocol {

    USER,
    ACTI,
    QUIT,
    PLAY,
    STAT,
    MODE,
    WCHT,
    STAR,
    SESS,
    JOIN;

    //HELP;


    public static boolean isACommand(String text) {
        for (ClientMenuProtocol command : ClientMenuProtocol.values()) {
            if (command.toString().equals(text.substring(0, 4))) {
                return true;
            }
        }
        return false;
    }



}
