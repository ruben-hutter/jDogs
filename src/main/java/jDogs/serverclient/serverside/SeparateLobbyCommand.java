package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Queuejd;
import java.util.ArrayList;

public class SeparateLobbyCommand {

    private Queuejd sendToThisClient;
    private Queuejd sendToAll;
    private ArrayList<ServerConnection> scArrayList;
    private SendFromServer[] senderArray;
    private ServerConnection serverConnection;
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

                System.out.println(nickname + " doesn`t join anymore " + this.gameFile.getNameId());
                if (this.gameFile.getHost() == nickname) {
                    Server.getInstance().allGamesNotFinished.remove(this.gameFile);
                    scArrayList.forEach(serverConnection1 -> serverConnection1.getMessageHandlerServer().setPlaying(false));
                    sendToAll.enqueue("DOGA " + this.gameFile.getNameId());
                } else {
                    this.gameFile.removeParticipant(nickname);
                    scArrayList.remove(serverConnection);
                    //TODO change the following line of code
                    scArrayList.forEach(serverConnection1 -> serverConnection1.getMessageHandlerServer().getSeparateLobbyCommand().removeParticipant(serverConnection));

                    sendToAll.enqueue("DPER " + this.gameFile.getNameId() + " " + nickname);
                }
                break;

        case "STAT":
            sendToThisClient.enqueue("STAT " + "runningGames " + Server.getInstance().runningGames.size() +
                    " finishedGames " + Server.getInstance().finishedGames.size());
            break;


        default:
            sendToThisClient.enqueue("INFO this command " + command + " is not implemented in lobby");
            System.err.println("received unknown message in lobby from " + nickname);
            System.err.println(text);
        }
    }

    private void removeParticipant(ServerConnection exMemberServerConnection) {

        scArrayList.remove(exMemberServerConnection);

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
        scArrayList.forEach(serverConnection1 -> serverConnection1.getMessageHandlerServer().setPlaying(true));

    }

    private GameFile getGame(String gameName) {
        return Server.getInstance().getNotFinishedGame(gameName);
    }

    private void createServerConnectionList() {
        this.scArrayList = Server.getInstance().getServerConnectionsMap(gameFile);
        int size = scArrayList.size();
        for (int i = 0; i < size; i++) {
            this.scArrayList.get(i).getMessageHandlerServer().getSeparateLobbyCommand().addParticipant(this.serverConnection);
        }
        System.out.println("done");

    }

    private void addParticipant(ServerConnection newServerConnection) {
        scArrayList.add(newServerConnection);
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
        this.scArrayList = Server.getInstance().getServerConnectionsMap(gameFile);

    }
    // if client joined a game

    public void setJoinedGame(GameFile gameFile, String nickname) {
        this.gameFile = gameFile;
        this.nickname = nickname;
        createServerConnectionList();
    }
}
