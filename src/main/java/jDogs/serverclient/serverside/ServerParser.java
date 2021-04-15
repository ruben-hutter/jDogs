package jDogs.serverclient.serverside;

public class ServerParser {

    private Server server;
    private ServerConnection serverConnection;

    ServerParser(Server server, ServerConnection serverConnection) {
        this.server = server;
        this.serverConnection = serverConnection;
    }

    public GameFile setUpGame(String gameSetup) {

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
        String total = gameSetup.substring(nameSeparator + 1);
        int number = Integer.parseInt(total);
        // allow only games with 4 or 6 players
        if (number == 4 || number == 6) {

            String host = serverConnection.getNickname();
            name = checkName(name);

            if (checkHost(host)) {
               // TODO change this
               // return new GameFile(name, host, total, serverConnection, );
            }
            System.err.println("Gamehost " + host
                                + " already exists in server.allGamesNotFinished!");
        }
        return null;
    }

    private boolean checkHost(String host) {
        for (int i = 0; i < server.allGamesNotFinished.size(); i++) {
            if (server.allGamesNotFinished.get(i).getHost().equals(host)) {
                return false;
            }
        }
        return true;
    }


    private String checkName(String name) {
        int problem = -1;
        for (int i = 0; i < server.allGamesNotFinished.size(); i++) {
            if (server.allGamesNotFinished.get(i).getNameId().equals(name)) {
                int num = 1;
                while (true) {
                    num++;
                    if (!server.allGamesNotFinished.get(i).getNameId().equals(name + num)) {
                        break;
                    }
                }
                return name + num;
            }
        }
        return name;
    }




}
