package jDogs.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 * this class represents a player which is displayed in lobby window
 */
public class Participant {

    SimpleStringProperty player;

    /**
     * constructor of participant
     * @param player name
     */
    Participant(String player) {
        this.player = new SimpleStringProperty(player);
    }

    /**
     * set player
     * @param player name
     */
    public void setPlayer(String player) {
        this.player.set(player);
    }

    /**
     * return player name
     * @return name
     */
    public String getPlayer() {
        return player.get();
    }

}
