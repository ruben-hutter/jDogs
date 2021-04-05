package jDogs.serverClientStuff.serverSide;

import jDogs.serverClientStuff.helpers.QueueJD;

/**
 * ServerMenuCommand contains the menu/lobby
 * commands which are sent from the clients to
 * communicate with the server.
 *
 */

public class ServerMenuCommand {
    private Server server;
    private ServerConnection serverConnection;
    private MessageHandlerServer messageHandlerServer;
    private QueueJD sendToThisClient;
    private QueueJD sendToAll;
    private boolean loggedIn;
    private String nickName;

    public ServerMenuCommand(Server server, ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer, QueueJD sendToThisClient, QueueJD sendToAll) {
        this.server = server;
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
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
                        sendToThisClient.enqueue("hi, user! your new nickname is: " + nickName);
                    } else {
                        int number = 2;
                        while (true) {
                            if(server.isValidNickName(nickName + " " + number)) {
                                nickName = nickName + " " + number;
                                sendToThisClient.enqueue("hi, user! your new name is: "
                                        + nickName);
                                break;
                            } else {
                                number++;
                            }
                        }
                    }

                    if (oldNick != null) {
                        server.removeNickname(oldNick);
                    }
                    System.out.println("login worked");
                    if (loggedIn) {

                        server.removeNickname(nickName);

                        /*
                        is needed to import the SendFromServer to the list
                        which is to send public chat messages to all
                         */
                    }
                    //nickName = "< " + nickName + " > ";
                    serverConnection.getLoggedIn(nickName);


                    loggedIn = true;
                }
                break;

            case "ACTI":
                String list = "";
                for (int i = 0; i < server.allNickNames.size(); i++) {
                    list += "player # " + i + "\n";
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
                //PCHT is now necessary for MessageHandlerClients
                if(loggedIn) {
                    sendToAll.enqueue("PCHT " + nickName + text.substring(4));
                }
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
