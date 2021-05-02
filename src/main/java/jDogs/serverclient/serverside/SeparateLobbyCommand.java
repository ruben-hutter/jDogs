package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.serverclient.helpers.Queuejd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SeparateLobbyCommand {

    private ServerConnection serverConnection;
    private String openGameFileID;
    private String nickname;
    private static final Logger logger = LogManager.getLogger(SeparateLobbyCommand.class);


    SeparateLobbyCommand (ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.openGameFileID = null;
    }

    public void execute(String text) {
        logger.debug("Entered SeparateLobbyCommand with: " + text);

        String command = text.substring(0, 4);

            switch (command) {

                case "WCHT":
                    //send private message
                    String mess = text.substring(5);
                    int separator = -1;
                    for (int i = 0; i < mess.length(); i++) {
                        if (Character.isWhitespace(mess.charAt(i))) {
                            separator = i;
                            break;
                        }
                    }

                    if (separator == -1) {
                        serverConnection.sendToClient("INFO " + "wrong WCHT format");
                        break;
                    }
                    separator = separator + 5;
                    String destiny = text.substring(5, separator);
                    String message = text.substring(separator + 1);

                    if (!isParticipant(destiny)) {
                        serverConnection.sendToClient
                                ("INFO " + destiny + " is not part of the joined group.");
                        break;
                    }

                    try {
                        Server.getInstance().getServerConnection(destiny).
                                sendToClient("WCHT " + "@" + serverConnection.getNickname()
                                        + ": " + message);
                    } catch (Exception e) {
                        //prevent shutdown if nickname doesn`t exist in hashmap
                        serverConnection.sendToClient("INFO nickname unknown");
                    }
                    break;

                case "LCHT":
                    Server.getInstance().getOpenGameFile(openGameFileID).sendMessageToParticipants("LCHT " + "<" + nickname + "> " + text.substring(5));
                    break;

                case "PCHT":
                    //send message to everyone logged in, in lobby, separated or playing
                    serverConnection.sendToAll("PCHT " + "<" + nickname + "> " + text.substring(5));
                    break;

                case "TEAM":
                    Server.getInstance().getOpenGameFile(openGameFileID).changeTeam(text.substring(5));
                    break;

                case "STAR":
                    // client confirms to start the game
                    if (Server.getInstance().getOpenGameFile(openGameFileID).readyToStart() && Server.getInstance().getOpenGameFile(openGameFileID).getHost().equals(nickname)) {
                        Server.getInstance().getOpenGameFile(openGameFileID).start();
                    }
                    break;

                case "EXIT":
                    Server.getInstance().getOpenGameFile(openGameFileID).sendMessageToParticipants("INFO " + nickname + " left openGame session");
                    if (Server.getInstance().getOpenGameFile(openGameFileID).getHost().equals(serverConnection.getNickname())) {
                        Server.getInstance().removeOpenGame(openGameFileID);
                    }
                    this.serverConnection.kill();
                    break;

                case "QUIT":

                    if (Server.getInstance().getOpenGameFile(openGameFileID).getHost().equals(serverConnection.getNickname())) {
                        Server.getInstance().removeOpenGame(openGameFileID);
                    } else {
                        Server.getInstance().getOpenGameFile(openGameFileID).removeParticipant(serverConnection.getNickname());
                    }
                    break;

                case "STAT":
                    serverConnection.sendToClient(
                            "STAT " + "runningGames " + Server.getInstance().runningGames.size() +
                                    " finishedGames " + Server.getInstance().finishedGames.size());
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

                case "LPUB":
                    for (Player player : Server.getInstance().getOpenGameFile(openGameFileID).getPlayers())
                        serverConnection.sendToClient("LPUB " + player.getPlayerName());
                    break;

                default:
                    serverConnection.sendToClient(
                            "INFO this command " + command + " is not implemented in lobby");
                    System.err.println("received unknown message in lobby from " + nickname);
                    System.err.println(text);
            }
        }

    /**
     * checks if name is in participants array
     * @param destiny name of possible participant
     * @return boolean
     */
    private boolean isParticipant(String destiny) {
        for (String name : Server.getInstance().getOpenGameFile(openGameFileID).getParticipantsArray()) {
            if (name.equals(destiny)) {
                return true;
            }
        }
        return false;
    }


    /**
     * adds the name of the joined game to the class
     * @param openGameFileID name of game
     */
    public void setJoinedGame(String openGameFileID) {
        this.openGameFileID = openGameFileID;
        this.nickname = serverConnection.getNickname();
    }
}
