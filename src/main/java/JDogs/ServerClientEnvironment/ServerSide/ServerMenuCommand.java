package JDogs.ServerClientEnvironment.ServerSide;

import JDogs.ServerClientEnvironment.QueueJD;

public class ServerMenuCommand {
    private Server server;
    private ServerConnection serverConnection;
    private MessageHandlerServer messageHandlerServer;
    private QueueJD sendToThisClient;
    private boolean loggedIn;
    private String nickName;
    public ServerMenuCommand(Server server, ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer, QueueJD sendToThisClient) {
        this.server = server;
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.sendToThisClient = sendToThisClient;
        this.loggedIn = false;
        this.nickName = null;
    }

    public void execute (String text) {
        //execute commands
        String command = text.substring(0,4);
        switch (command) {
            case "USER":
                if (text.length() < 6) {
                    sendToThisClient.enqueue("No username entered");
                } else {
                    String oldNick = nickName;
                    nickName = text.substring(5);

                    if (server.isValidNickName(nickName)) {
                        server.allNickNames.add(nickName);
                        //oldNick is null, if client just logged in to the server
                        if (oldNick != null) {
                            server.allNickNames.remove(oldNick);
                        }

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

            case "ACTI":
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

        }
    }
    /**
     * Returns the nickName of the user
     * @return nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     *
     * @return if user is logged in or not
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
