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

    /**
     * constructor of ColorFXEnum
     * @param color Yellow,Green,Blue,Red
     */
    ColorFXEnum(Color color) { this.color = color;}

    /**
     * get the color
     * @return Yellow,Green,Blue,Red
     */
    public Color getColor() {
        return color;
    }

    /**
     * get the AllianceColor
     * @param alliance4 YELO, GREN, BLUE, REDD
     * @return YELLOW, GREEN, BLUE, RED
     */
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
