package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Player;
import java.util.ArrayList;

public class GameState {

    String[] playerNames;
    Player[] playersState;
    ArrayList<ServerConnection> serverConnectionArrayList;
    int numPlayers;

    public GameState(GameFile gameFile) {
        this.numPlayers = gameFile.getNumberOfParticipants();
        this.playerNames = gameFile.getParticipantsArray();
        playersState = createPlayers();
        this.serverConnectionArrayList = gameFile.getscArrayList();
    }

    private Player[] createPlayers() {
        Player[] players = new Player[numPlayers];
        int counter = 0;
        for (Alliance_4 alliance_4 : Alliance_4.values()) {
            players[counter] = new Player(playerNames[counter], alliance_4,serverConnectionArrayList.get(counter));
            counter++;
        }
        return players;
    }
}
