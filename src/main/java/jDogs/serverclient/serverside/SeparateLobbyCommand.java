package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.serverclient.helpers.Queuejd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SeparateLobbyCommand {

    private Queuejd sendToThisClient;
    private Queuejd sendToAll;
    private Queuejd sendToPub;
    private ServerConnection serverConnection;
    private static final Logger logger = LogManager.getLogger(SeparateLobbyCommand.class);
    private String openGameFileID;
    private String nickname;

    SeparateLobbyCommand (Queuejd sendToThisClient, Queuejd sendToAll, Queuejd sendToPub, ServerConnection serverConnection) {
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.serverConnection = serverConnection;
        this.sendToPub = sendToPub;
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
                        sendToThisClient.enqueue("INFO " + "wrong WCHT format");
                        break;
                    }
                    String destiny = text.substring(0, separator);
                    String message = text.substring(separator + 1);

                    if (!isParticipant(destiny)) {
                        sendToThisClient
                                .enqueue("INFO " + destiny + " is not part of the joined group.");
                        break;
                    }

                    try {
                        Server.getInstance().getSender(destiny).
                                sendStringToClient("WCHT " + "@" + serverConnection.getNickname()
                                        + ": " + message);
                    } catch (Exception e) {
                        //prevent shutdown if nickname doesn`t exist in hashmap
                        sendToThisClient.enqueue("INFO nickname unknown");
                    }
                    break;

                case "LCHT":
                    Server.getInstance().getOpenGameFile(openGameFileID).sendMessageToParticipants("LCHT " + "<" + nickname + "> " + text.substring(5));
                    break;

                case "PCHT":
                    //send message to everyone logged in, in lobby, separated or playing
                    sendToAll.enqueue("PCHT " + "<" + nickname + "> " + text.substring(5));
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
                    serverConnection.getMessageHandlerServer().returnToLobby();
                    break;

                case "STAT":
                    sendToThisClient.enqueue(
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
                    sendToThisClient.enqueue(list);
                    break;

                case "LPUB":
                    for (Player player : Server.getInstance().getOpenGameFile(openGameFileID).getPlayers())
                    sendToThisClient.enqueue("LPUB " + player.getPlayerName());
                    break;

                default:
                    sendToThisClient.enqueue(
                            "INFO this command " + command + " is not implemented in lobby");
                    System.err.println("received unknown message in lobby from " + nickname);
                    System.err.println(text);
            }
        }

    /**
     * check if name is in participants array
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



    public void setJoinedGame(String openGameFileID) {
        this.openGameFileID = openGameFileID;
        this.nickname = serverConnection.getNickname();
    }
}
