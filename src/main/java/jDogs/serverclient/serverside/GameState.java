package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Piece;
import jDogs.player.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Saves the state of the game and update it during the game
 */
public class GameState {

    private final Map<String, ArrayList<String>> cards = new HashMap<>();
    private final GameFile gameFile;
    private ArrayList<Piece> piecesOnTrack;
    private final boolean teamMode;

    public GameState(GameFile gameFile) {
        this.gameFile = gameFile;
        this.piecesOnTrack = new ArrayList<>();
        teamMode = gameFile.isTeamMode();
    }

    /**
     * Creates the players for the game
     */
    public void createPlayers() {
        int counter = 0;
        for (Alliance_4 alliance_4 : Alliance_4.values()) {
            gameFile.getPlayers().get(counter).setUpPlayerOnServer(alliance_4);
            cards.put(gameFile.getPlayers().get(counter).getPlayerName(),new ArrayList<>());
            counter++;
        }
    }

    public ArrayList<Piece> getSortedPiecesOnTrack() {
        Collections.sort(piecesOnTrack);
        return piecesOnTrack;
    }

    public boolean isPieceOnTrack(Piece piece) {
        for (Piece p : piecesOnTrack) {
            if (p.getPieceAlliance() == piece.getPieceAlliance()
                    && p.getPieceID() == piece.getPieceID()) {
                return true;
            }
        }
        return false;
    }

    public Map<String, ArrayList<String>> getCards() {
        return cards;
    }

    /**
     * Updates ArrayList when a move is done
     * @param piece piece which changes position
     * @param newPosition1 A, B or C
     */
    public void updatePiecesOnTrack(Piece piece, String newPosition1) {
        if (isPieceOnTrack(piece)) {
            if (!newPosition1.equals("B")) {
                piecesOnTrack.remove(piece);
            }
        } else if (!isPieceOnTrack(piece)) {
            if (newPosition1.equals("B")) {
                piecesOnTrack.add(piece);
            }
        }
        piecesOnTrack = getSortedPiecesOnTrack();
    }

    /**
     * Checks if a given track position is occupied.
     * @param newPosition2 int between 0-63
     * @return a piece, or null if not occupied
     */
    public Piece trackPositionOccupied(int newPosition2) {
        for (Piece p : piecesOnTrack) {
            if (p.getPositionServer2() == newPosition2) {
                return p;
            }
        }
        return null;
    }

    /**
     * Gives a player for a given nickname.
     * @param nickname player's name
     * @return a player or null if it doesn't exist
     */
    public Player getPlayer(String nickname) {
        for (Player player : gameFile.getPlayers()) {
            if (player.getPlayerName().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gives the players of the game.
     * @return an array list with the players
     */
    public ArrayList<Player> getPlayersState() {
        return gameFile.getPlayers();
    }

    /**
     * Checks if there is a winner
     */
    public void checkForVictory() {
        if (teamMode) {
            if (checkTeamVictory() != null) {
                // TODO send winners and terminate game, write stats
                // TODO checkFinished() in Player
            }
        } else {
            if (checkSingleVictory() != null) {
                // TODO send winner and terminate game, write stats
            }
        }
    }

    /**
     * Checks if a player won.
     * @return the winning player or null
     */
    private Player checkSingleVictory() {
        for (Player player : getPlayersState()) {
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
    private Player[] checkTeamVictory() {
        Player[] winningTeam = new Player[2];
        int count = 0;
        for (Player player : getPlayersState()) {
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
