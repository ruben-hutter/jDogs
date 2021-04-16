package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Player;
import java.util.ArrayList;

public class GameState {

    private final GameFile gameFile;
    private String[] playerNames;
   private Player[] playersState;
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
        players = gameFile.getPlayersArray();
        for (Alliance_4 alliance_4 : Alliance_4.values()) {
            System.out.println("players " + players[counter].getPlayerName());
            players[counter].setUpPlayerOnServer(alliance_4);
            counter++;
        }
        System.out.println("all players set up");
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

    public Player[] getPlayersState() {
        return playersState;
    }

    public void sendMessageToPlayers(String message) {
        for (Player player : playersState) {
            player.getServerConnection().getSender().sendStringToClient(message);
        }
    }
}
