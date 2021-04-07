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
    private ServerParser serverParser;

    public ServerMenuCommand(Server server, ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer, Queuejd sendToThisClient, Queuejd sendToAll) {
        this.server = server;
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.loggedIn = false;
        this.nickName = null;
        this.serverParser = new ServerParser(server,serverConnection);
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

                    if (!validCharacters(nickName)) {
                        serverConnection.getDefaultName();
                    }

                    if (!server.isValidNickName(nickName)) {
                        int number = 2;
                        while (true) {
                            if (server.isValidNickName(nickName + " " + number)) {
                                nickName = nickName + " " + number;
                                break;
                            } else {
                                number++;
                            }
                        }
                    }


                    if (oldNick != null) {
                        server.removeNickname(oldNick);
                    }
                    sendToThisClient.enqueue("USER "
                            + nickName);

                    System.out.println("login worked " + "USER " + nickName);
                    if (loggedIn) {
                        server.removeNickname(nickName);
                    } else {
                        server.addSender(serverConnection.getSender());
                    }
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
                //send private message
                int separator = -1;
                for (int i = 0; i < text.substring(5).length(); i++) {
                   if (text.substring(4).toCharArray()[i] == ';') {
                       separator = i;
                       break;
                   }
                }

                if (separator == -1) {
                    sendToThisClient.enqueue("INFO " + "wrong WCHT format");
                } else {
                    String adressor = text.substring(5,4 + separator);
                    String message = text.substring(5 + separator);
                    try {
                        server.getSenderForWhisper(adressor).sendStringToClient("WCHT " + "@" +nickName + ": " + message);
                    } catch (Exception e) {
                        //prevent shutdown if nickname doesn`t exist in hashmap
                        sendToThisClient.enqueue("INFO nickname unknown");
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

            case "SETG":
                //set game up with this command
                try {
                    setUpGame(text.substring(5));
                } catch (Exception e) {
                    sendToThisClient.enqueue("INFO wrong gameFile format");

                }




            case "JOIN":
                //join a game with this command
                try {
                    joinGame(text.substring(5));
                } catch (Exception e) {
                    sendToThisClient.enqueue("INFO wrong format, can`t join");
                }
                break;

            case "STAR":
                // TODO confirm you wanna start the game
                break;

        }
    }

    private void joinGame(String substring) {
        ServerParser.joinGame(substring);
    }

    private void setUpGame(String game) {
       GameFile gameFile = serverParser.setUpGame(game);
       if (gameFile == null) {
           sendToThisClient.enqueue("INFO wrong game file format");
       } else {
          server.allGames.add(gameFile);
          sendToAll.enqueue(gameFile.getSendReady());
       }
    }

    /**
     *
     * @param name nickname to check
     * @return false, if name contains ';'
     */
    private boolean validCharacters(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ';') {
                return false;
            }
        }
        return true;
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
