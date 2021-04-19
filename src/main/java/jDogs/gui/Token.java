package jDogs.gui;

import jDogs.Alliance_4;

/**
 * this class represents a piece in the gui
 */
public class Token {

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
        this.posType = 'A';
        this.position = -1;
        fieldOnBoard = AdaptToGui.getInstance().getHomeField(alliance4.getStartingPosition(), pieceId);
    }


    public void setNewPosition(int newPosition, char type) {
        this.posType = type;
        this.position = newPosition;

        switch (type) {
            case 'A':
                //get heaven field on board here
                break;

            case 'B':
                // get track field on board here and set it there
                break;

            case 'C':

                break;

        }


    }

}
