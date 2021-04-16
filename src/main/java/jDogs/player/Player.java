package jDogs.player;

import jDogs.Alliance_4;
import jDogs.Piece;
import jDogs.board.Board;
import jDogs.board.Tile;
import jDogs.serverclient.serverside.ServerConnection;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class defines a player
 * Giving a name and an alliance it initializes
 * the pieces for the player and sets them on the home tiles.
 */
public class Player {

    public Piece[] pieces;
    String playerName;
    Alliance_4 alliance4;
    Board board;
    public int startingPosition;
    private ArrayList<String> deck;
    private ServerConnection serverConnection;
    private int teamID;

    public Player(String playerName, ServerConnection serverConnection) {
        this.playerName = playerName;
        this.serverConnection = serverConnection;
        teamID = -1;


    }

    public Player(String playerName, Alliance_4 alliance4, Board board) {
        this.playerName = playerName;
        this.alliance4 = alliance4;
        this.board = board;
        startingPosition = alliance4.getStartingPosition();
        pieces = createPieces(startingPosition);
        setPiecesOnHome();
    }

    public void setUpPlayerOnServer( Alliance_4 alliance4) {
        this.alliance4 = alliance4;
        startingPosition = alliance4.getStartingPosition();
        pieces = createPieces(startingPosition);
        this.deck = null;
    }

    public static Comparator<Player> TeamIdComparator = new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            int teamIdPlayer1 = player1.getTeamID();
            int teamIdPlayer2 = player2.getTeamID();
            return teamIdPlayer1 - teamIdPlayer2;
        }
    };

    /**
     * Creates 4 pieces for a player
     * @return an array with the 4 pieces
     */
    private Piece[] createPieces(int startingPosition) {
        Piece[] pieces = new Piece[Board.NUM_HOME_TILES];
        for (int i = 0; i < pieces.length; i++) {
            pieces[i] = new Piece(alliance4, startingPosition,i + 1);
        }
        return pieces;
    }

    /**
     * Put pieces on their home.
     */
    private void setPiecesOnHome() {
        for (int i = 0; i < pieces.length; i++) {
            board.allHomeTiles.get(alliance4)[i].setPiece(pieces[i]);
            pieces[i].setPosition(board.allHomeTiles.get(alliance4)[i]);
        }
    }

    public void setDeck (ArrayList<String> deck) {
        this.deck = deck;
    }

    public ArrayList<String> getDeck() {
        return deck;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Alliance_4 getAlliance() {
        return alliance4;
    }

    /**
     * Get the piece you want, giving the id.
     * @param pieceID a number between 1-4
     * @return Piece with fitting pieceID
     */
    public Piece getPiece(int pieceID) {
        for (Piece piece : pieces) {
            if (piece.getPieceID() == pieceID) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Change the actual position of a piece to a new one
     * @param pieceID which of the 4 pieces
     * @param newPosition the new position tile
     */
    public void changePiecePosition(int pieceID, Tile newPosition) {
        Piece pieceToMove = getPiece(pieceID);
        Tile oldPosition = pieceToMove.getPosition();
        pieceToMove.setPosition(newPosition);
        oldPosition.setPiece(null);
        newPosition.setPiece(pieceToMove);
    }

    public void setTeamID(int newID) {

        teamID = newID;
    }

    public int getTeamID() {
        return teamID;
    }


    /**
     *
     * @param message to the client concerning this game
     */
    public void sendMessageToClient(String message) {
        serverConnection.getSender().sendStringToClient(message);
    }

    @Override
    public String toString() {
        return getPlayerName() + ": " + getAlliance();
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}