package jDogs.serverclient.serverside;

import java.util.Random;

public class MainGame {
    private String[] gameArray;
    private GameFile gameFile;
    private int turnNumber;


    MainGame(GameFile gameFile) {
        this.gameFile = gameFile;
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
        Server.getInstance().getSender(gameArray[turnNumber] + "TURN");
    }

    private void dealOutCards() {
    }


    public void turnComplete(String nickname) {
        System.out.println(nickname + " finished his turn");
        turnNumber++;
        // new round
        if (turnNumber == gameFile.getNumberOfParticipants() - 1) {
            dealOutCards();
        }

    }

    public String getGameId() {
        return gameFile.getNameId();
    }



}
