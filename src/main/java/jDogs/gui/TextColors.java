package jDogs.gui;

/**
 * this class represents colors used to color the text which displays the player name in gui
 */
public class TextColors {

    /**
     * get all colors for text
     * @return yellow, green, blue, red
     */
    public static String[] getTextFill() {
        String[] colors = new String[4];
        colors[0] = "D2B710";
        colors[1] = "559869";
        colors[2] = "10A1D2";
        colors[3] = "F06017";

        return colors;
    }

    /**
     * get the team shadow for dropShadow
     * @return shadow for yellow, green, blue, red
     */
    public static String[] getTeamShadowColor() {
         String[] colors = new String[4];
        colors[0] = "10A1D2";
        colors[1] = "F06017";
        colors[2] = "D2B710";
        colors[3] = "559869";
        return colors;
    }
    /**
     * get the single shadow for dropShadow
     * @return shadow for yellow, green, blue, red
     */
    public static String[] getSingleShadowColor() {
        String[] colors = new String[4];
        colors[0] = "D6CEA0";
        colors[1] = "A3C2AC";
        colors[2] = "#70C0DB";
        colors[3] = "#E3A789";

        return colors;
    }
}
