package jDogs.serverclient.serverside;

import jDogs.player.Player;

/**
 * Saves all the infos needed to check a move in RulesCheck
 */
public class PlayersActualInfo {

    private final Player player;
    private final String actualPosition1;
    private final int actualPosition2;
    private final boolean hasMoved;
    private final int startingPosition;

    PlayersActualInfo(Player player, String actualPosition1, int actualPosition2, boolean hasMoved,
            int startingPosition) {
        this.player = player;
        this.actualPosition1 = actualPosition1;
        this.actualPosition2 = actualPosition2;
        this.hasMoved = hasMoved;
        this.startingPosition = startingPosition;
    }

    /**
     * Gives back the player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gives the actual position
     * @return A, B or C
     */
    public String getActualPosition1() {
        return actualPosition1;
    }

    /**
     * Gives the actual position
     * @return 0-63
     */
    public int getActualPosition2() {
        return actualPosition2;
    }

    /**
     * Gives if player has moved
     * @return true if has moved, false if not
     */
    public boolean getHasMoved() {
        return hasMoved;
    }

    /**
     * Gives the starting position of a Player
     * @return 0, 16, 32 or 48
     */
    public int getStartingPosition() {
        return startingPosition;
    }
}
