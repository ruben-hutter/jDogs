package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Queuejd;

public class SeparateLobbyCommand {

    private Queuejd sendToThisClient;
    private Queuejd sendToAll;
    private Queuejd sendToPub;
    private SendFromServer[] senderArray;
    private ServerConnection serverConnection;
    private GameFile gameFile;
    private String nickname;

    SeparateLobbyCommand (Queuejd sendToThisClient, Queuejd sendToAll, Queuejd sendToPub, ServerConnection serverConnection) {
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.serverConnection = serverConnection;
        this.sendToPub = sendToPub;
        this.gameFile = null;
    }

    public void execute(String text) {

        String command = text.substring(0, 4);

            switch (command) {

                case "WCHT":
                    //send private message

                    int separator = -1;
                    for (int i = 0; i < text.substring(5).length(); i++) {
                        if (Character.isWhitespace(text.substring(4).charAt(i))) {
                            separator = i;
                            break;
                        }
                    }

                    if (separator == -1) {
                        sendToThisClient.enqueue("INFO " + "wrong WCHT format");
                        break;
                    }

                    String destiny = text.substring(5, 4 + separator);
                    String message = text.substring(5 + separator);

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
                    if (gameFile.getPlayer(text.substring(5)) != null) {
                        gameFile.changeTeam(nickname, text.substring(5));
                    } else {
                        sendToThisClient.enqueue("INFO error client doesn`t exist");
                    }
                    break;

                case "STAR":

                    // client confirms to start the game

                    GameFile gameFile = getGame(text.substring(5));
                    if (gameFile == null) {
                        sendToThisClient.enqueue("INFO game name does not exist on server");
                        break;
                    }
                    gameFile.confirmStart(nickname);

                    //set all to game mode

                    if (gameFile.startGame()) {
                        gameFile.start();
                    }
                    break;

                case "QUIT":

                    System.out.println(
                            nickname + " doesn`t join anymore " + this.gameFile.getNameId());
                    if (this.gameFile.getHost() == nickname) {
                        this.gameFile.cancel();
                        Server.getInstance().allGamesNotFinished.remove(this.gameFile);
                        sendToAll.enqueue("DOGA " + this.gameFile.getSendReady());
                    } else {
                        this.gameFile.removeParticipant(serverConnection);
                        sendToAll.enqueue("OGAM " + this.gameFile.getSendReady());
                    }
                    System.out.println(2);
                    serverConnection.getMessageHandlerServer().returnToLobby();
                    sendToPub.enqueue("LPUB " + nickname);
                    System.out.println(3);
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
