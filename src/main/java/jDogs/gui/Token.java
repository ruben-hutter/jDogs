package jDogs.gui;

import jDogs.Alliance_4;

/**
 * this class represents a piece in the gui
 */
public class Token {
   private Alliance_4 alliance4;
   private String playerName;
   private int orderNumber;

    public Token(Alliance_4 alliance4, String playerName, int orderNumber) {
        this.alliance4 = alliance4;
        this.playerName = playerName;
        this.orderNumber = orderNumber;
    }


}
