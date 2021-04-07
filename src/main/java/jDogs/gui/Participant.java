package jDogs.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class Participant {

    SimpleStringProperty player;


    Participant(String player) {
        this.player = new SimpleStringProperty(player);
    }

    public void setPlayer(String player) {
        this.player.set(player);
    }

    public String getPlayer() {
        return player.get();
    }

}
