package jDogs.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class CardUrl {
    //TODO here cardURLArray to getback all urls for all dialog controller to go through all cards




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
        String[] cardURL = new String[14];

        cardURL[0] = "src/main/resources/cards/AC.png";
        cardURL[1] = "src/main/resources/cards/2C.png";
        cardURL[2] = "src/main/resources/cards/3C.png";
        cardURL[3] = "src/main/resources/cards/4C.png";
        cardURL[4] = "src/main/resources/cards/5C.png";
        cardURL[5] = "src/main/resources/cards/6C.png";
        cardURL[6] = "src/main/resources/cards/7C.png";
        cardURL[7] = "src/main/resources/cards/8C.png";
        cardURL[8] = "src/main/resources/cards/9C.png";
        cardURL[9] = "src/main/resources/cards/10C.png";
        cardURL[10] = "src/main/resources/cards/QC.png";
        cardURL[11] = "src/main/resources/cards/JC.png";
        cardURL[12] = "src/main/resources/cards/KC.png";
        cardURL[13] = "src/main/resources/cards/red_back.png";

        return cardURL;
    }

    public static String getCardNameByNumber(int number) {
        String[] card = new String[14];

        card[0] = "ACEE";
        card[1] = "TWOO";
        card[2] = "THRE";
        card[3] = "FOUR";
        card[4] = "FIVE";
        card[5] = "SIXX";
        card[6] = "SEVE";
        card[7] = "EIGT";
        card[8] = "NINE";
        card[9] = "TENN";
        card[10] = "QUEN";
        card[11] = "JACK";
        card[12] = "KING";

        return card[number];
    }

}
