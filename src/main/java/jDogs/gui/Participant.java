package jDogs.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class Participant {

    SimpleStringProperty player;

    Button button;

    Participant(String player) {
        this.player = new SimpleStringProperty(player);
        this.button = new Button("pM");
    }

    public void setPlayer(String player) {
        this.player.set(player);
    }

    public String getPlayer() {
        return player.get();
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }
}
