package jDogs.serverclient.serverside;

import java.util.Random;

public class MainGame {
    private String[] gameArray;
    private GameFile gameFile;
    private int turnNumber;


    MainGame(GameFile gameFile) {
        this.gameFile = gameFile;
        startGameRhythm();
    }

    private void startGameRhythm() {
        setRandomBeginner();
        turnNumber = 0;
        dealOutCards();
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

        Server.getInstance().getSender(gameArray[turnNumber]).sendStringToClient("TURN");
        System.out.println(turnNumber);
        System.out.println(gameArray[turnNumber]);

        System.out.println("TURN");
    }

    private void dealOutCards() {
        //deal out cards from here by using a certain procedure
    }


    public void turnComplete(String nickname) {
        System.out.println(nickname + " finished his turn");
        turnNumber++;
        // new round
        //no cards in any player`s hand
        if (true) {
            dealOutCards();
        }

    }

    public String getGameId() {
        return gameFile.getNameId();
    }


    public GameFile getGameFile() {
        return gameFile;
    }
}
