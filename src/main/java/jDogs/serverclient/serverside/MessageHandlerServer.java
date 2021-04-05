package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.QueueJD;

/**
 * This thread processes messages received meaningfully.
 * <li>it distinguishes between messages for server and all clients</li>
 * <li>it handles the login of the client</li>
 */

public class MessageHandlerServer implements Runnable {

    private final QueueJD sendToAll;
    private final QueueJD sendToThisClient;
    private final QueueJD receivedFromClient;
    private boolean running;
    private boolean loggedIn;
    private final Server server;
    private final ServerConnection serverConnection;
    private ServerGameCommand serverGameCommand;
    private ServerMenuCommand serverMenuCommand;
    private String nickName;

    public MessageHandlerServer(Server server,ServerConnection serverConnection,
            QueueJD sendToThisClient, QueueJD sendToAll, QueueJD receivedFromClient) {
        this.sendToAll = sendToAll;
        this.sendToThisClient = sendToThisClient;
        this.receivedFromClient = receivedFromClient;
        this.server = server;
        this.serverConnection = serverConnection;
        this.running = true;
        this.loggedIn = false;
        this.serverMenuCommand = new ServerMenuCommand(server, serverConnection,this,sendToThisClient, sendToAll);
        this.serverGameCommand = new ServerGameCommand();
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
                if (text.length() >= 4 && ServerGameProtocol.isACommand(text.substring(0,4))) {
                    serverGameCommand.execute(text);
                } else {
                    // check if text is a MenuCommand

                    if (text.length() >= 4 && ServerMenuProtocol.isACommand(text)) {
                        serverMenuCommand.execute(text);
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
}
