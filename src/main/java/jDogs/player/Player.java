package jDogs.player;

import jDogs.Alliance_4;
import jDogs.board.Board;
import jDogs.board.Tile;
import jDogs.serverclient.serverside.ServerConnection;
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
    private ServerConnection serverConnection;
    private int teamID;
    private boolean allowedToPlay;
    private boolean finished;

    /**
     * Player instance for server
     * @param playerName name of player
     * @param serverConnection main server instance
     */
    public Player(String playerName, ServerConnection serverConnection) {
        this.playerName = playerName;
        this.serverConnection = serverConnection;
        allowedToPlay = false;
        teamID = -1;
        finished = false;
    }

    /**
     * Player instance for server, only  for test purposes
     * same as {@link #Player(String, ServerConnection)} but without ServerConnection
     * @param playerName name of player
     */
    public Player(String playerName) {
        this.playerName = playerName;
        allowedToPlay = false;
        teamID = -1;
        finished = false;
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
     * Sets a new player for the server side
     * @param alliance4 the alliance for the new player
     */
    public void setUpPlayerOnServer(Alliance_4 alliance4) {
        this.alliance4 = alliance4;
        startingPosition = alliance4.getStartingPosition();
        pieces = createPieces(startingPosition);
        for (Piece piece : pieces) {
            piece.setPositionServer("A", piece.getPieceID() - 1);
        }
    }

    /**
     * Checks if two players are in the same team
     * if playing in teamMode.
     */
    public static Comparator<Player> TeamIdComparator = (player1, player2) -> {
        int teamIdPlayer1 = player1.getTeamID();
        int teamIdPlayer2 = player2.getTeamID();
        return teamIdPlayer1 - teamIdPlayer2;
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
            pieces[i].setPositionClient(board.allHomeTiles.get(alliance4)[i]);
        }
    }

    /**
     * Gets the nickname of the player.
     * @return a String
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the piece alliance
     * @return YELLOW, GREEN, BLUE or RED
     */
    public Alliance_4 getAlliance() {
        return alliance4;
    }

    /**
     * Gives if a piece as been moved size it
     * was placed on the track.
     * @param pieceID num between 1-4
     * @return true if it has moved
     */
    public boolean receiveHasMoved(int pieceID) {
        return getPiece(pieceID).getHasMoved();
    }

    /**
     * Gets starting position (saved in alliance)
     * @return 0, 16, 32 or 48
     */
    public int getStartingPosition() {
        return startingPosition;
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
     * Change the actual position of a piece on client side
     * @param pieceID which of the 4 pieces
     * @param newPosition the new position tile
     */
    public void changePositionClient(int pieceID, Tile newPosition) {
        Piece pieceToMove = getPiece(pieceID);
        assert pieceToMove != null;
        Tile oldPosition = pieceToMove.getPositionClient();
        pieceToMove.setPositionClient(newPosition);
        // checks if the oldTile is pointing to this piece
        if (oldPosition.getPiece() == pieceToMove) {
            oldPosition.setPiece(null);
        }
        newPosition.setPiece(pieceToMove);
    }

    /**
     * Change the actual position of a piece on server side
     * @param pieceID which of the 4 pieces
     * @param newPosition1 A = home, B = track, C = heaven
     * @param newPosition2 0 up to max num of tiles for newPos 1
     */
    public void changePositionServer(int pieceID, String newPosition1, int newPosition2) {
        Piece pieceToMove = getPiece(pieceID);
        assert pieceToMove != null;
        pieceToMove.setPositionServer(newPosition1, newPosition2);
    }

    /**
     * Gives the first part of the position
     * for the server side
     * @param pieceID int between 1-4
     * @return A, B or C
     */
    public String receivePosition1Server(int pieceID) {
        return pieces[pieceID - 1].getPositionServer1();
    }

    /**
     * Gives the second part of the position
     * for the server side
     * @param pieceID int between 1-4
     * @return int between 0-3 or 0-63
     */
    public int receivePosition2Server(int pieceID) {
        return pieces[pieceID - 1].getPositionServer2();
    }

    /**
     * Sets on the player the teamID if
     * player mode is teamMode
     * @param newID 0 or 1
     */
    public void setTeamID(int newID) {
        teamID = newID;
    }

    /**
     * Returns the teamID
     * @return 0 or 1
     */
    public int getTeamID() {
        return teamID;
    }

    /**
     *
     * @param message to the client concerning this game
     */
    public void sendMessageToClient(String message) {
        this.serverConnection.sendToClient(message);
    }

    /**
     * Returns the connection instance to the server
     * @return a ServerConnection
     */
    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    /**
     * Excludes player from game till this round finished
     */
    public void excludeForRound() {
        this.allowedToPlay = false;
    }

    /**
     * Gives back if the player has al the pieces in heaven.
     * @return true if finished, false if not
     */
    public boolean getFinished() {
        return finished;
    }

    /**
     * Checks if all the pieces are in heaven
     * and if so, change finished state to true.
     */
    public void checkFinished() {
        int count = 0;
        for (Piece piece : pieces) {
            if (piece.getPositionServer1().equals("C")) {
                count++;
            }
        }
        if (count == 4) {
            finished = true;
        }
    }

    /**
     * Returns if the player is or not allowed to play
     * @return true if allowed
     */
    public boolean isAllowedToPlay() {
        return allowedToPlay;
    }

    /**
     * Changes the allowedToPlay value to the given one.
     * @param val true or false
     */
    public void setAllowedToPlay(boolean val) {
        this.allowedToPlay = val;
    }

    @Override
    public String toString() {
        return getPlayerName() + ": " + getAlliance();
    }
}