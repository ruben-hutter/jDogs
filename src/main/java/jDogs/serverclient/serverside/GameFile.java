package jDogs.serverclient.serverside;
//this class represents information of pendent games(still in lobby) or ongoing games

public class GameFile {

    String nameId;
    String host;
    String participants;
    int numberParticipants;
    int total;


    public GameFile(String nameId, String host,String total) {

        this.nameId = nameId;
        this.host = host;
        this.total = Integer.parseInt(total);
        this.participants = null;
        this.numberParticipants = 1;

    }

    public void addParticipants(String newParticipant) {
        if (participants == null) {
            participants = newParticipant;
        } else {
            participants += ";" + newParticipant;
        }
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
}
