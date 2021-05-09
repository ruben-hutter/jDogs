package jDogs.serverclient.serverside;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;
import static org.apache.logging.log4j.core.util.Loader.getResourceAsStream;

import jDogs.Main;
import jDogs.player.Player;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import org.apache.logging.log4j.core.util.Loader;
import org.checkerframework.checker.units.qual.C;
import org.xml.sax.InputSource;

/**
 * This class is used to store as CSV-File
 * and update the highscore list
 */
public class CSVWriter {

    private final ArrayList<SavedUser> usersHighScore;

    private String FILENAME = "/highScoreList.csv";

    //private final String FILENAME = this.getClass().getClassLoader().getResource("src/main/resources/highScoreList.csv").toExternalForm();
    //private final String FILENAME = "src/main/resources/highScoreList.csv";

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
        InputStream inputStream = getClass().getResourceAsStream(FILENAME);
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
     * @param line line
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

        SavedUser savedUser = new SavedUser(name);
        savedUser.setPlayedGames(playedGames);
        savedUser.setVictories(victories);
        usersHighScore.add(savedUser);
    }

    public void addUser(SavedUser savedUser) {
        usersHighScore.add(savedUser);
    }
}


