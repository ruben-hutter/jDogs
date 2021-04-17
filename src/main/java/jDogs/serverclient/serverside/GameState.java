package jDogs.serverclient.serverside;

import jDogs.Alliance_4;
import jDogs.player.Piece;
import jDogs.player.Player;
import java.util.ArrayList;
import java.util.Collections;

public class GameState {

    private final GameFile gameFile;
    private String[] playerNames;
    private ArrayList<Piece> piecesOnTrack;
    int numPlayers;

    public GameState(GameFile gameFile) {
        this.gameFile = gameFile;
        this.numPlayers = gameFile.getNumberOfParticipants();
        this.playerNames = gameFile.getParticipantsArray();
        this.piecesOnTrack = new ArrayList<>();
    }

    public void createPlayers() {
        int counter = 0;
        for (Alliance_4 alliance_4 : Alliance_4.values()) {
            gameFile.getPlayers().get(counter).setUpPlayerOnServer(alliance_4);
            System.out.println(gameFile.getPlayers().get(counter).getAlliance());
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
        System.out.println("player " + player.getPlayerName());
        System.out.println("newpos1 " + newPosition1);
        System.out.println("newpos2 " + newPosition2);
        Piece otherPiece = null;
        if (newPosition1.equals("A") || newPosition1.equals("C")) {
            otherPiece = newPositionOccupiedHelper(player, newPosition1, newPosition2);
        } else if (newPosition1.equals("B")) {
            for (Piece p : piecesOnTrack) {
                System.out.println("pieceID on Track " + p.getPieceID());
                if (p.getPositionServer1().equals(newPosition1)
                        && p.getPositionServer2() == newPosition2) {
                    otherPiece = p;
                    System.out.println("otherPieceID " + otherPiece.getPieceID());
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

    public Player getPlayer(String nickname) {
       return gameFile.getPlayer(nickname);
    }

    public ArrayList<Player> getPlayersState() {
        return gameFile.getPlayers();
    }

    public void sendMessageToPlayers(String message) {
        gameFile.sendMessageToParticipants(message);
    }
}

