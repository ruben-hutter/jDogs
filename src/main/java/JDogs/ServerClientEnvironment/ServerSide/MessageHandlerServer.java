package JDogs.ServerClientEnvironment.ServerSide;

import JDogs.ServerClientEnvironment.QueueJD;

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
    private ServerGameCommand gameCommand;
    private ServerMenuCommand menuCommand;
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
        this.menuCommand = new ServerMenuCommand(server, serverConnection,this,sendToThisClient);
        this.gameCommand = new ServerGameCommand();
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
                    gameCommand.execute(text);
                }
                // check if text is a MenuCommand
                if (text.length() >= 4 && ServerMenuProtocol.isACommand(text)) {
                    menuCommand.execute(text);
                }

                else {
                    // before sending messages to others: complete login!
                    if (loggedIn) {
                        sendToAll.enqueue(nickName + " : " + text);
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

    public void manageCommand(String text) {
        /*String command = text.substring(0,4);
        switch (command) {
            case "USER":
                if (text.length() < 6) {
                    sendToThisClient.enqueue("No username entered");
                } else {
                    String oldNick = nickName;
                    nickName = text.substring(5);
                    if (server.isValidNickName(nickName)) {
                        server.allNickNames.add(nickName);
                        server.allNickNames.remove(oldNick);
                        sendToThisClient.enqueue("hi, user! your new nickname is: " + nickName);
                    } else {
                        int number = 2;
                        while (true) {
                            if(server.isValidNickName(nickName + " " + number)) {
                                nickName = nickName + " " + number;
                                server.allNickNames.add(nickName);
                                server.allNickNames.remove(oldNick);
                                sendToThisClient.enqueue("hi, user! your new name is: "
                                        + nickName);
                                break;
                            } else {
                                number++;
                            }
                        }
                    }
                    System.out.println("login worked");
                    if(!loggedIn) {serverConnection.loggedIn();}
                    loggedIn = true;
                }
                break;
            case "PASS":
                // TODO give and change password Gregor: is pw necessary?
                break;
            case "ACTI":
                // TODO return a list of online usernames
                String list = "";
                for (int i = 0; i < server.allNickNames.size(); i++) {
                    list += "player # " + i;
                    list += server.allNickNames.get(i);
                    list += "";
                }
                sendToThisClient.enqueue(list);
                break;
            case "QUIT":
                sendToThisClient.enqueue("logout now");
                serverConnection.kill();
                break;
            case "EXIT":
                // TODO leave game session
                break;
            case "MOVE":
                // TODO move marble in game
                break;
            case "STAT":
                // TODO sync game stats
                break;
            case "MODE":
                // TODO chose a game mode
                break;
            case "WCHT":
                // TODO chose a partner whom to send the message
                sendToThisClient.enqueue("whisperChat is not implemented");
                break;
            case "PCHT":
                // TODO send message to all active clients
                break;
            case "STAR":
                // TODO confirm you wanna start the game
                break;
            case "CTTP":
                // TODO switch selected card with partner
                break;
            case "HELP":
                // TODO shows the user guide.
                break;
        }

         */
    }

    /**
     * Returns the nickName of the user
     * @return nickName
     */
    public String getNickName() {
        return menuCommand.getNickName();
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }
}
