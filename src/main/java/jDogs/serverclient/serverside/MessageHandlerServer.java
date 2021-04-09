package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.Queuejd;

/**
 * This thread processes messages received meaningfully.
 * <li>it distinguishes between messages for server and all clients</li>
 * <li>it handles the login of the client</li>
 */

public class MessageHandlerServer implements Runnable {

    private final Queuejd sendToAll;
    private final Queuejd sendToThisClient;
    private final Queuejd receivedFromClient;
    private boolean running;
    private boolean loggedIn;
    private final Server server;
    private final ServerConnection serverConnection;
    private ServerGameCommand serverGameCommand;
    private ServerMenuCommand serverMenuCommand;
    private SeparateLobbyCommand separateLobbyCommand;
    private String state;

    public MessageHandlerServer(Server server,ServerConnection serverConnection,
            Queuejd sendToThisClient, Queuejd sendToAll, Queuejd receivedFromClient) {
        this.sendToAll = sendToAll;
        this.sendToThisClient = sendToThisClient;
        this.receivedFromClient = receivedFromClient;
        this.server = server;
        this.serverConnection = serverConnection;
        this.running = true;
        this.loggedIn = false;
        this.serverMenuCommand = new ServerMenuCommand(server, serverConnection,this,sendToThisClient, sendToAll);
        this.serverGameCommand = new ServerGameCommand();
        this.separateLobbyCommand = new SeparateLobbyCommand(sendToThisClient, sendToAll, serverConnection);
        this.state = "publicLobby";
    }

    @Override
    public void run() {
        String text;

        // get loggedIn
        sendToThisClient.enqueue("USER");

        //while()-loop always running
        while (running) {
            if (!receivedFromClient.isEmpty()) {
                text = receivedFromClient.dequeue();
                if (text.length() >= 4) {

                switch(state) {

                    case "playing":
                        serverGameCommand.execute(text);
                        break;

                    case "openGame":

                        break;


                    case "publicLobby":
                        serverMenuCommand.execute(text);
                        break;

                    }

                } else {

                    System.err.println("message did not match menu or game protocol:  " + text);
                }
            } else {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(this.toString() + "  stops now");
    }


    /**
     * Returns the nickName of the user
     * @return nickName
     */
    public String getNickName() {
        return serverMenuCommand.getNickName();
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }

    /**
     *
     * @param playing true: send messages to GameCommand (a game started)
     *                false: send messages to public MenuCommand (a game ended)
     */

    public void setPlaying(boolean playing) {
        if (playing) {
            state = "playing";
        } else {
            state = "publicLobby";
        }
    }

    /**
     *
     * @param joined send messages to separateLobbyCommand
     */

    public void setJoinedOpenGame(boolean joined) {
        if (joined) {
            state = "openGame";
        } else {
            state = "publicLobby";
        }
    }

    public ServerMenuCommand getServerMenuCommand() {
        return serverMenuCommand;
    }

    public SeparateLobbyCommand getSeparateLobbyCommand() {
        return separateLobbyCommand;
    }

    public ServerGameCommand getServerGameCommand() {
        return serverGameCommand;
    }

}
