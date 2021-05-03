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

    private Map<String, ArrayList<String>> cards;
    private ArrayList<Piece> piecesOnTrack;
    private final boolean teamMode;
    private int numPlayers;
    private MainGame mainGame;

    public GameState(MainGame mainGame) {
        this.mainGame = mainGame;
        this.numPlayers = mainGame.getPlayersArray().length;
        this.piecesOnTrack = new ArrayList<>();
        teamMode = mainGame.isTeamMode();
        this.cards = new HashMap<>();
    }

    /**
     * Creates the players for the game
     */
    public void createPlayers() {
        int counter = 0;
        for (Alliance_4 alliance_4 : Alliance_4.values()) {
            mainGame.getPlayersArray()[counter].setUpPlayerOnServer(alliance_4);
            cards.put(mainGame.getPlayersArray()[counter].getPlayerName(), new ArrayList<>());
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
     *
     * @param piece        piece which changes position
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
     *
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
     *
     * @param nickname player's name
     * @return a player or null if it doesn't exist
     */
    public Player getPlayer(String nickname) {
        for (Player player : getPlayers()) {
            if (player.getPlayerName().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    public Player[] getPlayers() {
        return mainGame.getPlayersArray();
    }

    /**
     * Gives the players of the game.
     * @return an list with the players
     */
    public Player[] getPlayersState () {
        return mainGame.getPlayersArray();
    }

    /**
     * Gives the mode of the game
     * @return true if team mode, false if not
     */
    public boolean isTeamMode () {
        return teamMode;
    }

    /**
     * Checks if there is a winner
     */
    public void checkForVictory () {
        if (teamMode) {
            int winningTeam = VictoryCheck.checkTeamVictory(this);
            if (winningTeam > -1) {
                // TODO send winners and terminate game, write stats
                mainGame.sendMessageToParticipants("VICT " + winningTeam);
            }
        } else {
            Player winner = VictoryCheck.checkSingleVictory(this);
            if (winner != null) {
                // TODO send winner and terminate game, write stats
                mainGame.sendMessageToParticipants("VICT " + winner.getPlayerName());
            }
        }
    }
}
