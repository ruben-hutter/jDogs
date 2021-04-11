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
    private ArrayList<ServerConnection> scArrayList = new ArrayList<>();


    public GameFile(String nameId, String host,String total, ServerConnection serverConnection) {

        this.nameId = nameId;
        this.host = host;
        this.total = Integer.parseInt(total);
        this.participants = host;
        this.numberParticipants = 1;
        this.confirmedParticipants = host;
        this.numberOfConfirmed = 0;
        this.scArrayList.add(serverConnection);
        sendMessageToParticipants("OGAM " + getSendReady());

    }

    private void sendMessageToParticipants(String message) {
        for (int i = 0; i < scArrayList.size(); i++) {
            scArrayList.get(i).getSender().sendStringToClient(message);
        }

    }

    public void addParticipants(ServerConnection serverConnection) {
        scArrayList.add(serverConnection);
        participants += " " + serverConnection.getNickname();
        numberParticipants++;
    }

    public String getParticipants() {
        return participants;
    }

    public String getSendReady() {
         return nameId + " " + host + " " + total;
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

                    if (participants.charAt(i) == ';') {
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
        Server.getInstance().startGame(new MainGame(this));
        for (int i = 0; i < scArrayList.size(); i++) {
            scArrayList.get(i).getMessageHandlerServer().setPlaying(true);
        }
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
            System.err.println(this.toString() + " tried to delete host as participant. Forbidden!");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(participants);
            int firstChar = sb.indexOf(nickName);
            sb.delete(firstChar - 1,firstChar + nickName.length());
            participants = sb.toString();

            scArrayList.remove(serverConnection);
        }

    }

    public void cancel() {
        for (int i = 0; i < scArrayList.size(); i++) {
            scArrayList.get(i).getMessageHandlerServer().returnToLobby();
        }
    }

    public ArrayList<ServerConnection> getscArrayList() {
        return scArrayList;
    }
}
