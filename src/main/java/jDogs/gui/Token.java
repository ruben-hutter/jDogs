package jDogs.gui;

import jDogs.Alliance_4;

/**
 * this class represents a piece in the gui
 */
public class Token {

    private final int pieceId;
    private char posType;
    private Alliance_4 alliance4;
   private String playerName;
   private int orderNumber;
   private int position;
   private FieldOnBoard fieldOnBoard;

    public Token(Alliance_4 alliance4, String playerName, int pieceId) {
        this.alliance4 = alliance4;
        this.playerName = playerName;
        this.orderNumber = orderNumber;
        this.pieceId = pieceId;
        this.posType = 'A';
        this.position = -1;
        this.fieldOnBoard = AdaptToGui.getInstance().getHomeField(alliance4.getStartingPosition(), pieceId);
    }


    public void setNewPosition(int newPosition, char type) {
        this.posType = type;
        this.position = newPosition;

        switch (type) {
            case 'A':
                //get heaven field on board here
                this.fieldOnBoard = AdaptToGui.getInstance().getHomeField(alliance4.getStartingPosition(), pieceId);
                this.position = -1;
                break;

            case 'B':
                // get track field on board here and set it there
                this.fieldOnBoard = AdaptToGui.getInstance().getTrack(newPosition);
                break;

            case 'C':
                this.fieldOnBoard = AdaptToGui.getInstance().getHeavenField(alliance4.getStartingPosition()/16)[newPosition];
                break;

        }

    }

}
