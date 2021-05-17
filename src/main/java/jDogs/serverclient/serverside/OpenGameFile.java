package jDogs.serverclient.serverside;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jDogs.player.Player;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.A;


/**
 * an object of this class is instantiated when a client
 * wants to create a new game in the lobby, after enough
 * people joined the object of this class
 * is transferred to main game
 */
public class OpenGameFile {

    private final String nameId;
    private String host;
    private int numberParticipants;
    private final int total = 4;
    private final int teamMode;
    private ArrayList<Player> players = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(OpenGameFile.class);
    private int teamIDs;

    /**
     * construct an object of OpenGameFile
     * @param nameId name given to the game lobby
     * @param host the player that created the game lobby
     * @param teamMode 1 if teamMode, 0 if not
     * @param serverConnection the object that handles connection to server
     */
    public OpenGameFile(String nameId, String host, int teamMode,
            ServerConnection serverConnection) {
        this.nameId = nameId;
        this.host = host;
        this.numberParticipants = 1;
        this.teamMode = teamMode;
        setUpTeamMode();
        players.add(new Player(host, serverConnection));
    }

    /**
     * sets up team mode by instantiating
     * the variable "teamIDs"
     */
    private void setUpTeamMode() {
        this.teamIDs = 2;
    }

    /**
     * @param combination is a string with the team size, number of names transmitted and with the
     *                    names which should be together in a team
     */
    public boolean changeTeam(String combination) {
        System.out.println("combo " + combination);
        //e.g. format of combination: "4 Gregor Ruben Johanna Joe"
        // 4 names to parse(4)
        //Gregor - Johanna vs Ruben - Joe
        int sizeNames = combination.charAt(0) - 48;

        if (4 == numberParticipants) {
            System.out.println("1");
            String[] array = parseNames(sizeNames, combination.substring(2));
            int teamID0 = 0;
            int teamID1 = 1;
            for (String name: array) {
                System.out.println("name " + name);
            }
            int count = 0;
            ArrayList<Player> newPlayersArray = new ArrayList<>();
            System.out.println("2");

            getPlayer(array[count]).setTeamID(teamID0);
            newPlayersArray.add(getPlayer(array[count]));
            System.out.println("player " + count + " name " + array[count]);
            count++;
            getPlayer(array[count]).setTeamID(teamID1);
            newPlayersArray.add(getPlayer(array[count]));
            System.out.println("player " + count + " name " + array[count]);
            count++;
            getPlayer(array[count]).setTeamID(teamID0);
            newPlayersArray.add(getPlayer(array[count]));
            System.out.println("player " + count + " name " + array[count]);
            count++;
            getPlayer(array[count]).setTeamID(teamID1);
            newPlayersArray.add(getPlayer(array[count]));
            System.out.println("player " + count + " name " + array[count]);
            System.out.println("players.length " + players.size());
            System.out.println("newPlARR " + newPlayersArray.size());
            players = newPlayersArray;
            System.out.println("get Participants " + getParticipants());
            sendMessageToParticipants("TEAM 4 " + getParticipants());
            return true;
        } else {
            // do nothing
            System.out.println(numberParticipants);
            System.out.println(sizeNames);
            System.out.println("combo " + combination);
            System.out.println("numPart and size names doesnt match");
            return false;
        }
    }

    /**
     * orders the players arraylist by the the teamID
     * so that afterwards it can be sorted in the right
     * way to play the game
     */
    private void orderByTeamId() {
        players.sort(Player.TeamIdComparator);

        System.out.println("NEW TEAM combination " + getParticipants());
    }

    /**
     * returns a player if his name is
     * in array of players
     * @param name the name of a player
     * @return a player or null
     */
    public Player getPlayer(String name) {
        for (Player player : players) {
            if (player.getPlayerName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * parses the names it receives
     * @param size of the array the method should create
     * @param mess the names etc. it should parse
     * @return a string array
     */
    private String[] parseNames(int size, String mess) {
        String[] array = new String[size];
        int position = 0;
        int i = 0;
        int count = 0;
        while (count < size - 1) {
            if (Character.isWhitespace(mess.charAt(i))) {
                array[count] = mess.substring(position, i);
                position = i + 1;
                count++;
            }
            i++;
        }
        array[count] = mess.substring(position);
        return array;
    }

    /**
     * sends message only to participants of this game
     * @param message for clients of this game
     */
    public void sendMessageToParticipants(String message) {
        logger.debug("message for players of " + nameId + " : " + message);
        for (Player player : players) {
            player.getServerConnection().sendToClient(message);
        }
    }

    /**
     * add a participant to the open game
     * @param serverConnection the object that handles the connection to the server
     */
    public synchronized void addParticipant(ServerConnection serverConnection) {
        System.out.println("ADDED  sc " + serverConnection.getNickname());
        if (numberParticipants < total) {
            players.add(new Player(serverConnection.getNickname(), serverConnection));
            numberParticipants++;

            serverConnection.sendToClient("JOIN " + getSendReady());
            serverConnection.sendToClient("PLAR " + numberParticipants + " " + getParticipants());
            sendMessageToParticipants("PLYR " + serverConnection.getNickname());
            for (int i = 0; i < numberParticipants - 1; i++) {
                players.get(i).sendMessageToClient("PLYR " + serverConnection.getNickname());
            }
            if (teamMode == 1 && readyToStart()) {
                checkForTeams();
                // get players arraylist in definitive order
            }
        } else {
            serverConnection.sendToClient("INFO no more players allowed in game");
        }
    }

    /**
     * @param nickname is the participant which should be removed attention: it doesn`t work
     *                         for the host the host should be removed by deleting the whole file
     */
    public void removeParticipant(String nickname) {
        Player player = getPlayer(nickname);
        if (!players.remove(player)) {
            System.out.println("couldn t remove player");
        } else {
            numberParticipants--;
            sendMessageToParticipants("DPLR " + nickname);
            // cancel start
            if (numberParticipants == 3) {
                getPlayer(host).sendMessageToClient("DSTR");
            }
            Server.getInstance().sendMessageToPublic("OGAM " + getSendReady(), 0);
        }
    }

    /**
     * this method orders the arrayList of players so that team members don`t 'sit' next to each
     * other
     */
    private void OrderArrayListToPlayGame() {
        ArrayList<Player> newList = new ArrayList<>();
        int counter = players.get(0).getTeamID();
        int teamSize = 2;
        int sizeAllEntries = players.size();
        while (newList.size() < sizeAllEntries - 1) {
            for (int j = 0; j < players.size() && j < teamSize; j++) {
                if (players.get(j).getTeamID() == counter) {
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
     * aren't part of a team
     */
    private void checkForTeams() {
        boolean teamsIncomplete = false;
        for (Player player : players) {
            if (player.getTeamID() == -1) {
                teamsIncomplete = true;
                break;
            }
        }
        if (teamsIncomplete) {
            System.out.println("numberParticipants " + numberParticipants);
            changeTeam(numberParticipants + " " + getParticipants());
        }
    }

    /**
     * this method returns a string with no whitespace at the end
     * @return String of participants
     */
    public String getParticipants() {
        StringBuilder particpants = new StringBuilder();
        for (Player player : players) {
            particpants.append(player.getPlayerName());
            particpants.append(" ");
        }
        particpants = new StringBuilder(particpants.substring(0, particpants.length() - 1));

        return particpants.toString();
    }

    /**
     * method to send information to client
     * @return String ready to send to client
     * for some basic information about game
     */
    public String getSendReady() {
        return nameId + " " + host + " " + numberParticipants + " " + total + " " + teamMode;
    }

    /**
     * get name
     * @return name
     */
    public String getNameId() {
        return nameId;
    }

    /**
     * get host of the open game
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * get an array with participants
     * @return all added participants in array
     */
    public String[] getParticipantsArray() {
        String[] array = new String[numberParticipants];
        int count = 0;
        for (Player player : players) {
            array[count] = player.getPlayerName();
            count++;
        }
        return array;
    }

    /**
     * checks if 4 person are added to open game
     * @return true if 4 person are added
     */
    public boolean readyToStart() {
        return total == numberParticipants;
    }

    /**
     * sends a confirmation message to the host
     * that the game can be started now
     */
    public void sendConfirmationMessage() {
        System.out.println("HOST is " + host);
        getPlayer(host).sendMessageToClient("STAR");
    }

    /**
     * get number of added participants
     * @return int of number of participants
     */
    public int getNumberOfParticipants() {
        return numberParticipants;
    }

    /**
     * starts game
     */
    public void start() {
        Server.getInstance().startGame(this);
    }


    /**
     * deletes this game, because it ended or because it is deleted by the host or a player did quit
     * the game while playing
     */
    public void cancel() {
        Server.getInstance().removeOpenGame(this);
    }

    /**
     * @return true if int teamMode is 1
     */
    public boolean isTeamMode() {
        return teamMode == 1;
    }

    /**
     * @return Array of the player objects
     */
    public Player[] getPlayersArray() {
        Player[] array = new Player[players.size()];
        System.out.println("PLAYERS ARRAY size " + players.size());
        int count = 0;
        for (Player player : players) {
            array[count] = player;
            System.out.println("array " + count + " " + array[count]);
            count++;
        }
        return array;
    }

    /**
     * get the array list of players
     * @return array list of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

}
