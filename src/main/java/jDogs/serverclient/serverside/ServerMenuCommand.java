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
    private  boolean isPlaying;
    private boolean joinedGame;
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
        this.isPlaying = false;
        this.joinedGame = false;
        this.actualGame = null;
    }

    public void execute (String text) {
        //execute commands
        String command = text.substring(0,4);
        switch (command) {
            case "USER":
                if (isPlaying() || joinedGame) {
                    sendToThisClient.enqueue("INFO not allowed changing name after joining or playing game");
                    break;
                }

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
                    list += server.allNickNames.get(i) + " ";
                    list += "\n";
                }
                sendToThisClient.enqueue(list);
                break;

            case "QUIT":
                if (isPlaying) {
                    //stop gaming for all
                    MainGame mainGame = getRunningGame(actualGame);
                    sendMessageToThisGroup(mainGame.getGameFile().getParticipantsArray(), "QUIT");
                    sendMessageToThisGroup(mainGame.getGameFile().getParticipantsArray(), "INFO user " + nickName + " left session" );
                    isPlaying = false;
                    endGameforAll(mainGame);
                    break;
                }

                if (joinedGame) {
                    System.out.println(actualGame);
                    GameFile gameFile = getGame(actualGame);
                    if (gameFile.getHost() == nickName) {
                        server.allGamesNotFinished.remove(gameFile);
                        sendToAll.enqueue("DOGA " + gameFile.getNameId());
                    } else {
                        gameFile.removeParticipant(nickName);
                        System.out.println(gameFile.getParticipants());
                        sendToAll.enqueue("DPER " + gameFile.getNameId() + ";" + getNickName());
                    }
                    joinedGame = false;
                    break;
                }

                sendToThisClient.enqueue("INFO logout now");
                serverConnection.kill();
                break;

            case "STAT":
                // TODO sync game stats
                break;

            case "WCHT":
                //send private message
                if (isPlaying) {
                    sendToThisClient.enqueue("INFO whisper not allowed while playing");
                    break;
                }
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
                        server.getSender(adressor).sendStringToClient("WCHT " + "@" +nickName + ": " + message);
                    } catch (Exception e) {
                        //prevent shutdown if nickname doesn`t exist in hashmap
                        sendToThisClient.enqueue("INFO nickname unknown");
                    }
                }

                break;

            case "PCHT":
                // TODO send message to all active clients
                //PCHT is now necessary for MessageHandlerClients
                if (isPlaying)
                if(loggedIn) {
                    sendToAll.enqueue("PCHT " + "<" + nickName + ">" + text.substring(4));
                }
                break;

            case "OGAM":
                //set new game up with this command
                try {
                    if (isPlaying || joinedGame) {
                        sendToThisClient.enqueue("INFO already joined game or playing");
                        break;
                    }
                    setUpGame(text.substring(5));
                    joinedGame = true;
                } catch (Exception e) {
                    sendToThisClient.enqueue("INFO wrong gameFile format");

                }
                break;

            case "JOIN":
                //join a game with this command
                System.out.println("JOIN from " +nickName + " : " + text );
                try {
                    if (isPlaying || joinedGame) {
                        sendToThisClient.enqueue("INFO already joined a game or playing");
                        break;
                    }

                    GameFile game = getGame(text.substring(5));
                    if (game == null) {
                        sendToThisClient.enqueue("INFO join not possible,game name does not exist");
                    } else {
                        game.addParticipants(serverConnection.getNickname());
                        sendToAll.enqueue("JOIN " + game.getNameId() + ";" + nickName);
                        actualGame = game.getNameId();
                        joinedGame = true;

                        // all required players are set, then send start request to client
                        if (game.readyToStart()) {
                            String[] array = game.getParticipantsArray();
                            for (int i = 0; i < game.getNumberOfParticipants(); i++) {
                                server.getSender(array[i]).sendStringToClient("STAR " + game.getNameId());
                            }
                        }
                    }
                } catch (Exception e) {
                    sendToThisClient.enqueue("INFO wrong format,you cannot join");
                }
                break;

            case "STAR":
                // TODO confirm you wanna start the game
                if (isPlaying) {
                    sendToThisClient.enqueue("INFO already joined a game");
                    break;
                }
                GameFile gameFile = getGame(text.substring(5));
                gameFile.confirmStart(nickName);
                isPlaying = true;
                //set all to game mode
                actualGame = gameFile.getNameId();

                if (gameFile.startGame()) {
                    sendToAll.enqueue("GAME " + gameFile.getNameId());
                    server.startGame(new MainGame(gameFile));
                }
                break;
        }
    }

    private void endGameforAll(MainGame mainGame) {
        server.runningGames.remove(mainGame);
        server.allGamesNotFinished.remove(getGame(actualGame));
        String[] array = mainGame.getGameFile().getParticipantsArray();
        for (int i = 0; i < array.length; i++) {
            server.getServerConnections(array[i]).getServerMenuCommand().setIsPlaying(false);
        }
    }

    private void setIsPlaying(boolean value) {
        isPlaying = value;
    }

    private void sendMessageToThisGroup(String[] participantsArray, String message) {
        for (int i = 0; i < participantsArray.length; i++) {
            server.getSender(participantsArray[i]).sendStringToClient(message);
        }
    }

    /**
     *
     * @param actualGame is the ongoing game which should be found
     * @return the game or null
     */

    private MainGame getRunningGame(String actualGame) {
        for (int i = 0; i < server.runningGames.size(); i++) {
           if (server.runningGames.get(i).getGameId() == actualGame) {
              return server.runningGames.get(i);
           }
        }
        return null;
    }

    /**
     *
     * @return if player joined a game in lobby or is playing
     */
    private boolean isPlaying() {
        return isPlaying;
    }

    private GameFile getGame(String gameName) {
        for (int i = 0; i < server.allGamesNotFinished.size(); i++) {
            if (server.allGamesNotFinished.get(i).getNameId().equals(gameName)) {
               return server.allGamesNotFinished.get(i);
            }
        }
        return null;
    }


    private void setUpGame(String game) {
       GameFile gameFile = serverParser.setUpGame(game);
       if (gameFile == null) {
           System.err.println("ERROR setUpGame");
           sendToThisClient.enqueue("INFO wrong game file format");
       } else {
          server.allGamesNotFinished.add(gameFile);
          actualGame = gameFile.getNameId();
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

    /**
     *
     * @return if user is logged in or not
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
