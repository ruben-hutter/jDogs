package jDogs.serverclient.serverside;

import jDogs.player.Player;

public class VictoryCheck {

    /**
     * Checks if a player won.
     * @return the winning player or null
     */
    protected static Player checkSingleVictory(GameState gameState) {
        for (Player player : gameState.getPlayersState()) {
            player.checkFinished();
            if (player.getFinished()) {
                return player;
            }
        }
        return null;
    }

    /**
     * Checks if a team won.
     * @return teamID of winners or -1
     */
    protected static int checkTeamVictory(GameState gameState) {
        int countTeam0 = 0;
        int countTeam1 = 0;
        for (Player player : gameState.getPlayersState()) {
            player.checkFinished();
            if (player.getFinished()) {
                if (player.getTeamID() == 0) {
                    countTeam0++;
                } else if (player.getTeamID() == 1) {
                    countTeam1++;
                }
            }
        }
        if (countTeam0 == 2) {
            return 0;
        } else if (countTeam1 == 2) {
            return 1;
        } else {
            return -1;
        }
    }
}
