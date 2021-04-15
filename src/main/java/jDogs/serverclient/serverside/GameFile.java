package jDogs.serverclient.serverside;
//this class represents information of pendent games(still in lobby) or ongoing games

import jDogs.gui.GuiParser;
import jDogs.player.Player;
import java.util.ArrayList;

public class GameFile {

    private String nameId;
    private String host;
    private String participants;
    private String confirmedParticipants;
    private int numberOfConfirmed;
    private int numberParticipants;
    private int IDCounter;
    private int total;
    private boolean pendent;
    private MainGame mainGame;
    private boolean teamMode;
    private ArrayList<Player> players = new ArrayList<>();

    public GameFile(String nameId, String host,String total, boolean teamMode, ServerConnection serverConnection) {
        this.IDCounter = 0;
        this.nameId = nameId;
        this.host = host;
        this.total = Integer.parseInt(total);
        this.participants = host;
        this.numberParticipants = 1;
        this.confirmedParticipants = host;
        this.numberOfConfirmed = 0;
        this.pendent = true;
        this.teamMode = teamMode;
        players.add(new Player(host,IDCounter++, serverConnection));


        sendMessageToParticipants("OGAM " + getSendReady());

    }

    /**
     * a team consists of those participants
     * at even or odd positions
     */
    public void changeTeam(String applicant, String subject) {
        if (total == 4) {
            getPlayer(applicant).setTeamIDToPersID();
            getPlayer(subject).setTeamID(getPlayer(applicant).getPersonalTeamID());
        }

        if (total == 6) {
            //not implemented check with parseMessage
        }


    }

    private Player getPlayer(String applicant) {
        for (Player player : players) {
            if (player.getPlayerName().equals(applicant)) {
                return player;
            }
        }
        return null;
    }

    private String[] parseNames(int size, String message) {
        String[] array = new String[size];
        int position = 0;
        int i = 0;
        int count = 1;
        while (count < size) {

            if (Character.isWhitespace(message.charAt(i))) {
                array[count] = message.substring(position, i);
                position = i + 1;
                count++;
            }
            i++;
        }
        array[count - 1] = message.substring(position);
        return array;
    }


    public void sendMessageToParticipants(String message) {
        for (Player player : players) {
            player.getServerConnection().getSender().sendStringToClient(message);
        }
    }

    public void addParticipant(ServerConnection serverConnection) {
        players.add(new Player(serverConnection.getNickname(),IDCounter++, serverConnection));

        sendMessageToParticipants("LPUB " + serverConnection.getNickname());
        for (int i = 0; i < numberParticipants - 1; i++) {
            players.get(i).sendMessageToClient("LPUB " + serverConnection.getNickname());
        }
    }

    public String getParticipants() {
        String participants = "";

        for (Player player : players) {
            participants += player.getPlayerName();
        }

        return participants;
    }

    public String getSendReady() {
         return nameId + " " + host + " " + numberParticipants + " " + total + " " + teamMode;
    }

    public String getNameId() {
        return nameId;
    }

    public String getHost() {
        return host;
    }

    public String[] getParticipantsArray() {
        String[] array = new String[numberParticipants];
        int count = 0;
        for (Player player : players) {
            array[count] = player.getPlayerName();
            count++;
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
        for (int i = 0; i < players.size(); i++) {
            players.get(i).getServerConnection().getMessageHandlerServer().setPlaying(true, this);
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
        String nickname = serverConnection.getNickname();

        if (pendent) {
            Player player = getPlayer(nickname);
            players.remove(player);
            numberParticipants--;
            sendMessageToParticipants("DPER " + nickname);
            Server.getInstance().getSender(nickname)
                    .sendStringToAllClients("OGAM " + getSendReady());
        }  else {
        // if serverConnection of a client stops while playing the server sends all clients back to public lobby
        sendMessageToParticipants("INFO " + " connection to " + nickname + " is shutdown");
        cancel();
        }
    }


    public void cancel() {

        for (Player player : players) {
            player.getServerConnection().getMessageHandlerServer().returnToLobby();
        }
        if (pendent) {sendMessageToAll("DOGA " + getSendReady());
        } else {
            Server.getInstance().finishedGames.add(this);
            System.out.println("INFO game finished");
        }
        Server.getInstance().removeGame(this);

    }

    private void sendMessageToAll(String message) {
        for (ServerConnection serverConnection1 : Server.getInstance().serverConnections) {
            serverConnection1.getSender().sendStringToAllClients(message);
        }
    }

    public boolean isPendent() {
        return pendent;
    }

    public Player[] getPlayersArray() {
        Player[] array = new Player[players.size()];
        int count = 0;
        for (Player player : players) {
            array[count] = player;
            count++;
        }
        return array;
    }
}
