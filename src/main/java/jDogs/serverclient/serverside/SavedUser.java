package jDogs.serverclient.serverside;

import org.jetbrains.annotations.NotNull;

/**
 * this class represents a user with his records
 */
public class SavedUser implements Comparable {
    private String name;
    private int points;
    private int rank;
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
        return this.getPoints() - savedUser2.getPoints();
    }

    private int getPoints() {
        return points;
    }

    /**
     * set rank(maybe we don `t need this method)
     * @param rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * get String for CSV row
     * @return
     */
    public String getCSVString() {
        return name + "," + playedGames + "," + victories + "," + points;
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




}
