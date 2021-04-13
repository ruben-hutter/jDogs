package jDogs.serverclient.serverside;

import java.util.ArrayList;
import java.util.Random;

public class MainGame {
    private String[] gameArray;
    private GameFile gameFile;
    private int cardsToDealOut;
    private int turnNumber;
    private ArrayList<String> deck;

    MainGame(GameFile gameFile) {
        this.gameFile = gameFile;
        this.cardsToDealOut = 6;
        startGameRhythm();
    }

    private void startGameRhythm() {
        setRandomBeginner();
        turnNumber = 0;
        dealOutCards(cardsToDealOut);
        cardsToDealOut--;
        nextTurn();
    }


    private void setRandomBeginner() {
        int random = new Random().nextInt(gameFile.getNumberOfParticipants());

        String[] oldArray = gameFile.getParticipantsArray();

        gameArray = new String[oldArray.length];
        int players = 0;

        for (int i = random; i < oldArray.length; i++) {
            gameArray[players] = oldArray[i];
            players++;
        }
        for (int i = 0; i < random; i++) {
            gameArray[players] = oldArray[i];
            players++;
        }
    }

    private void nextTurn() {
        //sendRequest to next player to make a move


        if (cardsToDealOut == 2) {
            cardsToDealOut = 6;
        }

        Server.getInstance().getSender(gameArray[turnNumber]).sendStringToClient("TURN");

        System.out.println(turnNumber);
        System.out.println(gameArray[turnNumber]);

        System.out.println("TURN");
    }

    private void dealOutCards(int number) {
        deck = getDeck();
        Random random = new Random();

        String newHand = "" + number;
        for (int i = 0; i < gameFile.getNumberOfParticipants(); i++) {
            for (int j = 0; j < number; j++) {
                System.out.println(deck.size());
                int randomNumber = random.nextInt(deck.size());
                newHand += " " + deck.get(randomNumber);
                deck.remove(randomNumber);
                System.out.println(deck.size());
            }
            // send newHand to player and to client here
            System.out.println(newHand);
            newHand = "" + number;
        }


    }

    private ArrayList<String> getDeck() {
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

        return newDeck;

    }


    public void turnComplete(String nickname) {
        System.out.println(nickname + " finished his turn");
        turnNumber++;
        // new round
        //no cards in any player`s hand
        // if turnNumber == numberOfCardsDealtOut
        if (true) {
            //how many? set number correctly
            dealOutCards(6);
        }

    }

    public String getGameId() {
        return gameFile.getNameId();
    }


    public GameFile getGameFile() {
        return gameFile;
    }

}
