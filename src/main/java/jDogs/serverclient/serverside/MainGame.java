package jDogs.serverclient.serverside;

import java.util.ArrayList;
import java.util.Random;
import org.checkerframework.checker.units.qual.A;

public class MainGame {

    private String[] gameArray;
    private GameFile gameFile;
    private int turnNumber;
    private GameState gameState;
    private int numbDealOut;



    MainGame(GameFile gameFile) {
        this.gameFile = gameFile;
        GameState gameState = new GameState(gameFile);
        gameFile.sendMessageToParticipants("GAME " + gameFile.getNumberOfParticipants() + " "
                + gameFile.getParticipants());
        startGameRhythm();
    }

    /**
     * this method initializes the game
     */
    private void startGameRhythm() {
        setRandomBeginner();
        turnNumber = 0;
        this.numbDealOut = 6;
        dealOutCards(numbDealOut);
        nextTurn();
    }

    /**
     * this method sets a random beginner to play the game in a random order
     */
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

    /**
     * this method sends a request to the next player to make a move
     */
    private void nextTurn() {

        Server.getInstance().getSender(gameArray[turnNumber]).sendStringToClient("TURN");
        // System.out.println(turnNumber);
        // System.out.println(gameArray[turnNumber]);

        // System.out.println("TURN");
    }

    private void dealOutCards(int number) {
        //deal out cards from here by using a certain procedure

        ArrayList<String> deck = getDeck();
        Random random = new Random();

        String newHand;
        ArrayList<String> newHandArray;

        for (int i = 0; i < gameFile.getNumberOfParticipants(); i++) {
            newHandArray = new ArrayList<>();
            newHand = "ROUN " + turnNumber + " " + number;

            for (int j = 0; j < number; j++) {
                System.out.println(deck.size());
                int randomNumber = random.nextInt(deck.size());
                newHand += " " + deck.get(randomNumber);
                newHandArray.add(deck.get(randomNumber));
                deck.remove(randomNumber);
                System.out.println(deck.size());
            }
            // send newHand to player and to client here
            gameState.playersState[i].setDeck(newHandArray);
            gameState.playersState[i].sendMessageToClient(newHand);
            System.out.println(newHand);

        }

    }

    /**
     *
     * @return a deck of 110 cards to send messages to clients
     */
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

    /**
     * this method gets called if a player completed his turn
     * (i.e.: he has sent a valid card to server)
     * @param nickname maybe not necessary to send the nickname
     *                 t
     */
    public void turnComplete(String nickname) {
        System.out.println(nickname + " finished his turn");
        turnNumber++;
        // new round
        //no cards in any player`s hand
        if (turnNumber == numbDealOut) {
            if (numbDealOut == 2) {
                numbDealOut = 6;
            } else {
                numbDealOut--;
            }
            dealOutCards(numbDealOut);
        }

        nextTurn();
    }

    public String getGameId() {
        return gameFile.getNameId();
    }


    public GameFile getGameFile() {
        return gameFile;
    }
}
