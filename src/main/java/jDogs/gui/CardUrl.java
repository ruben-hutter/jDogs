package jDogs.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class CardUrl {
    public static String getURL(String card) {
        String url = null;

        switch (card) {

            case "ACEE":
                url = "/cards/AC.png";
                break;

            case "TWOO":
                url = "/cards/2C.png";
                break;

            case "THRE":
                url = "/cards/3C.png";
                break;

            case "FOUR":
                url = "/cards/4C.png";
                break;

            case "FIVE":
                url = "/cards/5C.png";
                break;

            case "SIXX":
                url = "/cards/6C.png";
                break;

            case "SEVE":
                url = "/cards/7C.png";
                break;

            case "EIGT":
                url = "/cards/8C.png";
                break;

            case "NINE":
                url = "/cards/9C.png";
                break;

            case "TENN":
                url = "/cards/10C.png";
                break;

            case "QUEN":
                url = "/cards/QC.png";
                break;

            case "JACK":
                url = "/cards/JC.png";
                break;

            case "JOKE":
                url = "/cards/red_back.png";
                break;

            case "KING":
                url = "/cards/KC.png";
                break;

            default:
                System.err.println("UNKNOWN CARD " + card);
        }
        return url;
    }

    public static String[] getURLArray() {
        String[] cardURL = new String[14];

        cardURL[0] = "/cards/AC.png";
        cardURL[1] = "/cards/2C.png";
        cardURL[2] = "/cards/3C.png";
        cardURL[3] = "/cards/4C.png";
        cardURL[4] = "/cards/5C.png";
        cardURL[5] = "/cards/6C.png";
        cardURL[6] = "/cards/7C.png";
        cardURL[7] = "/cards/8C.png";
        cardURL[8] = "/cards/9C.png";
        cardURL[9] = "/cards/10C.png";
        cardURL[10] = "/cards/QC.png";
        cardURL[11] = "/cards/JC.png";
        cardURL[12] = "/cards/KC.png";
        cardURL[13] = "/cards/red_back.png";

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
        card[10] = "JACK";
        card[11] = "QUEN";
        card[12] = "KING";

        return card[number];
    }

}
