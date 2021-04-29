package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.serverclient.helpers.Queuejd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SeparateLobbyCommand {

    private Queuejd sendToThisClient;
    private Queuejd sendToAll;
    private Queuejd sendToPub;
    private SendFromServer[] senderArray;
    private ServerConnection serverConnection;
    private OpenGameFile openGameFile;
    private String nickname;
    private static final Logger logger = LogManager.getLogger(SeparateLobbyCommand.class);

    SeparateLobbyCommand (Queuejd sendToThisClient, Queuejd sendToAll, Queuejd sendToPub, ServerConnection serverConnection) {
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.serverConnection = serverConnection;
        this.sendToPub = sendToPub;
        this.openGameFile = null;
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
                    //sendToAll.enqueue("PCHT " + "<" + nickname + ">" + text.substring(4));

                    System.out.println("LCHT: " + text.substring(5));
                    openGameFile.sendMessageToParticipants("LCHT " + "<" + nickname + "> " + text.substring(5));
                    break;

                case "PCHT":
                    //send message to everyone logged in, in lobby, separated or playing

                    sendToAll.enqueue("PCHT " + "<" + nickname + "> " + text.substring(5));
                    break;

                case "TEAM":
                    System.out.println("team " + text.substring(5));
                    openGameFile.changeTeam(text.substring(5));
                    break;

                case "STAR":
                    // client confirms to start the game
                    if (openGameFile.readyToStart() && openGameFile.getHost().equals(nickname)) {
                        logger.debug("gamefile ready to start? " + openGameFile.readyToStart());
                        logger.debug("nickname: " + nickname);
                        logger.debug("host: " + openGameFile.getHost());
                        openGameFile.start();
                        logger.debug("Game started");
                    }
                    break;

                case "EXIT":
                    this.openGameFile.sendMessageToParticipants("INFO " + nickname + " left openGame session");
                    this.openGameFile.cancel();
                    this.serverConnection.kill();
                    break;

                case "QUIT":

                    if (this.openGameFile.getHost() == nickname) {
                        this.openGameFile.cancel();
                        Server.getInstance().allGamesNotFinished.remove(this.openGameFile);
                    } else {
                        this.openGameFile.removeParticipant(serverConnection.getNickname());
                        sendToAll.enqueue("OGAM " + this.openGameFile.getSendReady());
                    }
                    serverConnection.getMessageHandlerServer().returnToLobby();
                    sendToPub.enqueue("LPUB " + nickname);
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
                    for (Player player : openGameFile.getPlayers())
                    sendToThisClient.enqueue("LPUB " + player.getPlayerName());
                    break;

                default:
                    sendToThisClient.enqueue(
                            "INFO this command " + command + " is not implemented in lobby");
                    System.err.println("received unknown message in lobby from " + nickname);
                    System.err.println(text);
            }
        }


    private boolean isParticipant(String destiny) {
        for (int i = 0; i < openGameFile.getNumberOfParticipants(); i++) {
            if (openGameFile.getParticipantsArray()[i].equals(destiny)) {
                return true;
            }
        }
        return false;
    }

    private OpenGameFile getGame(String gameName) {
        return Server.getInstance().getNotFinishedGame(gameName);
    }


    /**
     *
     * @param openGameFile  the gameFile the client opened
     * @param nickname the nickname of this client
     */

    // if client opened the game
    public void setGameFile(OpenGameFile openGameFile, String nickname) {
        this.openGameFile = openGameFile;
        this.nickname = nickname;
    }
    // if client joined a game

    public void setJoinedGame(OpenGameFile openGameFile, String nickname) {
        this.openGameFile = openGameFile;
        this.nickname = nickname;
    }
}
