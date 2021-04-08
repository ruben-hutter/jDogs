package jDogs.serverclient.serverside;
//this class represents information of pendent games(still in lobby) or ongoing games

public class GameFile {

    private String nameId;
    private String host;
    private String participants;
    private String confirmedParticipants;
    private int numberOfConfirmed;
    private int numberParticipants;
    private int total;


    public GameFile(String nameId, String host,String total) {

        this.nameId = nameId;
        this.host = host;
        this.total = Integer.parseInt(total);
        this.participants = host;
        this.numberParticipants = 1;
        this.confirmedParticipants = host;
        this.numberOfConfirmed = 0;

    }

    public void addParticipants(String newParticipant) {
        participants += ";" + newParticipant;
        numberParticipants++;
    }

    public String getParticipants() {
        return participants;
    }

    public String getSendReady() {
         return nameId + ";" + host + ";" + total;
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

    /**
     *
     * @param nickName is the participant which should be removed
     *                 attention: it doesn`t work for the host
     *                 the host should be removed
     *                 by deleting the whole file
     */
    public void removeParticipant(String nickName) {
        if (nickName == host) {
            System.err.println(this.toString() + " tried to delete host as participant. Forbidden!");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(participants);
            int firstChar = sb.indexOf(nickName);
            sb.delete(firstChar - 1,firstChar + nickName.length());
            participants = sb.toString();
        }

    }
}
