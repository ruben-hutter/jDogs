package jDogs.serverclient.serverside;

import jDogs.player.Player;

public class VictoryCheck {

    /**
     * Checks if a player won.
     * @return the winning player or null
     */
    protected static Player checkSingleVictory(GameState gameState) {
        for (Player player : gameState.getPlayersState()) {
            if (player.getFinished()) {
                return player;
            }
        }
        return null;
    }

    /**
     * Checks if a team won.
     * @return an array with the winners or null
     */
    protected static Player[] checkTeamVictory(GameState gameState) {
        Player[] winningTeam = new Player[2];
        int count = 0;
        for (Player player : gameState.getPlayersState()) {
            if (player.getFinished()) {
                winningTeam[count++] = player;
            }
        }
        if (winningTeam[1] != null) {
            return winningTeam;
        } else {
            return null;
        }
    }
}
