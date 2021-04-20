package jDogs.gui;

import jDogs.Alliance_4;
import javafx.scene.shape.Circle;

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
    private int oldPos;
    private char oldType;
    private FieldOnBoard oldFieldOnBoard;
    private Circle circle;
    private ColorTokens colorToken;

    public Token(Alliance_4 alliance4, String playerName, int pieceId) {
        this.playerName = playerName;
        this.orderNumber = orderNumber;
        this.pieceId = pieceId;
        this.posType = 'A';
        this.position = -1;
        this.fieldOnBoard = AdaptToGui.getInstance().getHomeField(alliance4.getStartingPosition(), pieceId);
        this.circle = new Circle(10, colorToken.getColor());
    }



    public void setNewPosition(int newPosition, char type) {
        this.oldPos = this.position;
        this.oldType = this.posType;
        this.oldFieldOnBoard = fieldOnBoard;
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

    public FieldOnBoard getOldField() {
        return oldFieldOnBoard;
    }

    public FieldOnBoard getNewField() {
        return fieldOnBoard;
    }
}
