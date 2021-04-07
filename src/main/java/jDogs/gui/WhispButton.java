package jDogs.gui;

import javafx.scene.control.Button;

public class WhispButton extends Button {

    String adressant;
    WhispButton(String adressant) {
        super();
        this.adressant = adressant;
    }

    public String getAdressant() {
        return adressant;
    }
}
