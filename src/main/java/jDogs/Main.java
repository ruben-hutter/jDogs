package jDogs;

import jDogs.serverclient.serverside.Server;

public class Main {

    public static void main(String[] args) {
        // client <hostadress>:<port> [<username>] | server <port>
        if (args[0].equals("client")) {
            System.out.println("Coming soon");
        } else if (args[0].equals("server")) {
            Server.main(args);
        } else {
            System.err.println("Not a valid command!");
        }
    }
}
