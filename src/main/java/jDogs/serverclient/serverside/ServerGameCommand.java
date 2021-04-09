package jDogs.serverclient.serverside;

/**
 * ServerGameCommand contains the game
 * commands which are sent from the
 * clients to communicate with the
 * server.
 *
 */

public class ServerGameCommand {

    public ServerGameCommand() {
        // TODO
    }

    public void execute(String text) {
        String command = text.substring(0,4);

        switch(command) {
            case "QUIT":
                // TODO stop ServerConnection and Client
            case "EXIT":
                // TODO startExit();
                //finish game
                break;
            case "MOVE":
                // TODO startMove()
                // Server: give it to GameEngine if move is according to the rules
                break;

            case "CTTP":
                // TODO start CTTP
                //change cards
                break;
        }

    }


    /**
     *
     * @param actualGame is the ongoing game which should be found
     * @return the game or null
     */

    private MainGame getRunningGame(String actualGame) {
        for (int i = 0; i < Server.getInstance().runningGames.size(); i++) {
            if (Server.getInstance().runningGames.get(i).getGameId() == actualGame) {
                return Server.getInstance().runningGames.get(i);
            }
        }
        return null;
    }

}
