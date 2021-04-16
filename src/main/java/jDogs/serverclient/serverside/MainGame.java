package jDogs.serverclient.serverside;

import jDogs.player.Player;
import java.util.ArrayList;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainGame {
    private String[] gameArray;
    private GameFile gameFile;
    private int turnNumber;
    private GameState gameState;
    private int numbDealOut;
    private ArrayList<String> deck;
    private Random random = new Random();
    private ArrayList<Player> players;
    private static final Logger logger = LogManager.getLogger(MainGame.class);



    MainGame(GameFile gameFile) {
        this.gameFile = gameFile;
        GameState gameState = new GameState(gameFile);
        gameFile.sendMessageToParticipants("GAME " + gameFile.getNumberOfParticipants() + " "
                + gameFile.getParticipants());

        players = gameFile.getPlayers();

        startGameRhythm();
    }

    /**
     * this method initializes the game
     */
    private void startGameRhythm() {
        setRandomBeginner();
        turnNumber = 0;
        this.numbDealOut = 6;
        //first deck
        deck = getDeck();
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

        System.out.println("RANDOM beginner is " + oldArray[random]);
        gameFile.sendMessageToParticipants("INFO Beginner is " + oldArray[random]);
        logger.debug("Random beginner is: " +  oldArray[random]);

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
        logger.debug("Next turn is player: " + Server.getInstance().getSender(gameArray[turnNumber]));
    }

    private void dealOutCards(int number) {
        //deal out cards from here by using a certain procedure

        String newHand;
        ArrayList<String> newHandArray;

        for (int i = 0; i < gameFile.getNumberOfParticipants(); i++) {
            newHandArray = new ArrayList<>();
            newHand = "ROUN " + turnNumber + " " + number;

            for (int j = 0; j < number; j++) {
                int randomNumber = random.nextInt(deck.size());
                String card = deck.remove(randomNumber);
                System.out.println("Card " + card);
                newHand += " " + card;
                newHandArray.add(card);
            }
            // send newHand to player and to client here
            players.get(i).setDeck(newHandArray);
            players.get(i).sendMessageToClient(newHand);
            logger.debug("Player " + players.get(i) + " has cards " + newHandArray);
            logger.debug("Client get the cards as: " + newHand);
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
            newDeck.add("ACEE");
        }
        for (int i = 80; i < 88; i++) {
            newDeck.add("KING");
        }
        for (int i = 88; i < 96; i++) {
            newDeck.add("QUEN");
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
                // anew deck
                deck = getDeck();
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

    public Player getPlayer(String nickname) {
        return gameState.getPlayer(nickname);

    }
}
