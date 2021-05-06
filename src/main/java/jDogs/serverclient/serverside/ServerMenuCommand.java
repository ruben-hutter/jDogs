package jDogs.serverclient.serverside;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * ServerMenuCommand contains the menu/lobby
 * commands which are sent from the clients to
 * communicate with the server.
 *
 */

public class ServerMenuCommand {

    private final ServerConnection serverConnection;
    private final MessageHandlerServer messageHandlerServer;
    private boolean loggedIn;
    private String nickName;
    private final ServerParser serverParser;
    private static final Logger logger = LogManager.getLogger(ServerMenuCommand.class);

    public ServerMenuCommand(ServerConnection serverConnection,
            MessageHandlerServer messageHandlerServer) {
        this.serverConnection = serverConnection;
        this.messageHandlerServer = messageHandlerServer;
        this.loggedIn = false;
        this.nickName = null;
        this.serverParser = new ServerParser(serverConnection);
    }

    public void execute(String text) {
        //execute commands
        logger.debug("Entered ServerMenuCommand with: " + text);
        String command = text.substring(0, 4);
        // do not receive any commands but USER before logged in
        if (!loggedIn && !command.equals("USER")) {
            serverConnection.sendToClient("INFO please log in first");

        } else {
            switch (command) {
                case "USER":
                    if (text.length() < 6) {
                        serverConnection.sendToClient("INFO No username entered");
                    } else {
                        String oldNick = serverConnection.getNickname();
                        nickName = text.substring(5);
                        logger.debug("oldNickname is: " + oldNick);

                        if (!validCharacters(nickName)) {
                           nickName = serverConnection.getDefaultName();
                        }

                        if (!Server.getInstance().isValidNickName(nickName)) {
                            logger.debug("Nickname " + nickName + " is already used.");
                            int number = 2;
                            while (true) {
                                if (Server.getInstance().isValidNickName(nickName + number)) {
                                    nickName = nickName + number;
                                    logger.debug("New nickname is " + nickName);
                                    break;
                                } else {
                                    number++;
                                }
                            }
                        }

                        if (oldNick != null) {
                            Server.getInstance().removeNickname(oldNick);
                            serverConnection.sendToPub("DPER " + oldNick);
                        }
                       serverConnection.sendToClient("USER "
                                + nickName);
                        serverConnection.sendToPub("LPUB " + nickName);

                        // if you are not logged in you are not added to the serverConnections lists
                        if (!loggedIn) {
                            Server.getInstance().addToLobby(serverConnection);
                            loggedIn = true;
                        }
                        Server.getInstance().addNickname(nickName, serverConnection);
                        serverConnection.updateNickname(nickName);
                    }
                    break;

                case "ACTI":
                    String list = "INFO all active Players ";
                    for (int i = 0; i < Server.getInstance().allNickNames.size(); i++) {
                        list += "player # " + i + "\n";
                        list += Server.getInstance().allNickNames.get(i) + " ";
                        list += "\n";
                    }
                    serverConnection.sendToClient(list);
                    break;

                case "EXIT":
                    serverConnection.sendToClient("INFO logout now");
                    logger.debug(serverConnection.getNickname() + " logged out");
                    serverConnection.kill();
                    break;

                case "STAT":
                    String running = "";
                    for (MainGame mainGame : Server.getInstance().runningGames) {
                        running += mainGame.getGameId() + " ";
                    }
                    String finished = "";
                    for (OpenGameFile openGameFile1 : Server.getInstance().finishedGames) {
                        finished += openGameFile1.getNameId() + " ";
                    }
                    serverConnection.sendToClient
                            ("STAT " + "runningGames " + Server.getInstance().runningGames.size()
                                    + running
                                    + " finishedGames " + Server.getInstance().finishedGames.size()
                                    + finished);
                    logger.debug("runningGames " + Server.getInstance().runningGames.size()
                            + " finishedGames " + Server.getInstance().finishedGames.size());
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
                        serverConnection.sendToClient("INFO " + "wrong WCHT format");
                    } else {
                        String adressor = text.substring(5, 5 + separator);
                        logger.debug("adressor: " + adressor);
                        String message = text.substring(5 + separator);
                        logger.debug("message: " + message);
                        try {
                            Server.getInstance().getServerConnection(adressor)
                                    .sendToClient(
                                            "WCHT " + "@" + nickName + ": " + message);
                        } catch (Exception e) {
                            //prevent shutdown if nickname doesn`t exist in hashmap
                            serverConnection.sendToClient("INFO nickname unknown");
                        }
                    }
                    break;

                case "PCHT":
                    // send to all in public lobby
                    serverConnection.sendToAll("PCHT " + "<" + nickName + ">" + text.substring(4));
                    break;

                case "OGAM":
                    //set new game up with this command
                    try {
                        setUpGame(text.substring(5));
                    } catch (Exception e) {
                        logger.error("Error while building up new game file.");
                        e.printStackTrace();
                        serverConnection.sendToClient("INFO error while building up new game file");
                    }
                    break;

                case "SESS":
                    for (OpenGameFile openGameFile : Server.getInstance().getOpenGameList()) {
                            serverConnection.sendToClient(
                                    "OGAM " + openGameFile.getSendReady());
                    }
                    break;

                case "LPUB":
                    sendListOfPublicGuests();
                    break;

                case "JOIN":
                    //join a game with this command
                    try {
                        System.out.println("JOIN " + serverConnection.getNickname());
                        String openGameId = text.substring(5);
                        System.out.println(0);
                        Server.getInstance().getOpenGameFile(openGameId)
                                .addParticipant(serverConnection);
                        System.out.println(1);
                        messageHandlerServer.setJoinedOpenGame(openGameId);
                        System.out.println(2);
                        logger.debug("User " + nickName + " has joined game " + openGameId);
                        serverConnection.sendToClient("JOIN " + openGameId);
                        System.out.println(3);
                        serverConnection.sendToAll("OGAM " + Server.getInstance().getOpenGameFile(openGameId)
                                .getSendReady());
                        System.out.println(4);
                        System.out.println("openGameFile " + " openGameID " + openGameId + " " + Server.getInstance().getOpenGameFile(openGameId).readyToStart());
                        // all required players are set, then send start request to host
                        if (Server.getInstance().getOpenGameFile(openGameId).readyToStart()) {
                            Server.getInstance().getOpenGameFile(openGameId)
                                    .sendConfirmationMessage();
                        }
                    } catch (Exception e) {
                        System.out.println("exception");
                        e.printStackTrace();
                        serverConnection.sendToClient("INFO wrong format,you cannot join");
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
            serverConnection.sendToClient("INFO wrong game file format");
        } else {
            Server.getInstance().addOpenGame(openGameFile);
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
    public void sendListOfPublicGuests() {
        for (int i = 0; i < Server.getInstance().publicLobbyGuests.size(); i++) {
            serverConnection.sendToClient("LPUB " + Server.getInstance().publicLobbyGuests.get(i));
        }
    }
}