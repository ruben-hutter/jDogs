package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.Queuejd;

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
    private Queuejd sendToThisClient;
    private Queuejd sendToAll;
    private boolean loggedIn;
    private String nickName;

    public ServerMenuCommand(Server server, ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer, Queuejd sendToThisClient, Queuejd sendToAll) {
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
                            if (server.isValidNickName(nickName + " " + number)) {
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
                    } else {
                        server.addSender(serverConnection.getSender());
                    }
                    //nickName = "< " + nickName + " > ";
                    server.addNickname(nickName, serverConnection.getSender());
                    serverConnection.updateNickname(nickName);

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
                // chose a partner whom to send the message
                //get separator sign
                int separator = -1;
                for (int i = 0; i < text.substring(4).length(); i++) {
                   if (text.substring(4).toCharArray()[i] == ';') {
                       separator = i;
                       break;
                   }
                }

                if (separator == -1) {
                    sendToThisClient.enqueue("wrong WCHT format");
                } else {
                    String adressor = text.substring(5,4 + separator);
                    String message = text.substring(4 + separator + 1);
                    try {
                        server.getSenderForWhisper(adressor).sendStringToClient("WCHT " + nickName + ";" + message);
                    } catch (Exception e) {
                        //prevent shutdown if nickname doesn`t exist in hashmap
                        sendToThisClient.enqueue("nickname unknown");
                    }
                }

                break;

            case "PCHT":
                // TODO send message to all active clients
                //PCHT is now necessary for MessageHandlerClients
                if(loggedIn) {
                    sendToAll.enqueue("PCHT " + "<" + nickName + ">" + text.substring(4));
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
