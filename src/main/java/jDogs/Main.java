package jDogs;

import jDogs.gui.GUILauncher;
import jDogs.serverclient.clientside.Client;
import jDogs.serverclient.serverside.Server;

/**
 * The core of the game ;)
 */
public class Main {
    private static Main instance;
    private int port;
    private String username;
    private String hostAddress;

    Main () {
        instance = this;
        this.username = null;
    }

    /**
     * Starts the server or the client, given the right arguments
     * @param args client hostadress:port username or server port
     */
    public static void main(String[] args) {
        System.out.println(args[0]);
        System.out.println(args[1]);

        try {
            Main mainInstance = new Main();

            if (args[0].equals("client")) {
                mainInstance.parseHostAddressAndPort(args[1]);

                if (args.length == 3 && args[2] != null) {
                    mainInstance.setUsername(args[2]);
                }
                GUILauncher.main(args);
                //Client.main(args);

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

    /**
     * Sets the username
     * @param arg given name
     */
    private void setUsername(String arg) {
        this.username = arg;
    }

    /**
     * Parses the server port from String to an int
     * @param arg the port as String
     */
    private void parseServerPort(String arg) {
        setPort(Integer.parseInt(arg));
    }

    /**
     * Sets the port to the given value
     * @param port port value as int
     */
    private void setPort(int port) {
        this.port = port;
    }

    /**
     * Returns the port value
     * @return port as int
     */
    public int getPort() {
        return port;
    }

    /**
     * Parses the address and the port for a client
     * @param arg address and port, divided by :
     */
    private void parseHostAddressAndPort(String arg) {
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

    /**
     * Returns the host address
     * @return address as String
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * Makes an instance of the main
     * @return instance of main
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Returns the username
     * @return username as String
     */
    public String getUsername() {
        return username;
    }
}
