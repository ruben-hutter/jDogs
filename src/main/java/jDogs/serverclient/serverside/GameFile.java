package jDogs.serverclient.serverside;
//this class represents information of pendent games(still in lobby) or ongoing games

import java.util.ArrayList;
import java.util.BitSet;

public class GameFile {

    private String nameId;
    private String host;
    private String participants;
    private String confirmedParticipants;
    private int numberOfConfirmed;
    private int numberParticipants;
    private int total;
    private boolean pendent;
    private ArrayList<ServerConnection> scArrayList = new ArrayList<>();
    private MainGame mainGame;


    public GameFile(String nameId, String host,String total, ServerConnection serverConnection) {

        this.nameId = nameId;
        this.host = host;
        this.total = Integer.parseInt(total);
        this.participants = host;
        this.numberParticipants = 1;
        this.confirmedParticipants = host;
        this.numberOfConfirmed = 0;
        this.scArrayList.add(serverConnection);
        this.pendent = true;
        sendMessageToParticipants("OGAM " + getSendReady());

    }

    public void sendMessageToParticipants(String message) {
        for (int i = 0; i < scArrayList.size(); i++) {
            scArrayList.get(i).getSender().sendStringToClient(message);
        }

    }

    public void addParticipants(ServerConnection serverConnection) {
        scArrayList.add(serverConnection);
        participants += " " + serverConnection.getNickname();
        numberParticipants++;
        sendMessageToParticipants("LPUB " + serverConnection.getNickname());
        for (int i = 0; i < numberParticipants - 1; i++) {
            serverConnection.getSender().sendStringToClient("LPUB " + getParticipantsArray()[i]);
        }
    }

    public String getParticipants() {
        return participants;
    }

    public String getSendReady() {
         return nameId + " " + host + " " + numberParticipants + " " + total;
    }

    public String getNameId() {
        return nameId;
    }

    public String getHost() {
        return host;
    }

    public String[] getParticipantsArray() {
        String[] array = new String[numberParticipants];
        int arrayEntries = 0;
        int separator = 0;
        if (numberParticipants > 1) {
            while (arrayEntries < numberParticipants - 1) {
                int i = separator;
                while (i < participants.length()) {

                    if (Character.isWhitespace(participants.charAt(i))) {
                        array[arrayEntries] = participants.substring(separator, i);
                        separator = i + 1;
                        arrayEntries++;
                        i = participants.length();
                    }
                    i++;
                }

                array[arrayEntries] = participants.substring(separator);
            }
        } else {
            array[0] = participants;
            System.out.println("array: " + array[0] + " " + array.length);
        }
        return array;
    }

    public boolean readyToStart() {
        return total == numberParticipants;
    }

    public void sendConfirmationMessage() {
        sendMessageToParticipants("STAR");

    }

    public int getNumberOfParticipants() {
        return numberParticipants;
    }

    public void confirmStart(String nickName) {
        if (numberOfConfirmed == 0) {
            confirmedParticipants = nickName;
        } else {
            confirmedParticipants += nickName;
        }
        numberOfConfirmed++;
    }

    public boolean startGame() {
        return total == numberOfConfirmed;
    }

    public void start() {
        pendent = false;
        //Server.getInstance().startGame(new MainGame(this));
        for (int i = 0; i < scArrayList.size(); i++) {
            scArrayList.get(i).getMessageHandlerServer().setPlaying(true,this);
            mainGame = new MainGame(this);
            Server.getInstance().startGame(mainGame);
        }
    }

    public MainGame getMainGame() {
        return mainGame;
    }

    /**
     *
     * @param serverConnection is the participant which should be removed
     *                 attention: it doesn`t work for the host
     *                 the host should be removed
     *                 by deleting the whole file
     */
    public void removeParticipant(ServerConnection serverConnection) {
        String nickName = serverConnection.getNickname();

        if (nickName == host) {
            cancel();
        } else {
            }
            if (isPendent()) {
                int number = -1;
                String[] array = getParticipantsArray();
                for (int i = 0; i < array.length; i++) {
                    if (array[i].equals(nickName)) {
                        number = i;
                    }
                }

                    //get whitespace
                    int counter = 0;
                int startPosition = 0;
                    for (int i = 0; i < participants.length(); i++) {
                        if (Character.isWhitespace(participants.charAt(i))) {
                            counter++;
                            if (counter == number) {
                                startPosition = i;
                                break;
                            }
                        }
                    }

                StringBuilder sb = new StringBuilder();
                sb.append(participants);
                sb.delete(startPosition - 1, startPosition + nickName.length());
                participants = sb.toString();
                numberParticipants--;

                scArrayList.remove(serverConnection);
                sendMessageToParticipants("DPER " + nickName);
                Server.getInstance().getSender(nickName).sendStringToAllClients("OGAM " + getSendReady());
            } else {
                // if serverConnection of a client stops while playing the server sends all clients back to public lobby
                sendMessageToParticipants("INFO " + " connection to " + nickName + " is shutdown");
                cancel();
            }
        }


    public void cancel() {

        for (int i = 0; i < scArrayList.size(); i++) {
            scArrayList.get(i).getMessageHandlerServer().returnToLobby();
        }
        if (pendent) {sendMessageToAll("DOGA " + getSendReady());
        } else {
            System.out.println("running game ended no command implemented");
        }

        Server.getInstance().removeGame(this);

    }

    private void sendMessageToAll(String message) {
        for (ServerConnection serverConnection1 : Server.getInstance().serverConnections) {
            serverConnection1.getSender().sendStringToAllClients(message);
        }
    }

    public ArrayList<ServerConnection> getscArrayList() {
        return scArrayList;
    }

    public boolean isPendent() {
        return pendent;
    }
}
