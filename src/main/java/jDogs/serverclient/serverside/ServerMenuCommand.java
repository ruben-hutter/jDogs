package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.Queuejd;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


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
    private static final Logger logger = LogManager.getLogger(ServerMenuCommand.class);

    public ServerMenuCommand(Server server, ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer, Queuejd sendToThisClient,
            Queuejd sendToAll) {
        this.server = server;
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.loggedIn = false;
        this.nickName = null;
        this.serverParser = new ServerParser(server, serverConnection);
    }

    public void execute(String text) {
        //execute commands
        logger.debug("Entered ServerMenuCommand with: " + text);
        String command = text.substring(0, 4);
        // do not receive any commands but USER before logged in
        if (!loggedIn && !command.equals("USER")) {
            sendToThisClient.enqueue("INFO please log in first");

        } else {
            switch (command) {
                case "USER":
                    if (text.length() < 6) {
                        sendToThisClient.enqueue("INFO No username entered");
                    } else {
                        String oldNick = serverConnection.getNickname();
                        nickName = text.substring(5);
                        logger.debug("oldNickname is: " + oldNick);

                        if (!validCharacters(nickName)) {
                           nickName = serverConnection.getDefaultName();
                        }

                        if (!server.isValidNickName(nickName)) {
                            logger.debug("Nickname " + nickName + " is already used.");
                            int number = 2;
                            while (true) {
                                if (server.isValidNickName(nickName + number)) {
                                    nickName = nickName + number;
                                    logger.debug("New nickname is " + nickName);
                                    break;
                                } else {
                                    number++;
                                }
                            }
                        }

                        if (oldNick != null) {
                            server.removeNickname(oldNick);
                            sendToAll.enqueue("DPER " + oldNick);
                        }
                        sendToThisClient.enqueue("USER "
                                + nickName);
                        sendToAll.enqueue("LPUB " + nickName);

                        // if you are not logged in you are not added to the serverConnections lists
                        if (!loggedIn) {
                            server.addToLobby(serverConnection);
                            loggedIn = true;
                        }
                        server.addNickname(nickName, serverConnection);
                        serverConnection.updateNickname(nickName);
                    }
                    break;

                case "ACTI":
                    String list = "INFO all active Players ";
                    for (int i = 0; i < server.allNickNames.size(); i++) {
                        list += "player # " + i + "\n";
                        list += server.allNickNames.get(i) + " ";
                        list += "\n";
                    }
                    sendToThisClient.enqueue(list);
                    break;

                case "EXIT":
                    sendToThisClient.enqueue("INFO logout now");
                    logger.debug(serverConnection.getNickname() + " logged out");
                    serverConnection.kill();
                    break;

                case "STAT":
                    String running = "";
                    for (MainGame mainGame : server.runningGames) {
                        running += mainGame.getGameId() + " ";
                    }
                    String finished = "";
                    for (OpenGameFile openGameFile1 : server.finishedGames) {
                        finished += openGameFile1.getNameId() + " ";
                    }
                    sendToThisClient
                            .enqueue("STAT " + "runningGames " + server.runningGames.size()
                                    + running
                                    + " finishedGames " + server.finishedGames.size()
                                    + finished);
                    logger.debug("runningGames " + server.runningGames.size()
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
                        logger.debug("adressor: " + adressor);
                        String message = text.substring(5 + separator);
                        logger.debug("message: " + message);
                        try {
                            server.getSender(adressor)
                                    .sendStringToClient(
                                            "WCHT " + "@" + nickName + ": " + message);
                        } catch (Exception e) {
                            //prevent shutdown if nickname doesn`t exist in hashmap
                            sendToThisClient.enqueue("INFO nickname unknown");
                        }
                    }
                    break;

                case "PCHT":
                    // send to all in public lobby
                    sendToAll.enqueue("PCHT " + "<" + nickName + ">" + text.substring(4));
                    break;

                case "OGAM":
                    //set new game up with this command
                    try {
                        setUpGame(text.substring(5));
                    } catch (Exception e) {
                        logger.error("Error while building up new game file.");
                        e.printStackTrace();
                        sendToThisClient.enqueue("INFO error while building up new game file");

                    }
                    break;

                case "SESS":
                    for (OpenGameFile openGameFile : Server.getInstance().getOpenGameList()) {
                            sendToThisClient.enqueue(
                                    "OGAM " + openGameFile.getSendReady());
                    }
                    break;

                case "LPUB":
                    sendAllPublicGuests();
                    break;

                case "JOIN":
                    //join a game with this command
                    try {
                        String openGameId = text.substring(5);
                        Server.getInstance().getOpenGameFile(openGameId)
                                .addParticipant(serverConnection);
                        messageHandlerServer.setJoinedOpenGame(openGameId);
                        logger.debug("User " + nickName + " has joined game " + openGameId);
                        sendToThisClient.enqueue("JOIN " + openGameId);
                        sendToAll.enqueue("OGAM " + Server.getInstance().getOpenGameFile(openGameId)
                                .getSendReady());
                        // all required players are set, then send start request to host
                        if (Server.getInstance().getOpenGameFile(openGameId).readyToStart()) {
                            Server.getInstance().getOpenGameFile(openGameId)
                                    .sendConfirmationMessage();
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
     *sets up a game if someone sends the command "OGAM" with the fitting parameters
     * @param game
     */
    private void setUpGame (String game) {
        OpenGameFile openGameFile = serverParser.setUpGame(game);
        if (openGameFile == null) {
            System.err.println("ERROR setUpGame");
            sendToThisClient.enqueue("INFO wrong game file format");
        } else {
            server.addOpenGame(openGameFile);
            messageHandlerServer.setJoinedOpenGame(openGameFile.getNameId());
        }
    }

    /**
     *
     * @param name nickname to check
     * @return false, if name contains whitespace
     */
    private boolean validCharacters (String name){
        for (int i = 0; i < name.length(); i++) {
            if (Character.isWhitespace(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the nickName of the user
     * @return nickName
     */
    public String getNickName () {
        return nickName;
    }

    /**
     * sends all public lobby guests to this client
     */
    public void sendAllPublicGuests () {
        for (int i = 0; i < server.publicLobbyGuests.size(); i++) {
            sendToThisClient.enqueue("LPUB " + server.publicLobbyGuests.get(i));
        }
    }
}