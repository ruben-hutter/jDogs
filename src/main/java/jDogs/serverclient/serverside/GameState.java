package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Player;

public class GameState {

    String[] playerNames;
    Player[] playersState;
    int numPlayers;

    public GameState(GameFile gameFile) {
        this.numPlayers = gameFile.getNumberOfParticipants();
        this.playerNames = gameFile.getParticipantsArray();
        playersState = createPlayers();
    }

    private Player[] createPlayers() {
        Player[] players = new Player[numPlayers];
        int counter = 0;
        for (Alliance_4 alliance_4 : Alliance_4.values()) {
            players[counter] = new Player(playerNames[counter], alliance_4);
            counter++;
        }
        return players;
    }
}
