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
    private int numberOfRounds;
    private String actualPlayer;


    MainGame (GameFile gameFile) {
        this.gameFile = gameFile;
        this.gameState = new GameState(gameFile);
        setUp();
        startGameRhythm();
    }

    /**
     * sets up the main game after calling the constructor
     */
    public void setUp() {
        gameState.createPlayers();
        players = gameFile.getPlayers();


        for (Player player : players) {
            player.getServerConnection().getMessageHandlerServer().setPlaying(true, this);
            player.getServerConnection().getSender().sendStringToClient("GAME " + gameFile.getNumberOfParticipants() + " "
                    + gameFile.getParticipants());
            logger.debug("Player   ServerConnection " + player.getServerConnection());
        }
    }

    /**
     * this method initializes the game
     */
    private void startGameRhythm() {
        setRandomBeginner();
        this.numberOfRounds = 0;
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
        System.out.println("turnNumbers in nextTurn " + turnNumber);
        int numb = turnNumber % players.size();
        actualPlayer = gameArray[numb];
        if (gameFile.getPlayer(actualPlayer).isAllowedToPlay()) {
            Server.getInstance().getSender(actualPlayer).sendStringToClient("TURN");
        } else {
            turnComplete(actualPlayer);
        }
    }

    /**
     * this method deals out cards after all cards are played or when the game starts
     * @param number
     */
    private void dealOutCards(int number) {
        //deal out cards from here by using a certain procedure

        String newHand;
        ArrayList<String> newHandArray;

        for (Player player : gameFile.getPlayers()) {
            newHand = "ROUN " +  " " + number;

            for (int j = 0; j < number; j++) {
                int randomNumber = random.nextInt(deck.size());
                String card = deck.remove(randomNumber);
                newHand += " " + card;
                gameState.getCards().get(player.getPlayerName()).add(card);
            }
            // send newHand to player and to client here
            player.sendMessageToClient(newHand);
            player.setAllowedToPlay(true);
            logger.debug("Player " + player.getPlayerName() + " has cards " + newHand);
        }
        gameFile.sendMessageToParticipants("HAND");



/*
        ArrayList<String> newHandArray;
        String newHand;

        newHandArray = new ArrayList<>();
        //damit checkCard funktioniert, müssen die Strings einzeln hinzugefügt werden
        //newHandArray.add("ACEE KING JOKE SIXX FOUR JACK");
        newHand = "ROUN " + turnNumber + " " + number;

        String hand = "ROUN " + turnNumber + " " + number + " ACEE ACEE TENN TWOO EIGT NINE";
        String a = "ACEE";
        String b = "TENN";
        String c = "TWOO";
        String d = "EIGT";
        String e = "NINE";
        String f = "ACEE";
        newHandArray.add(a);
        newHandArray.add(b);
        newHandArray.add(c);
        newHandArray.add(d);
        newHandArray.add(e);
        newHandArray.add(f);




        for (Player player : players) {
            player.setAllowedToPlay(true);
            player.sendMessageToClient(hand);
            gameState.getCards().get(player.getPlayerName()).add(a);
            gameState.getCards().get(player.getPlayerName()).add(b);
            gameState.getCards().get(player.getPlayerName()).add(c);
            gameState.getCards().get(player.getPlayerName()).add(d);
            gameState.getCards().get(player.getPlayerName()).add(e);
            gameState.getCards().get(player.getPlayerName()).add(f);
        }

 */

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
        actualPlayer = null;
        turnNumber++;
        numberOfRounds++;
        System.out.println("number of rounds " + numberOfRounds);
        System.out.println("calc numberofrounds/participants " + numberOfRounds / gameFile.getNumberOfParticipants());
        System.out.println("numberDeal out 1 " + numbDealOut);
        // new round
        //no cards in any player`s hand
        if (numberOfRounds / gameFile.getNumberOfParticipants() == numbDealOut) {
            if (numbDealOut == 2) {
                System.out.println("entered if ");
                numbDealOut = 6;
                // anew deck
                deck = getDeck();
                numberOfRounds = 0;
            } else {
                System.out.println("2 NUMB DEAL OUT " + numbDealOut);
                numbDealOut--;
                numberOfRounds = 0;
            }
            dealOutCards(numbDealOut);
        }
        nextTurn();

    }

    /**
     * this method is used to check the gameID
     * @return
     */
    public String getGameId() {
        return gameFile.getNameId();
    }

    /**
     * get the gamefile of this game
     * @return
     */
    public GameFile getGameFile() {
        return gameFile;
    }

    /**
     * get the gameState of the maingame
     * @return
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * sends the game file to the archive
     * and the main game will be left to the garbage collector
     */
    public void kill() {
        this.gameFile.cancel();
    }

    public String getActualPlayer() {
        return actualPlayer;
    }
}
