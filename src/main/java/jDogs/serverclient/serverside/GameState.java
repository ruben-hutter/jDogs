package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Piece;
import jDogs.player.Player;
import java.util.ArrayList;
import java.util.Collections;

public class GameState {

    String[] playerNames;
    Player[] playersState;
    ArrayList<Piece> piecesOnTrack;
    ArrayList<ServerConnection> serverConnectionArrayList;
    int numPlayers;

    public GameState(GameFile gameFile) {
        this.numPlayers = gameFile.getNumberOfParticipants();
        this.playerNames = gameFile.getParticipantsArray();
        playersState = createPlayers();
        this.serverConnectionArrayList = gameFile.getscArrayList();
        piecesOnTrack = new ArrayList<>();
    }

    private Player[] createPlayers() {
        Player[] players = new Player[numPlayers];
        int counter = 0;
        for (Alliance_4 alliance_4 : Alliance_4.values()) {
            players[counter] = new Player(playerNames[counter], alliance_4,
                    serverConnectionArrayList.get(counter));
            counter++;
        }
        return players;
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

    /**
     * Updates ArrayList when a move is done
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
        if (newPosition1.equals("A") || newPosition1.equals("C")) {
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
        for (Player pl : playersState) {
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

    public static void main(String[] args) {
        Piece piece1 = new Piece(Alliance_4.BLUE, 1 , 1);
        Piece piece2 = new Piece(Alliance_4.YELLOW, 1 , 1);
        Piece piece3 = new Piece(Alliance_4.GREEN, 1 , 1);
        piece1.setPositionServer("A", 0);
        piece2.setPositionServer("B", 10);
        piece3.setPositionServer("B", 63);
        ArrayList<Piece> test = new ArrayList<>();
        test.add(piece2);
        test.add(piece3);

        String newPosition1 = "B";
        if (isInTest(test, piece1)) {
            if (!newPosition1.equals("B")) {
                test.remove(piece1);
                if (newPosition1.equals("A")) {
                    piece1.changeHasMoved();
                }
            }
        } else if (!isInTest(test, piece1)) {
            if (newPosition1.equals("B")) {
                test.add(piece1);
            }
        }


        Collections.sort(test);
        System.out.println(test);
        System.out.println(piece1.getHasMoved());
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
}
