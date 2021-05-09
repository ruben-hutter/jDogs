package jDogs.serverclient.serverside;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to store as CSV-File
 * and update the highscore list
 */
public class CSVWriter {

    private final ArrayList<SavedUser> usersHighScore;
    private final String FILENAME = "src/main/resources/HighScoreList.csv";

    /**
     * construct a CSV-Writer object by instantiating a new ArrayList
     */
    CSVWriter() {
        usersHighScore = new ArrayList<>();
    }

    /**
     * rank the list, so that
     * the highest core is in the first place.
     */
    public void rankList() {
        Collections.sort(usersHighScore);
        int rank = 1;
    }

    /**
     * get the highScoreList
     * @return highScoreList
     */
    public ArrayList<SavedUser> getUsersHighScore() {
        return usersHighScore;
    }

    /**
     * writes a CSV-File
     */
    public void writeCSV() {
        FileWriter csvWriter = null;

        try {
            csvWriter = new FileWriter(FILENAME);
            csvWriter.append("Name");
            csvWriter.append(",");
            csvWriter.append("PlayedGames");
            csvWriter.append(",");
            csvWriter.append("Victories");
            csvWriter.append("\n");

            for (SavedUser savedUser : usersHighScore) {
                csvWriter.append(savedUser.getCSVString());
                csvWriter.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads the CSV-File
     */
    public void readCSV() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //ignore first line with info
            String line = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
               parseAndAddLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * parse a line from the csv document and
     * adds it to the usersHighScoreList
     * @param line
     */
    private void parseAndAddLine(String line) {
        int count = 0;
        String name = null;
        while (count < line.length()) {
            if (line.charAt(count) == ',') {
               name = line.substring(0, count);
               break;
            }
            count++;
        }

        count++;
        int nameSeparator = count;
        int playedGames = -1;
        while (count < line.length()) {
            if (line.charAt(count) == ',') {
                playedGames = Integer.parseInt(line.substring(nameSeparator, count));
                break;
            }
            count++;
        }

        count++;
        int playedGameSeparator = count;
        int victories = -1;
        while (count < line.length()) {
            if (line.charAt(count) == ',') {
                victories = Integer.parseInt(line.substring(playedGameSeparator, count));
                break;
            }
            count++;
        }

        count++;
        int victorySeparator = count;
        int points = Integer.parseInt(line.substring(victorySeparator, line.length()));

        SavedUser savedUser = new SavedUser(name);
        savedUser.setPlayedGames(playedGames);
        savedUser.setVictories(victories);
        savedUser.setPoints(points);
        usersHighScore.add(savedUser);
    }

    public void addUser(SavedUser savedUser) {
        usersHighScore.add(savedUser);
    }
}


