package jDogs.serverclient.serverside;

import java.util.ArrayList;

public class ServerParser {

    private Server server;
    private ServerConnection serverConnection;

    ServerParser(Server server, ServerConnection serverConnection) {
        this.server = server;
        this.serverConnection = serverConnection;
    }

    public static void joinGame(String substring) {
        for (int i = 0; i < substring.length(); i++) {
            if (substring.charAt(i) == ';') {
                // TODO format: gameName only gamename distroy this after
            }
        }
    }


    public GameFile setUpGame(String gameSetup) {

        int nameSeparator = 0;

        for (int i = 0; i < gameSetup.length(); i++) {
            if (gameSetup.charAt(i) == ';') {
                nameSeparator = i;
                break;
            }
        }



        String name = gameSetup.substring(0,nameSeparator);
        String total = gameSetup.substring(nameSeparator + 1);


        System.out.println(name);
        System.out.println(total);

        String host = serverConnection.getNickname();
        name = checkName(name);

        if (checkHost(host)) {return new GameFile(name, host, total);}

        System.err.println("Gamehost " + host + " already exists in server.allGames!");
        return null;
    }

    private boolean checkHost(String host) {
        for (int i = 0; i < server.allGames.size(); i++) {
            if (server.allGames.get(i).getHost().equals(host)) {
                return false;
            }
        }
        return false;
    }


    private String checkName(String name) {
        int problem = -1;
        for (int i = 0; i < server.allGames.size(); i++) {
            if (server.allGames.get(i).getNameId().equals(name)) {
                int num = 1;
                while (true) {
                    num++;
                    if (!server.allGames.get(i).getNameId().equals(name + num)) {
                        break;
                    }
                }
                return name + num;
            }
        }
        return name;
    }


}
