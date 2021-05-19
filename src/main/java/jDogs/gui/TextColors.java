package jDogs.gui;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class TextColors {

    public static String[] getSingleTextColor() {

        String[] colors = new String[4];
        colors[0] = "D2B710";
        colors[1] = "559869";
        colors[2] = "10A1D2";
        colors[3] = "F06017";

        return colors;
    }

    public static Paint[] getTextGradients() {
        Paint[] gradients = new Paint[2];
        gradients[0] = new LinearGradient(0, .1, 1, 1,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#d2b710")),
                new Stop(1, Color.web("#10a1d2")));

         gradients[1] = new LinearGradient(0.2, 0.5, 0.2, 0.4, true,
                 CycleMethod.REPEAT,
                new Stop(0,Color.web("#559869")),
                new Stop(1, Color.web("#f06017")));
        return gradients;
    }


    public static String[] getTeamShadowColor() {
        return null;
    }

    public static String[] getSingleShadowColor() {
        return null;
    }
}
