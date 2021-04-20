package jDogs.gui;

import javafx.scene.paint.Color;

/**
 * translate Alliance to color in Gui
 */
public enum ColorTokens {

    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    RED(Color.RED);

    Color color;

    ColorTokens(Color color) { this.color = color;}

    public Color getColor() {
        return color;
    }
}
