package jDogs.serverclient.serverside;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to store as CSV-File
 * and update the highscore list
 */
public class CSVWriter {

    private final ArrayList<SavedUser> usersHighScore;
    private final String csvFileDirectory;
    private final String FILEPATH;
    private final boolean directoryExists;

    /**
     * construct a CSV-Writer object by instantiating a new ArrayList
     */
    CSVWriter() {
        this.usersHighScore = new ArrayList<>();
        this.csvFileDirectory = setFileDirectory() + "/jDogsData";
        this.FILEPATH = csvFileDirectory + "/highScoreList.csv";
        this.directoryExists = !setDirectory();
    }

    /**
     * create folder for data,
     * if not already created
     */
    private boolean setDirectory() {
        File directory = new File(csvFileDirectory);
        return directory.mkdir();
    }

    /**
     * rank the list, so that
     * the highest core is in the first place.
     */
    public void rankList() {
        Collections.sort(usersHighScore);
    }

    /**
     * gets the filePath of the jar
     * @return root directory of jar
     */
    private String setFileDirectory() {
        File jarPath=new File(CSVWriter.class.getProtectionDomain().getCodeSource().getLocation().
                getPath());
        System.out.println(jarPath.getParentFile().getAbsolutePath());

        return jarPath.getParentFile().getAbsolutePath();
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
            csvWriter = new FileWriter(FILEPATH);
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
            assert csvWriter != null;
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
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(FILEPATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = null;
        try {
            assert fileInputStream != null;
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            //ignore first line with info
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
               parseAndAddLine(line);
            }
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("could not load highScoreList.csv");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("could not find highScoreList.csv");
            System.out.println("path " + FILEPATH);
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

    /**
     * adds a user who played
     * @param savedUser file with name, victories, defeats
     */
    public void addUser(SavedUser savedUser) {
        usersHighScore.add(savedUser);
    }

    /**
     * check if directory exists
     * @return true, if directory exists, false if not
     */
    public boolean isDirectoryExisting() {
        return directoryExists;
    }
}
