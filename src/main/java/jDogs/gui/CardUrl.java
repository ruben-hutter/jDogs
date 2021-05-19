package jDogs.gui;

import java.util.Random;

/**
 * Chooses the correct image for a given card
 */
public class CardUrl {

    public static String getURL(String card) {
        Random random = new Random();
        String url = null;
        int randNum;

        switch (card) {

            case "ACEE":
                String[] aces = {"/playing_cards/Ac.png", "/playing_cards/Ad.png", "/playing_cards/Ah.png", "/playing_cards/As.png"};
                randNum = random.nextInt(aces.length);
                url = aces[randNum];
                //url = "/cards/AC.png";
                break;

            case "TWOO":
                String[] twos = {"/playing_cards/2c.png", "/playing_cards/2d.png", "/playing_cards/2h.png", "/playing_cards/2s.png"};
                randNum = random.nextInt(twos.length);
                url = twos[randNum];
                break;

            case "THRE":
                String[] threes = {"/playing_cards/3c.png", "/playing_cards/3d.png", "/playing_cards/3h.png", "/playing_cards/3s.png"};
                randNum = random.nextInt(threes.length);
                url = threes[randNum];
                break;

            case "FOUR":
                String[] fours = {"/playing_cards/4c.png", "/playing_cards/4d.png", "/playing_cards/4h.png", "/playing_cards/4s.png"};
                randNum = random.nextInt(fours.length);
                url = fours[randNum];
                break;

            case "FIVE":
                String[] fives = {"/playing_cards/5c.png", "/playing_cards/5d.png", "/playing_cards/5h.png", "/playing_cards/5s.png"};
                randNum = random.nextInt(fives.length);
                url = fives[randNum];
                break;

            case "SIXX":
                String[] sixes = {"/playing_cards/6c.png", "/playing_cards/6d.png", "/playing_cards/6h.png", "/playing_cards/6s.png"};
                randNum = random.nextInt(sixes.length);
                url = sixes[randNum];
                break;

            case "SEVE":
                String[] sevens = {"/playing_cards/7c.png", "/playing_cards/7d.png", "/playing_cards/7h.png", "/playing_cards/7s.png"};
                randNum = random.nextInt(sevens.length);
                url = sevens[randNum];
                break;

            case "EIGT":
                String[] eights = {"/playing_cards/8c.png", "/playing_cards/8d.png", "/playing_cards/8h.png", "/playing_cards/8s.png"};
                randNum = random.nextInt(eights.length);
                url = eights[randNum];
                break;

            case "NINE":
                String[] nines = {"/playing_cards/9c.png", "/playing_cards/9d.png", "/playing_cards/9h.png", "/playing_cards/9s.png"};
                randNum = random.nextInt(nines.length);
                url = nines[randNum];
                break;

            case "TENN":
                String[] tens = {"/playing_cards/10c.png", "/playing_cards/10d.png", "/playing_cards/10h.png", "/playing_cards/10s.png"};
                randNum = random.nextInt(tens.length);
                url = tens[randNum];
                break;

            case "QUEN":
                String[] queens = {"/playing_cards/Qc.png", "/playing_cards/Qd.png", "/playing_cards/Qh.png", "/playing_cards/Qs.png"};
                randNum = random.nextInt(queens.length);
                url = queens[randNum];
                break;

            case "JACK":
                String[] jacks = {"/playing_cards/Jc.png", "/playing_cards/Jd.png", "/playing_cards/Jh.png", "/playing_cards/Js.png"};
                randNum = random.nextInt(jacks.length);
                url = jacks[randNum];
                break;

            case "JOKE":
                url = "/playing_cards/Joker.png";
                break;

            case "KING":
                String[] kings = {"/playing_cards/Kc.png", "/playing_cards/Kd.png", "/playing_cards/Kh.png", "/playing_cards/Ks.png"};
                randNum = random.nextInt(kings.length);
                url = kings[randNum];
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

    /**
     * returns the name of the card
     * @param number number of the card
     * @return card as String
     */
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
