package jDogs.serverclient.serverside;

import jDogs.player.Player;
import jDogs.serverclient.clientside.ClientMenuCommand;
import jDogs.serverclient.helpers.Queuejd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SeparateLobbyCommand {

    private Queuejd sendToThisClient;
    private Queuejd sendToAll;
    private Queuejd sendToPub;
    private SendFromServer[] senderArray;
    private ServerConnection serverConnection;
    private GameFile gameFile;
    private String nickname;
    private static final Logger logger = LogManager.getLogger(SeparateLobbyCommand.class);

    SeparateLobbyCommand (Queuejd sendToThisClient, Queuejd sendToAll, Queuejd sendToPub, ServerConnection serverConnection) {
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.serverConnection = serverConnection;
        this.sendToPub = sendToPub;
        this.gameFile = null;
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
                    gameFile.sendMessageToParticipants("LCHT " + "<" + nickname + "> " + text.substring(5));
                    break;

                case "PCHT":
                    //send message to everyone logged in, in lobby, separated or playing

                    sendToAll.enqueue("PCHT " + "<" + nickname + "> " + text.substring(5));
                    break;

                case "TEAM":
                    System.out.println("team " + text.substring(5));
                    gameFile.changeTeam(text.substring(5));
                    break;

                case "STAR":
                    // client confirms to start the game

                    if (gameFile.readyToStart() && gameFile.getHost().equals(nickname)) {
                        logger.debug("gamefile ready to start? " + gameFile.readyToStart());
                        logger.debug("nickname: " + nickname);
                        logger.debug("host: " +gameFile.getHost());
                        System.out.println("starting game ");
                        gameFile.start();
                        logger.debug("Game started");

                    }
                    break;

                case "QUIT":


                    if (this.gameFile.getHost() == nickname) {
                        this.gameFile.cancel();
                        Server.getInstance().allGamesNotFinished.remove(this.gameFile);
                    } else {
                        this.gameFile.removeParticipant(serverConnection);
                        sendToAll.enqueue("OGAM " + this.gameFile.getSendReady());
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
                    for (Player player : gameFile.getPlayers())
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
        for (int i = 0; i < gameFile.getNumberOfParticipants(); i++) {
            if (gameFile.getParticipantsArray()[i].equals(destiny)) {
                return true;
            }
        }
        return false;
    }

    private GameFile getGame(String gameName) {
        return Server.getInstance().getNotFinishedGame(gameName);
    }


    /**
     *
     * @param gameFile  the gameFile the client opened
     * @param nickname the nickname of this client
     */

    // if client opened the game
    public void setGameFile(GameFile gameFile, String nickname) {
        this.gameFile = gameFile;
        this.nickname = nickname;
    }
    // if client joined a game

    public void setJoinedGame(GameFile gameFile, String nickname) {
        this.gameFile = gameFile;
        this.nickname = nickname;
    }
}
