package jDogs.game;

//this will check if moves correspond to rules

import jDogs.board.HeavenTiles;
import jDogs.board.HomeTiles;
import jDogs.board.Tile;
import jDogs.board.TrackTiles;

public class GameRules {

    GameRules(TrackTiles trackTiles, HeavenTiles heavenTiles, HomeTiles homeTiles, PlayerRegister playerRegister) {

        this.trackTiles = trackTiles;
        this.heavenTiles
        this.homeTiles
        this.playerRegister

    }

    public static boolean checkSimpleMove(Piece piece, Tile origin, Tile destination, Card card) {

        if (validRequest(piece, origin, card)) {
            if (validCardMove(origin, destination, card)) {
                addAdditionalActions();
                return true;
                }
            }
        return false;
    }

    private static boolean validCardMove(Tile origin, Tile destination, Card card) {
        //check if move is possible
        //maybe: get consequences for others

        return false;
    }

    private static boolean validRequest(Piece piece, Tile origin, Card card) {
        //check if piece exists

        // check if piece is on position

        // check if card is in players Deck

        return false;
    }
}
