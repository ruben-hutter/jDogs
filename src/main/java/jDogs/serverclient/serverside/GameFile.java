package jDogs.serverclient.serverside;
//this class represents information of pendent games(still in lobby) or ongoing games

import jDogs.player.Player;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * an object of this class is instantiated when a client
 * wants to create a new game in the lobby, after enough
 * people joined the object of this class
 * is transferred to main game
 */
public class GameFile {

    private String nameId;
    private String host;
    private String participants;
    private String confirmedParticipants;
    private int numberOfConfirmed;
    private int numberParticipants;
    private int total;
    private boolean pendent;
    private MainGame mainGame;
    private int teamMode;
    private ArrayList<Player> players = new ArrayList<>();
    private int teamIDs;
    private static final Logger logger = LogManager.getLogger(GameFile.class);

    public GameFile(String nameId, String host, String total, int teamMode,
            ServerConnection serverConnection) {
        this.nameId = nameId;
        this.host = host;
        this.total = Integer.parseInt(total);
        this.participants = host;
        this.numberParticipants = 1;
        this.confirmedParticipants = host;
        this.numberOfConfirmed = 0;
        this.pendent = true;
        this.teamMode = teamMode;
        setUpTeamMode();

        players.add(new Player(host, serverConnection));

        sendMessageToParticipants("OGAM " + getSendReady());
    }

    /**
     * if team mode is activated it is bigger than one, and to get the team Ids divide total by
     * teamMode
     */
    private void setUpTeamMode() {

        if (teamMode > 0) {
            this.teamIDs = total / teamMode + 1;
        }
    }

    /**
     * @param combination is a string with the teamsize, number of names transmitted and with the
     *                    names which should be together in a team
     */
    public void changeTeam(String combination) {
        //e.g. format of combination 2 4 Gregor Ruben Johanna Joe
        // teams of two
        //Gregor - Ruben & Johanna - Joe
        int teamSize = combination.charAt(0);
        int sizeNames = combination.charAt(1);
        if (sizeNames == numberParticipants) {

            String[] array = parseNames(sizeNames, combination.substring(3));
            int teamID = 0;
            while (teamID < sizeNames / teamSize) {
                for (int i = 0; i < teamSize; i++) {
                    getPlayer(array[i]).setTeamID(teamID);
                }
                teamID++;
            }

            orderByTeamId();

        } else {
            // do nothing
        }
    }

    private void orderByTeamId() {
        Collections.sort(players, Player.TeamIdComparator);
        System.out.println("NEW TEAM combination " + participants);
    }


    public Player getPlayer(String name) {
        for (Player player : players) {
            if (player.getPlayerName().equals(name)) {
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
            System.out.println("send message from game file" + message);
            player.getServerConnection().getSender().sendStringToClient(message);
        }
    }

    public void addParticipant(ServerConnection serverConnection) {
        if (numberParticipants < total) {
            players.add(new Player(serverConnection.getNickname(), serverConnection));
            numberParticipants++;
            sendMessageToParticipants("LPUB " + serverConnection.getNickname());
            for (int i = 0; i < numberParticipants - 1; i++) {
                players.get(i).sendMessageToClient("LPUB " + serverConnection.getNickname());
            }

            if (teamMode == 1 && readyToStart()) {

                checkforTeams();
                OrderArrayListToPlayGame();
                // get players arraylist in definitive order
            }

        } else {
            serverConnection.getSender().sendStringToClient("INFO no more players allowed in game");
        }
    }

    /**
     * @param serverConnection is the participant which should be removed attention: it doesn`t work
     *                         for the host the host should be removed by deleting the whole file
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
        } else {
            // if serverConnection of a client stops while playing the server sends all clients back to public lobby
            sendMessageToParticipants("INFO " + " connection to " + nickname + " is shutdown");
            cancel();
        }
    }

    /**
     * this method orders the arrayList of players so that team members don`t sit next to each
     * other
     */
    private void OrderArrayListToPlayGame() {

        ArrayList<Player> newList = new ArrayList<>();

        int counter = players.get(0).getTeamID();
        int teamSize = numberParticipants / teamIDs;
        int sizeAllEntries = players.size();

        while (newList.size() < sizeAllEntries - 1) {
            for (int j = 0; j < players.size() && j < teamSize; j++) {
                if (players.get(j).getTeamID() == counter) {
                    System.out.println(players.get(j));
                    newList.add(players.get(j));
                    players.remove(j);
                    counter++;
                }
            }
            counter = 0;
        }
        newList.add(players.get(0));

        players = newList;

    }


    /**
     * checks that teams are complete when starting game and sets random teams if some players
     * aren`t part of a team
     */
    private void checkforTeams() {
        boolean teamsIncomplete = false;
        for (Player player : players) {
            if (player.getTeamID() == -1) {
                teamsIncomplete = true;
                break;
            }
        }

        if (teamsIncomplete) {
            changeTeam("2 " + numberParticipants + participants);
        }
    }

    public String getParticipants() {

        String participants = "";

        for (int i = 0; i < players.size() - 1; i++) {
            participants += players.get(i).getPlayerName() + " ";
        }
        participants += players.get(players.size() - 1).getPlayerName();
        logger.debug("Participants: " + participants);

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
        getPlayer(host).sendMessageToClient("STAR");
    }

    public int getNumberOfParticipants() {
        return numberParticipants;
    }

    /**
     * starts game
     */
    public void start() {
        pendent = false;
        mainGame = new MainGame(this);
        Server.getInstance().startGame(mainGame);
    }

    /**
     * @return MainGame file, which only exists if game started
     */
    public MainGame getMainGame() {
        return mainGame;
    }


    /**
     * deletes this game, because it ended or because it is deleted by the host or a player did quit
     * the game while playing
     */
    public void cancel() {

        for (Player player : players) {
            player.getServerConnection().getMessageHandlerServer().returnToLobby();
        }
        if (pendent) {
            sendMessageToAll("DOGA " + getSendReady());
        } else {
            System.out.println("INFO game finished");
        }
        Server.getInstance().removeGame(this);
    }

    /**
     * message to every active player in lobby, separate lobby or games
     * @param message
     */
    private void sendMessageToAll(String message) {
        for (ServerConnection serverConnection1 : Server.getInstance().getBasicConnections()) {
            serverConnection1.getSender().sendStringToClient(message);
        }
    }

    /**
     * @return true if int teamMode is 1
     */
    public boolean isTeamMode() {
        return teamMode == 1;
    }

    /**
     * @return true if the game is not started but an "open game file"
     */
    public boolean isPendent() {
        return pendent;
    }

    /**
     * @return Array of the player objects
     */
    public Player[] getPlayersArray() {
        Player[] array = new Player[players.size()];
        int count = 0;
        for (Player player : players) {
            array[count] = player;
            count++;
        }
        return array;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

}
