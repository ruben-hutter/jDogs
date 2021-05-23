package jDogs.serverclient.serverside;

/**
 * this class helps to parse information sent from
 * client to server
 */
public class ServerParser {

    private final ServerConnection serverConnection;

    /**
     * constructor of an object of serverParser
     * @param serverConnection scObject
     */
    ServerParser(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * sets up a new open game
     * @param gameSetup required information from server about new open game
     * @return a gameFile in which most information about the game is saved
     */
    public OpenGameFile setUpGame(String gameSetup) {

        try {
            int nameSeparator = 0;

            for (int i = 0; i < gameSetup.length(); i++) {
                if (Character.isWhitespace(gameSetup.charAt(i))) {
                    nameSeparator = i;
                    break;
                }
            }
            if (nameSeparator == 0) {
                return null;
            }
            String name = gameSetup.substring(0,nameSeparator);

            // team mode 0 == false
            // team mode 1 == true
            int teamMode = gameSetup.charAt(nameSeparator + 1) - 48;

                String host = serverConnection.getNickname();
                name = checkName(name);

                if (checkHost(host)) {
                   return new OpenGameFile(name, host, teamMode, serverConnection);
                }
                System.err.println("Gamehost " + host
                                    + " already exists in server.allGamesNotFinished!");
                return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * this method checks if the host is already in a game involved
     * (but this method will be deprecated)
     * @param host the one who opened this opengame
     * @return true, if host is already in a game involved
     */
    //TODO delete method
    private boolean checkHost(String host) {
        for (OpenGameFile openGameFile : Server.getInstance().getOpenGameList()) {
            if (openGameFile.getHost().equals(host)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param name name of the new open game
     * @return modified name if name is already used
     * or original name if not
     */
    private String checkName(String name) {
        int problem = -1;
        for (String gameName : Server.getInstance().getAllGamesNotFinishedNames()) {
            if (gameName.equals(name)) {
                int num = 1;
                do {
                    num++;
                } while (gameName.equals(name + num));
                return name + num;
            }
        }
        return name;
    }
}
