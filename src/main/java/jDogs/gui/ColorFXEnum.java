package jDogs.gui;

import jDogs.Alliance_4;
import javafx.scene.paint.Color;

/**
 * translate Alliance to color in Gui
 */
public enum ColorFXEnum {

    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    RED(Color.RED);

    Color color;

    ColorFXEnum(Color color) { this.color = color;}

    public Color getColor() {
        return color;
    }

    public Color getAllianceColor(Alliance_4 alliance4) {
        switch (alliance4) {

            case YELLOW:
                return Color.YELLOW;

            case GREEN:
                return Color.GREEN;

            case BLUE:
                return Color.BLUE;

            case RED:
                return Color.RED;
        }
        return null;
    }
}
