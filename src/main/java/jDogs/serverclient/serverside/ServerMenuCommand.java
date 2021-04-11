package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.Queuejd;


/**
 * ServerMenuCommand contains the menu/lobby
 * commands which are sent from the clients to
 * communicate with the server.
 *
 */

public class ServerMenuCommand {
    private final Server server;
    private final ServerConnection serverConnection;
    private final MessageHandlerServer messageHandlerServer;
    private final Queuejd sendToThisClient;
    private final Queuejd sendToAll;
    private boolean loggedIn;
    private String nickName;
    private final ServerParser serverParser;
    private String actualGame;

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
        // do not receive any commands but USER before logged in
        if (!loggedIn && !command.equals("USER")) {
            sendToThisClient.enqueue("INFO please log in first");

        } else {

            switch (command) {
                case "USER":
                    if (text.length() < 6) {
                        sendToThisClient.enqueue("INFO No username entered");
                    } else {
                        String oldNick = nickName;
                        nickName = text.substring(5);

                        if (!validCharacters(nickName)) {
                            serverConnection.getDefaultName();
                        }

                        if (!server.isValidNickName(nickName)) {
                            int number = 2;
                            while (true) {
                                if (server.isValidNickName(nickName + number)) {
                                    nickName = nickName + number;
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

                        if (!loggedIn) {
                            server.addSender(serverConnection.getSender());
                        }
                        server.addNickname(nickName, serverConnection);
                        serverConnection.updateNickname(nickName);

                        loggedIn = true;
                    }
                    break;

                case "ACTI":
                    String list = "";
                    for (int i = 0; i < server.allNickNames.size(); i++) {
                        list += "player # " + i + "\n";
                        list += server.allNickNames.get(i) + " ";
                        list += "\n";
                    }
                    sendToThisClient.enqueue(list);
                    break;

                case "QUIT":

                    sendToThisClient.enqueue("INFO logout now");
                    serverConnection.kill();
                    break;

                case "STAT":
                    sendToThisClient.enqueue("STAT " + "runningGames " + server.runningGames.size()
                            + " finishedGames " + server.finishedGames.size());
                    break;

                case "WCHT":
                    //send private message

                    int separator = -1;
                    for (int i = 0; i < text.substring(5).length(); i++) {
                        if (Character.isWhitespace(text.substring(5).charAt(i))) {
                            separator = i;
                            break;
                        }
                    }

                    if (separator == -1) {
                        sendToThisClient.enqueue("INFO " + "wrong WCHT format");
                    } else {
                        String adressor = text.substring(5, 5 + separator);
                        System.out.println("adressor " + adressor);
                        String message = text.substring(5 + separator);
                        System.out.println("mess " + message);
                        try {
                            server.getSender(adressor)
                                    .sendStringToClient("WCHT " + "@" + nickName + ": " + message);
                        } catch (Exception e) {
                            //prevent shutdown if nickname doesn`t exist in hashmap
                            sendToThisClient.enqueue("INFO nickname unknown");
                        }
                    }

                    break;

                case "PCHT":
                    // TODO send message to all active clients
                    //PCHT is now necessary for MessageHandlerClients
                    if (loggedIn) {
                        sendToAll.enqueue("PCHT " + "<" + nickName + ">" + text.substring(4));
                    }
                    break;

                case "OGAM":
                    //set new game up with this command
                    try {
                        setUpGame(text.substring(5));
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendToThisClient.enqueue("INFO error while building up new game file");

                    }
                    break;

                case "JOIN":
                    //join a game with this command
                    System.out.println("JOIN from " + nickName + " : " + text);
                    try {
                        GameFile game = getGame(text.substring(5));
                        if (game == null) {
                            sendToThisClient
                                    .enqueue("INFO join not possible,game name does not exist");
                        } else {
                            game.addParticipants(serverConnection);
                            sendToAll.enqueue("JOIN " + game.getNameId() + " " + nickName);
                            actualGame = game.getNameId();
                            messageHandlerServer.setJoinedOpenGame(game, nickName);

                            // all required players are set, then send start request to client
                            if (game.readyToStart()) {
                                game.start();

                                String[] array = game.getParticipantsArray();
                                for (int i = 0; i < game.getNumberOfParticipants(); i++) {
                                    server.getSender(array[i])
                                            .sendStringToClient("STAR " + game.getNameId());
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendToThisClient.enqueue("INFO wrong format,you cannot join");
                    }
                    break;
            }
        }
    }
    /**
     *
      * @param gameName is the name of the game which was sent to the server
     * @return returns a game if it exists in the server ArrayList of unfinished games
     */
    private GameFile getGame(String gameName) {
        for (int i = 0; i < server.allGamesNotFinished.size(); i++) {
            if (server.allGamesNotFinished.get(i).getNameId().equals(gameName)) {
               return server.allGamesNotFinished.get(i);
            }
        }
        return null;
    }

    /**
     *
     * @param game sets up a game if someone sends the command "OGAM" with the fitting parameters
     */

    private void setUpGame(String game) {
       GameFile gameFile = serverParser.setUpGame(game);
       if (gameFile == null) {
           System.err.println("ERROR setUpGame");
           sendToThisClient.enqueue("INFO wrong game file format");
       } else {
           System.out.println("set up game <" + gameFile.getNameId() + "> worked");
           server.allGamesNotFinished.add(gameFile);
           messageHandlerServer.setJoinedOpenGame(gameFile, nickName);
           sendToAll.enqueue("OGAM " + gameFile.getSendReady());
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

}
