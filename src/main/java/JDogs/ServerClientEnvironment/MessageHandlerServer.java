package JDogs.ServerClientEnvironment;

/**
 * this thread processes messages received meaningfully.
 * - it distinguishes between messages for server and all clients
 * - it handles the login of the client
 *
 */
public class MessageHandlerServer implements Runnable {

    private final Queue sendToAll;
    private final Queue sendToThisClient;
    private final Queue receivedFromClient;
    private boolean running;
    private boolean loggedIn;
    private final Server server;
    private final ServerConnection serverConnection;
    private String nickName;

    public MessageHandlerServer(Server server,ServerConnection serverConnection,
            Queue sendToThisClient, Queue sendToAll, Queue receivedFromClient) {
        this.sendToAll = sendToAll;
        this.sendToThisClient = sendToThisClient;
        this.receivedFromClient = receivedFromClient;
        this.server = server;
        this.serverConnection = serverConnection;

        this.running = true;
        this.loggedIn = false;
    }

    @Override
    public void run() {
        String text;
        nickName = null;
        //get loggedIn
        sendToThisClient.enqueue("USER");

        while (running) {
            if (!receivedFromClient.isEmpty()) {
                text = receivedFromClient.dequeue();

                // check if text is a command
                if (text.length() >= 4 && Protocol.isACommand(text)) {
                    manageCommand(text);
                } else {
                    //before sending messages to others: complete login!
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
        String command = text.substring(0,4);
        switch (command) {
            case "USER":
                if (text.length() < 6) {
                    sendToThisClient.enqueue("No username entered");
                } else {
                    nickName = text.substring(5);
                    if (server.isValidNickName(nickName)) {
                        server.allNickNames.add(nickName);
                        sendToThisClient.enqueue("hi, user! your new nickname is: " + nickName);
                    } else {
                        int number = 2;
                        while (true) {
                            if(server.isValidNickName(nickName + " " + number)) {
                                nickName = nickName + " " + number;
                                server.allNickNames.add(nickName);
                                sendToThisClient.enqueue("hi, user! your new name is: " + nickName);
                                break;
                            } else {
                                number++;
                            }
                        }
                    }
                    System.out.println("login worked");
                    serverConnection.loggedIn();
                    loggedIn = true;
                }
                break;
            /*case "PASS":
                // TODO give and change password Gregor: is pw necessary?
                break;

             */
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
            /*case HELP:
                // TODO shows the user guide. Gregor: or save manual on client side?
                break;

             */
        }
    }


    public void kill() {
        running = false;
    }
}
