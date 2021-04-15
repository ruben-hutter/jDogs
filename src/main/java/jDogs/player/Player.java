package jDogs.player;

import jDogs.Alliance_4;
import jDogs.board.Board;
import jDogs.board.Tile;
import jDogs.serverclient.serverside.ServerConnection;
import java.util.ArrayList;

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

    /**
     * Player instance for server
     * @param playerName name of player
     * @param alliance4 alliance of player
     */
    public Player(String playerName, Alliance_4 alliance4, ServerConnection serverConnection) {
        this.playerName = playerName;
        this.alliance4 = alliance4;
        startingPosition = alliance4.getStartingPosition();
        pieces = createPieces(startingPosition);
        for (Piece piece : pieces) {
            piece.setPositionServer("A" + (piece.getPieceID() - 1));
        }
        this.serverConnection = serverConnection;
    }

    /**
     * Player instance for client
     * @param playerName name of player
     * @param alliance4 alliance of player
     * @param board created board for client
     */
    public Player(String playerName, Alliance_4 alliance4, Board board) {
        this.playerName = playerName;
        this.alliance4 = alliance4;
        this.board = board;
        startingPosition = alliance4.getStartingPosition();
        pieces = createPieces(startingPosition);
        setPiecesOnHome();
    }

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
            pieces[i].setPositionClient(board.allHomeTiles.get(alliance4)[i]);
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

    public boolean reciveHasMoved(int pieceID) {
        return getPiece(pieceID).getHasMoved();
    }

    /**
     * Get the piece you want, giving the id.
     * @param pieceID a number between 1-4
     * @return Piece with fitting pieceID
     */
    private Piece getPiece(int pieceID) {
        for (Piece piece : pieces) {
            if (piece.getPieceID() == pieceID) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Change the actual position of a piece on client side
     * @param pieceID which of the 4 pieces
     * @param newPosition the new position tile
     */
    public void changePositionClient(int pieceID, Tile newPosition) {
        Piece pieceToMove = getPiece(pieceID);
        assert pieceToMove != null;
        Tile oldPosition = pieceToMove.getPositionClient();
        pieceToMove.setPositionClient(newPosition);
        oldPosition.setPiece(null);
        newPosition.setPiece(pieceToMove);
    }

    /**
     * Change the actual position of a piece on server side
     * @param pieceID which of the 4 pieces
     * @param newPosition the new position
     */
    public void changePositionServer(int pieceID, String newPosition) {
        Piece pieceToMove = getPiece(pieceID);
        assert pieceToMove != null;
        pieceToMove.setPositionServer(newPosition);
    }

    public String recivePosition1Server(int pieceID) {
        return pieces[pieceID - 1].getPosition1Server();
    }

    public int recivePosition2Server(int pieceID) {
        return pieces[pieceID - 1].getPosition2Server();
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
}