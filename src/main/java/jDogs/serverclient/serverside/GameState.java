package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Player;
import java.util.ArrayList;

public class GameState {

    private final GameFile gameFile;
    String[] playerNames;
    Player[] playersState;
    int numPlayers;

    public GameState(GameFile gameFile) {
        this.gameFile = gameFile;
        this.numPlayers = gameFile.getNumberOfParticipants();
        this.playerNames = gameFile.getParticipantsArray();

        playersState = createPlayers();
    }

    private Player[] createPlayers() {
        Player[] players = new Player[numPlayers];
        int counter = 0;
        for (Alliance_4 alliance_4 : Alliance_4.values()) {
            players[counter] = gameFile.getPlayersArray()[counter];
            players[counter].setUpPlayerOnServer(alliance_4);
            counter++;
        }
        return players;
    }

    public Player getPlayer(String nickname) {
        for (Player player : playersState) {
            if (player.getPlayerName().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    public void sendMessageToPlayers(String message) {
        for (Player player : playersState) {
            player.getServerConnection().getSender().sendStringToClient(message);
        }
    }
}
