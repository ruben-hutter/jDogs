package jDogs;

import jDogs.gui.GUILauncher;
import jDogs.serverclient.clientside.Client;
import jDogs.serverclient.serverside.Server;

public class Main {
    private static Main instance;
    private int port;
    private String username;
    private String hostAddress;

    Main () {
        instance = this;
        this.username = null;
    }

    public static void main(String[] args) {
        //client <hostadress>:<port> [<username>] | server <port>
        System.out.println(args[0]);
        System.out.println(args[1]);

        try {
            Main mainInstance = new Main();

            if (args[0].equals("client")) {
                mainInstance.parseHostaddressAndPort(args[1]);

                if (args.length == 3 && args[2] != null) {
                    mainInstance.setUsername(args[2]);
                }
                //GUILauncher.main(args);
                Client.main(args);

            } else if (args[0].equals("server")) {
                //mainInstance.parseServerPort(args[1]);
                Server.main(args);

            } else {
                System.err.println("server or client in string is missing");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("error parsing argument");
        }

    }

    private void setUsername(String arg) {
        this.username = arg;
    }

    private void parseServerPort(String arg) {
        setPort(Integer.parseInt(arg));
    }

    private void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private void parseHostaddressAndPort(String arg) {
        int separator = -1;
        for (int i = 0; i < arg.length(); i++) {
            if (arg.charAt(i) == ':') {
                separator = i;
                break;
            }
        }

        hostAddress = arg.substring(0, separator);
        parseServerPort(arg.substring(separator + 1));
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public static Main getInstance() {
        return instance;
    }


    public String getUsername() {
        return username;
    }
}
