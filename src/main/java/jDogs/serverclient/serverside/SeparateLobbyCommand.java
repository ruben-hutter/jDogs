package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Queuejd;
import java.util.ArrayList;

public class SeparateLobbyCommand {

    private Queuejd sendToThisClient;
    private Queuejd sendToAll;
    private ArrayList<ServerConnection> scArrayList;
    private SendFromServer[] senderArray;
    private ServerConnection serverConnection;
    private String actualGame;
    private GameFile gameFile;
    private String nickname;

    SeparateLobbyCommand (Queuejd sendToThisClient, Queuejd sendToAll, ServerConnection serverConnection) {
        this.sendToThisClient = sendToThisClient;
        this.sendToAll = sendToAll;
        this.serverConnection = serverConnection;
        this.scArrayList = new ArrayList<>();
        this.gameFile = null;
    }

    public void execute(String text) {

        String command = text.substring(0,4);

    switch(command) {


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

                String destiny = text.substring(5,4 + separator);
                String message = text.substring(5 + separator);

                if (!isParticipant(destiny)) {
                    sendToThisClient.enqueue("INFO " + destiny + " is not part of the joined group.");
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
            sendMessageToMembers(text.substring(4));
            break;

        case "STAR":

            // client confirms to start the game

            if (actualGame != gameFile.getNameId()) {
                sendToThisClient.enqueue("INFO denied you didn`t join game");
                break;
            }
            GameFile gameFile = getGame(text.substring(5));
            if (gameFile == null) {
                sendToThisClient.enqueue("INFO game name does not exist on server");
                break;
            }
            gameFile.confirmStart(nickname);

            //set all to game mode

            if (gameFile.startGame()) {
                changeServerConnectionsToPlay(true);
                Server.getInstance().startGame(new MainGame(gameFile));
            }
            break;

        case "QUIT":

                System.out.println(nickname + " doesn`t join anymore " + actualGame);
                if (this.gameFile.getHost() == nickname) {
                    Server.getInstance().allGamesNotFinished.remove(this.gameFile);
                    scArrayList.forEach(serverConnection1 -> serverConnection1.getMessageHandlerServer().setPlaying(false));
                    sendToAll.enqueue("DOGA " + this.gameFile.getNameId());
                } else {
                    this.gameFile.removeParticipant(nickname);
                    sendToAll.enqueue("DPER " + this.gameFile.getNameId() + " " + nickname);
                }
                break;


        default:
            sendToThisClient.enqueue("INFO this command " + command + " is not implemented in lobby");
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

    private void sendMessageToMembers(String message) {
            scArrayList.forEach(ServerConnection1 -> ServerConnection1.getSender().sendStringToClient(message));
    }

    private void changeServerConnectionsToPlay(boolean boole) {
        for (int i = 0; i < scArrayList.size(); i++) {
            scArrayList.get(i).getMessageHandlerServer().setPlaying(boole);
        }
    }

    private GameFile getGame(String gameName) {
        return Server.getInstance().getNotFinishedGame(gameName);
    }

    private void createServerConnectionList() {
        this.scArrayList = Server.getInstance().getServerConnections(gameFile);
        getSenderArray();
    }

    private void getSenderArray() {
        for (int i = 0; i < scArrayList.size(); i++) {
            this.senderArray[i] = scArrayList.get(i).getSender();
        }
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
        createServerConnectionList();
    }
    // if client joined a game
    public void setJoinedGame(String actualGame, String nickname) {
        this.actualGame = actualGame;
        this.nickname = nickname;
    }
}
