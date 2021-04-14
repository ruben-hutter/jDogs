package jDogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Example {

    public static void main(String[] args) {

        ArrayList<String> newDeck = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            newDeck.add("TWOO");
        }
        for (int i = 8; i < 16; i++) {
            newDeck.add("THRE");

        }
        for (int i = 16; i < 24; i++) {
            newDeck.add("FOUR");
        }
        for (int i = 24; i < 32; i++) {
            newDeck.add("FIVE");
        }
        for (int i = 32; i < 40; i++) {
            newDeck.add("SIXX");
        }
        for (int i = 40; i < 48; i++) {
            newDeck.add("SEVE");
        }
        for (int i = 48; i < 56; i++) {
            newDeck.add("EIGT");
        }
        for (int i = 56; i < 64; i++) {
            newDeck.add("NINE");
        }
        for (int i = 64; i < 72; i++) {
            newDeck.add("TENN");
        }
        for (int i = 72; i < 80; i++) {
            newDeck.add("ELVN");
        }
        for (int i = 80; i < 88; i++) {
            newDeck.add("KING");
        }
        for (int i = 88; i < 96; i++) {
            newDeck.add("DAME");
        }
        for (int i = 96; i < 104; i++) {
            newDeck.add("JACK");
        }
        for (int i = 104; i < 110; i++) {
            newDeck.add("JOKE");
        }

        Random random = new Random();

        int number = 6;

        String newHand = "" + number;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < number; j++) {
                System.out.println(newDeck.size());
                int randomNumber = random.nextInt(newDeck.size());
                newHand += " " + newDeck.get(randomNumber);
                newDeck.remove(randomNumber);
                System.out.println(newDeck.size());
            }
            // send newHand to player and to client here
            System.out.println(newHand);
            newHand = "" + number;
        }


    }





}
