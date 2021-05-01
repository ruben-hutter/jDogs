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

    // TODO check if player has 4 in heaven, end game
    public boolean checkGameFinished() {
        int counter = 0;
        if (teamMode) {
            // team mode
            for (Player player : getPlayersState()) {
                for (Piece piece : player.pieces) {
                    if (piece.getPositionServer1().equals("C")) {
                        counter++;
                    }
                    //if counter
                }
            }
        } else {
            // single player
        }
        return false;
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

    public Player getPlayer(String nickname) {
        for (Player player : gameFile.getPlayers()) {
            if (player.getPlayerName().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    public ArrayList<Player> getPlayersState() {
        return gameFile.getPlayers();
    }
}
