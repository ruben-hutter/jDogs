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

    private Map<String, ArrayList<String>> cards = new HashMap<>();
    private final GameFile gameFile;
    private ArrayList<Piece> piecesOnTrack;
    int numPlayers;

    public GameState(GameFile gameFile) {
        this.gameFile = gameFile;
        this.numPlayers = gameFile.getNumberOfParticipants();
        this.piecesOnTrack = new ArrayList<>();
    }

    // TODO check if player has 4 in heaven, end game

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
                if (newPosition1.equals("A")) {
                    piece.changeHasMoved();
                }
            }
        } else if (!isPieceOnTrack(piece)) {
            if (newPosition1.equals("B")) {
                piecesOnTrack.add(piece);
            }
        }
        piecesOnTrack = getSortedPiecesOnTrack();
    }

    public Piece newPositionOccupied(Player player, String newPosition1, int newPosition2) {
        Piece otherPiece = null;
        if (newPosition1.equals("C")) {
            otherPiece = newPositionOccupiedHelper(player, newPosition1, newPosition2);
        } else if (newPosition1.equals("B")) {
            for (Piece p : piecesOnTrack) {
                if (p.getPositionServer1().equals(newPosition1)
                        && p.getPositionServer2() == newPosition2) {
                    otherPiece = p;
                }
            }
        }
        return otherPiece;
    }

    private Piece newPositionOccupiedHelper(Player player, String newPosition1, int newPosition2) {
        Piece otherPiece = null;
        for (Player pl : gameFile.getPlayers()) {
            if (pl.equals(player)) {
                for (Piece p : player.pieces) {
                    if (p.getPositionServer1().equals(newPosition1)
                            && p.getPositionServer2() == newPosition2) {
                        otherPiece = p;
                    }
                }
            }
            break;
        }
        return otherPiece;
    }

    private static boolean isInTest(ArrayList<Piece> test, Piece piece) {
        for (Piece p : test) {
            if (p.getPieceAlliance() == piece.getPieceAlliance()
                    && p.getPieceID() == piece.getPieceID()) {
                return true;
            }
        }
        return false;
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
