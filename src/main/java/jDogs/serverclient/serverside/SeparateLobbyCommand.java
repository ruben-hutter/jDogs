package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Queuejd;

public class SeparateLobbyCommand {

    private Queuejd sendToThisClient;
    private Queuejd sendToAll;
    private SendFromServer[] senderArray;
    private ServerConnection serverConnection;
    private GameFile gameFile;
    private String nickname;

    SeparateLobbyCommand (Queuejd sendToThisClient, Queuejd sendToAll, ServerConnection serverConnection) {
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.serverConnection = serverConnection;
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

                case "PCHT":
                    //sendToAll.enqueue("PCHT " + "<" + nickname + ">" + text.substring(4));
                    for (int i = 0; i < gameFile.getscArrayList().size(); i++) {
                        gameFile.getscArrayList().get(i).getSender()
                                .sendStringToClient(text.substring(5));
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
                        sendToAll.enqueue("DOGA " + this.gameFile.getNameId());
                    } else {
                        this.gameFile.removeParticipant(serverConnection);
                        sendToAll.enqueue("DPER " + this.gameFile.getNameId() + " " + nickname);
                    }
                    break;

                case "STAT":
                    sendToThisClient.enqueue(
                            "STAT " + "runningGames " + Server.getInstance().runningGames.size() +
                                    " finishedGames " + Server.getInstance().finishedGames.size());
                    break;

                case "ACTI":
                    sendToThisClient.enqueue(
                            "ACTI all joining this game: " + this.gameFile.getParticipants());
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
