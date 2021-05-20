package jDogs.gui;

import javafx.beans.property.SimpleStringProperty;

/**
 * this class represents a user of the highScoreList
 */
public class SavedUserGui {

    private final SimpleStringProperty name;
    private final SimpleStringProperty playedGames;
    private final SimpleStringProperty victories;

    SavedUserGui(String name, String playedGames, String victories){
        this.name = new SimpleStringProperty(name);
        this.playedGames = new SimpleStringProperty(playedGames);
        this.victories = new SimpleStringProperty(victories);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPlayedGames() {
        return playedGames.get();
    }

    public void setPlayedGames(String playedGames) {
        this.playedGames.set(playedGames);
    }

    public String getVictories() {
        return victories.get();
    }

    public void setVictories(String victories) {
        this.victories.set(victories);
    }
}
