package jDogs.serverclient.serverside;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * thsi class is used to store as CSV-File
 * and update the highscore list
 */
public class CSVWriter {
    private ArrayList<SavedUser> usersHighScore;
    private final String FILENAME = "HighScoreList.csv";

    CSVWriter() {
        this.usersHighScore = new ArrayList<SavedUser>();
    }

    /**
     * rank the list, so that
     * the highest core is in the first place.
     */
    public void rankList() {
        Collections.sort(usersHighScore);
        int rank = 1;
        //update Rank
        for (SavedUser savedUser : usersHighScore) {
            savedUser.setRank(rank);
            rank++;
        }

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
            csvWriter = new FileWriter("new.csv");
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
}


