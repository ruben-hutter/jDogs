package jDogs.serverclient.serverside;

import org.jetbrains.annotations.NotNull;

/**
 * this class represents a user with his records
 */
public class SavedUser implements Comparable {
    private String name;
    private int points;
    private int playedGames;
    private int victories;

    SavedUser(String name) {
        this.name = name;
        this.playedGames = 0;
        this.victories = 0;
    }

    /**
     * get nameID
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * compare points
     * @param object
     * @return
     */
    @Override
    public int compareTo(@NotNull Object object) {
        SavedUser savedUser2 = (SavedUser) object;
        return savedUser2.getPoints() - this.getPoints();
    }

    /**
     * get the points, this method returns the victories
     * adjust this method.
     * @return
     */
    private int getPoints() {
       return victories;
    }

    /**
     * get String for CSV row
     * @return
     */
    public String getCSVString() {
        return name + "," + playedGames + "," + victories + "," + getPoints();
    }

    /**
     * add a victory
     */
    public void addVictory() {
        addPlayedGame();
        victories++;
    }

    /**
     * add a defeat
     */
    public void addDefeat() {
        addPlayedGame();
    }

    /**
     * add a playedGame
     * used in case of defeat or victory
     */
    private void addPlayedGame() {
        playedGames++;
    }

    /**
     * set the played games when read SavedUser-Objects from csv
     * @param playedGames
     */
    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    /**
     * set the victories when read SavedUser-Objects from csv
     * @param victories
     */
    public void setVictories(int victories) {
        this.victories = victories;
    }

    /**
     * set the points when read SavedUser-Objects from csv
     * @param points
     */
    public void setPoints(int points) {
        this.points = points;
    }
}
