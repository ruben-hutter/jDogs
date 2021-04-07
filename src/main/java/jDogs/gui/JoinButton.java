package jDogs.gui;

import javafx.scene.control.Button;

public class JoinButton extends Button {

    private String gameId;

    JoinButton(String gameId) {
        super();
        this.gameId = gameId;

    }

    public String getGameId() {
        return gameId;
    }

}
