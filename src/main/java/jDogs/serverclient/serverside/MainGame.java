package jDogs.serverclient.serverside;

import jDogs.player.Player;
import java.util.ArrayList;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * this class contains a game of 4 participants
 * from here the whole game is organized
 */
public class MainGame {

    private final boolean teamMode;
    private String[] gameArray;
    private int turnNumber;
    private final GameState gameState;
    private int numbDealOut;
    private ArrayList<String> deck;
    private final Random random = new Random();
    private final Player[] playersArray;
    private static final Logger logger = LogManager.getLogger(MainGame.class);
    private int numberOfRounds;
    private String actualPlayer;
    private final String nameID;

    /**
     * Constructor of a new game
     * @param playersArray array with participants
     * @param nameID name of game
     * @param teamMode true or false
     */
    MainGame (Player[] playersArray, String nameID, boolean teamMode) {
        this.teamMode = teamMode;
        this.playersArray = playersArray;
        this.gameState = new GameState(this);
        this.nameID = nameID;
    }

    /**
     * Copy constructor used to check move SEVE
     * @param mainGame the actual mainGame
     * @param tempGameState the copy of the actualGameState
     */
    MainGame(MainGame mainGame, GameState tempGameState) {
        teamMode = mainGame.getGameState().getTeamMode();
        playersArray = mainGame.copyPlayersArray();
        gameState = tempGameState;
        nameID = mainGame.getGameId();
    }

    /**
     * this method starts the mainGame after the mainGame was added to serverlist
     * (otherwise there will be a conflict)
     */
    public void start() {
        setUp();
        startGameRhythm();
    }

    /**
     * This start method is only for test purposes
     * it does the same as {@link #startTest()}
     */
    public void startTest() {
        setUpTest();
        startGameRhythmTest();
    }

    /**
     * sets up the main game after calling the constructor
     * and sends a message to clients with
     * "GAME " + teamMode + " " + playersArray.length + " "
     * + getParticipants()"
     */
    public void setUp() {
        gameState.createPlayers();
        for (Player player : playersArray) {
            player.getServerConnection().getMessageHandlerServer().setPlaying(nameID);
            int teamModeInt = 0;
            if (teamMode) { teamModeInt = 1;}
            player.getServerConnection().sendToClient("GAME " + teamModeInt + " "
                    + playersArray.length + " " + getParticipants());
            logger.debug("Player   ServerConnection " + player.getServerConnection());
        }
    }

    /**
     * this setUp method is only for test purposes.
     * it does the same as {@link #setUp()}
     */
    public void setUpTest(){
        gameState.createPlayers();
    }

    /**
     * String of participants to send to clients
     * @return "name1 name2 name3 name4"
     */
    private String getParticipants() {

        StringBuilder participants = new StringBuilder();

        for (int i = 0; i < playersArray.length - 1; i++) {
            participants.append(playersArray[i].getPlayerName()).append(" ");
        }
        participants.append(playersArray[playersArray.length - 1].getPlayerName());

        logger.debug("Participants: " + participants);
        logger.debug("Array l??nge playersArray: " + playersArray.length);

        return participants.toString();
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
     * this method is only for test purposes
     * it does the same as {@link #startGameRhythm()}
     */
    private void startGameRhythmTest() {
        setRandomBeginnerTest();
        this.numberOfRounds = 0;
        turnNumber = 0;
        this.numbDealOut = 6;
        dealOutCardsTest(numbDealOut);
        nextTurnTest();
    }

    /**
     * this method sets a random beginner to play the game in a random order
     */
    private void setRandomBeginner() {
        int random = new Random().nextInt(playersArray.length);

        String[] oldArray = getParticipantsArray();

        this.gameArray = new String[oldArray.length];
        int playersNumb = 0;

        sendMessageToParticipants("INFO Beginner is " + oldArray[random]);
        actualPlayer = oldArray[random];
        getPlayer(actualPlayer).setAllowedToPlay(true);

        for (int i = random; i < oldArray.length; i++) {
            gameArray[playersNumb] = oldArray[i];
            playersNumb++;
        }
        for (int i = 0; i < random; i++) {
            gameArray[playersNumb] = oldArray[i];
            playersNumb++;
        }
    }

    /**
     * this test method is equivalent to {@link #setRandomBeginner()}
     * but it doesn't send a message to the clients
     */
    private void setRandomBeginnerTest() {
        int random = new Random().nextInt(playersArray.length);

        String[] oldArray = getParticipantsArray();

        this.gameArray = new String[oldArray.length];
        int playersNumb = 0;

        actualPlayer = oldArray[random];
        getPlayer(actualPlayer).setAllowedToPlay(true);

        for (int i = random; i < oldArray.length; i++) {
            gameArray[playersNumb] = oldArray[i];
            playersNumb++;
        }
        for (int i = 0; i < random; i++) {
            gameArray[playersNumb] = oldArray[i];
            playersNumb++;
        }
    }

    /**
     * this method sends a request to the next player to make a move
     * The exception is needed for tests without ServerConnection
     */
    private void nextTurn() {
        int numb = turnNumber % playersArray.length;
        actualPlayer = gameArray[numb];
        if (getPlayer(actualPlayer).isAllowedToPlay()) {
            try {
                sendMessageToParticipants("TURN " + actualPlayer);
            } catch (Exception e){
                System.err.println("No ServerConnection available (test)");
            }
        } else {
            turnComplete(actualPlayer);
        }
    }

    /**
     * this test method is equivalent to {@link #nextTurn()}
     * but it doesn't send a message to the client
     */
    private void nextTurnTest() {
        int numb = turnNumber % playersArray.length;
        actualPlayer = gameArray[numb];
        if(!getPlayer(actualPlayer).isAllowedToPlay()){
            turnComplete(actualPlayer);
        }
    }

    /**
     * this method deals out cards after all cards are played or when the game starts
     * @param number number of cards
     */
    private void dealOutCards(int number) {
        //deal out cards from here by using a certain procedure

        StringBuilder newHand;
        ArrayList<String> newHandArray;

        for (Player player : playersArray) {
            newHand = new StringBuilder("ROUN " + number);
            for (int j = 0; j < number; j++) {
                int randomNumber = random.nextInt(deck.size());
                String card = deck.remove(randomNumber);
                newHand.append(" ").append(card);
                gameState.getCards().get(player.getPlayerName()).add(card);
            }
            // send newHand to player and to client here
            player.sendMessageToClient(newHand.toString());
            player.setAllowedToPlay(true);
            logger.debug("Player " + player.getPlayerName() + " has cards " + newHand);
        }
        sendMessageToParticipants("HAND");
    }

    /**
     * this method is similar to {@link #dealOutCards(int)}
     * but it doesn't send messages to clients
     * and you can add the desired cards to the client's hand manually
     * @param number number of cards
     */
    private void dealOutCardsTest(int number) {
        //deal out cards from here by using a certain procedure
        for (Player player : playersArray) {
            for (int j = 0; j < number; j++) {
                String card1 = "KING";
                String card2 = "JOKE";
                String card3 = "ACEE";
                gameState.getCards().get(player.getPlayerName()).add(card1);
                gameState.getCards().get(player.getPlayerName()).add(card2);
                gameState.getCards().get(player.getPlayerName()).add(card3);
            }
            player.setAllowedToPlay(true);
        }
    }

    /**
     *get deck for this round (6 hands distributed)
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
     */
    public void turnComplete(String nickname) {
        System.out.println(nickname + " finished his turn");
        actualPlayer = null;
        turnNumber++;
        numberOfRounds++;

        // new round
        //no cards in any player`s hand
        if (numberOfRounds / playersArray.length == numbDealOut) {
            if (numbDealOut == 2) {
                numbDealOut = 6;

                // anew deck
                deck = getDeck();

                numberOfRounds = 0;
            } else {
                numbDealOut--;
                numberOfRounds = 0;
            }
            dealOutCards(numbDealOut);
        }
        nextTurn();
    }

    /**
     * get array of names with participants
     * @return participants names
     */
    private String[] getParticipantsArray() {
        String[] playerNames = new String[playersArray.length];
        int count = 0;
        for (Player player : playersArray) {
            playerNames[count] = player.getPlayerName();
            count++;
        }
        return playerNames;
    }

    /**
     * get a player by nickname
     * @param actualPlayer nickname
     * @return player container
     */
    public Player getPlayer(String actualPlayer) {
        for (Player player : playersArray) {
            if (player.getPlayerName().equals(actualPlayer)) {
                return player;
            }
        }
        return null;
    }

    /**
     * this method is used to check the gameID
     * @return the gameID
     */
    public String getGameId() {
        return nameID;
    }

    /**
     * send message to participants of game
     * @param message a given message
     */
    public void sendMessageToParticipants(String message) {
        for (Player player : playersArray) {
            player.getServerConnection().sendToClient(message);
        }
    }

    /**
     * get the gameState of the maingame
     * @return actual game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * sends data to the archive
     * and the main game should be collected by the garbage collector
     */
    public void delete() {
        Server.getInstance().deleteMainGame(this);
    }

    /**
     * get actual player allowed to play
     * @return name of player
     */
    public String getActualPlayer() {
        System.out.println("actual player " + actualPlayer);
        return actualPlayer;
    }

    /**
     * get Array of players
     * @return playerArray
     */
    public Player[] getPlayersArray() {
        return playersArray;
    }

    /**
     * Gives team mode state
     * @return true if team mode on, false if not
     */
    public boolean isTeamMode() {
        return teamMode;
    }

    /**
     * Makes a copy of the in game players
     * @return a hard copy of the array
     */
    public Player[] copyPlayersArray() {
        Player[] playersArrayCopy = new Player[playersArray.length];
        int i = 0;
        for (Player player : playersArray) {
            playersArrayCopy[i++] = new Player(player.getPlayerName());
        }
        return playersArrayCopy;
    }

    /**
     * Gets a player for a given alliance
     * @param alliance player's alliance as string
     * @param mainGame instance of MainGame
     * @return the player or null
     */
    public Player getPlayerForAlliance(String alliance, MainGame mainGame) {
        RulesCheckHelper helper = new RulesCheckHelper(mainGame);
        for (Player player : playersArray) {
            if (player.getAlliance() == helper.convertAlliance(alliance)) {
                return player;
            }
        }
        return null;
    }
}
