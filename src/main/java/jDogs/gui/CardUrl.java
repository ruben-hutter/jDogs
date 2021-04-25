package jDogs.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class CardUrl {
    //TODO here cardURLArray to getback all urls for all dialog controller to go through all cards

    String[] cardURL = new String[14];


    public static URL getURL(String card) {
        String url = null;

        switch (card) {

            case "ACEE":
                url = "src/main/resources/cards/AC.png";
                break;

            case "TWOO":
                url = "src/main/resources/cards/2C.png";
                break;

            case "THRE":
                url = "src/main/resources/cards/3C.png";
                break;

            case "FOUR":
                url = "src/main/resources/cards/4C.png";
                break;

            case "FIVE":
                url = "src/main/resources/cards/5C.png";
                break;

            case "SIXX":
                url = "src/main/resources/cards/6C.png";
                break;

            case "SEVE":
                url = "src/main/resources/cards/7C.png";
                break;

            case "EIGT":
                url = "src/main/resources/cards/8C.png";
                break;

            case "NINE":
                url = "src/main/resources/cards/9C.png";
                break;

            case "TENN":
                url = "src/main/resources/cards/10C.png";
                break;

            case "QUEN":
                url = "src/main/resources/cards/QC.png";
                break;

            case "JACK":
                url = "src/main/resources/cards/JC.png";
                break;

            case "JOKE":
                url = "src/main/resources/cards/red_back.png";
                break;

            case "KING":
                url = "src/main/resources/cards/KC.png";
                break;

            default:
                System.err.println("UNKNOWN CARD " + card);
        }

        try {
            return Paths.get(url).toUri().toURL();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String[] getURLArray() {

    }
}
