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

    /**
     * Updates ArrayList when a move is done
     */
    public void updatePiecesOnTrack() {
        // in sorted order int serverPosition2
        // TODO to concatenate with changePositionServer() / setPositionServer()
        piecesOnTrack = getSortedPiecesOnTrack();
    }

    public static void main(String[] args) {
        Piece piece1 = new Piece(Alliance_4.BLUE, 1 , 1);
        Piece piece2 = new Piece(Alliance_4.YELLOW, 1 , 1);
        Piece piece3 = new Piece(Alliance_4.GREEN, 1 , 1);
        piece1.setPositionServer("B00");
        piece2.setPositionServer("B10");
        piece3.setPositionServer("B63");
        ArrayList<Piece> test = new ArrayList<>();
        test.add(piece1);
        test.add(piece2);
        test.add(piece3);

        System.out.println(test);
        Collections.sort(test);
        System.out.println(test);
    }
}
