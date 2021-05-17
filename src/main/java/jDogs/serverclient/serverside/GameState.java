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

    private final Map<String, ArrayList<String>> cards;
    private ArrayList<Piece> piecesOnTrack;
    private final boolean teamMode;
    private final MainGame mainGame;
    private String winners;

    /**
     * constructs an object of game state
     * @param mainGame gameObject
     */
    public GameState(MainGame mainGame) {
        this.mainGame = mainGame;
        piecesOnTrack = new ArrayList<>();
        teamMode = mainGame.isTeamMode();
        cards = new HashMap<>();
    }

    /**
     * Copy constructor used to check move SEVE
     * @param gameState the actual gameState
     */
    public GameState(GameState gameState) {
        cards = gameState.getCards();
        piecesOnTrack = new ArrayList<>();
        teamMode = gameState.getTeamMode();
        mainGame = new MainGame(gameState.getMainGame(), this);
        winners = gameState.getWinners();
        copyPiecesActualInformation(gameState.getMainGame());
    }

    /**
     * Makes a copy of the pieces info of every player
     * @param actualMainGame the mainGame for the, in the constructor, given gameState
     */
    private void copyPiecesActualInformation(MainGame actualMainGame) {
        for (Player player : actualMainGame.getPlayersArray()) {
            Player playerToSetUp = mainGame.getPlayer(player.getPlayerName());
            playerToSetUp.setUpPlayerOnServerCopy(player.getAlliance(), player.getTeamID());
            for (int i = 0; i < playerToSetUp.pieces.length; i++) {
                // sets the correct position
                playerToSetUp.pieces[i].setPositionServer(player.pieces[i].getPositionServer1(),
                        player.pieces[i].getPositionServer2());
                // sets the correct hasMoved value
                if (player.pieces[i].getHasMoved()) {
                    playerToSetUp.pieces[i].changeHasMoved();
                }
                // adds the piece to track if actualPosition1 is B
                updatePiecesOnTrack(playerToSetUp.pieces[i],
                        playerToSetUp.pieces[i].getPositionServer1());
            }
        }
    }

    /**
     * Creates the players for the game
     */
    public void createPlayers() {
        int counter = 0;
        for (Alliance_4 alliance4 : Alliance_4.values()) {
            mainGame.getPlayersArray()[counter].setUpPlayerOnServer(alliance4);
            cards.put(mainGame.getPlayersArray()[counter].getPlayerName(), new ArrayList<>());
            counter++;
        }
    }

    /**
     * Get pieces that are on track in ascending order
     * @return arraylist with the track pieces
     */
    public ArrayList<Piece> getSortedPiecesOnTrack() {
        Collections.sort(piecesOnTrack);
        return piecesOnTrack;
    }

    /**
     * Checks if a piece is on track (on B)
     * @param piece given piece
     * @return true if on track, false if not
     */
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
     * Get team mode
     * @return true if team mode on, false if single player
     */
    public boolean getTeamMode() {
        return teamMode;
    }

    /**
     * Gets the mainGame object
     * @return mainGame object
     */
    public MainGame getMainGame() {
        return mainGame;
    }

    /**
     * Get cards as map of player name and associated cards
     * @return map with the cards
     */
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
        for (Player player : getPlayers()) {
            if (player.getPlayerName().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gives a player for a given alliance4
     * @param alliance4 his alliance
     * @return a player or null
     */
    public Player getPlayer(Alliance_4 alliance4) {
        for (Player player : getPlayers()) {
            if (player.getAlliance() == alliance4) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gets all the players of the game
     * @return an array with the players
     */
    public Player[] getPlayers() {
        return mainGame.getPlayersArray();
    }

    /**
     * Checks if there is a winner
     * @return true if the game's finished
     */
    public boolean checkForVictory () {
        StringBuilder winners = new StringBuilder();
        if (teamMode) {
            int winningTeam = VictoryCheck.checkTeamVictory(this);
            if (winningTeam > -1) {
                for (Player player : getPlayers()) {
                    if (player.getTeamID() == winningTeam) {
                        winners.append(player.getPlayerName()).append(" ");
                    }
                }
                winners.deleteCharAt(winners.length() - 1);
            }
        } else {
            Player winner = VictoryCheck.checkSingleVictory(this);
            if (winner != null) {
                winners.append(winner.getPlayerName());
            }
        }
        this.winners = winners.toString();
        return winners.length() != 0;
    }

    /**
     * Gets the winners of the game
     * @return 1 or 2 names of the winner/s
     */
    public String getWinners() {
        return winners;
    }
}
