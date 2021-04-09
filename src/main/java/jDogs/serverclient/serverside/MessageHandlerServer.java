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
    private String nickName;
    private boolean isPlaying;

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
        this.isPlaying = false;
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
                // check if text is a GameCommand
                if (isPlaying) {
                    serverGameCommand.execute(text);
                } else {
                    // check if text is a MenuCommand

                    if (text.length() >= 4 && ServerMenuProtocol.isACommand(text)) {
                        serverMenuCommand.execute(text);
                        System.out.println("menu command: " + text);
                    } else {
                        System.err.println("message did not match menu or game protocol:  " + text);
                    }
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

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
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

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public ServerMenuCommand getServerMenuCommand() {
        return serverMenuCommand;
    }
}
