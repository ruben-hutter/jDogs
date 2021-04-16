package jDogs.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateGameWindowController {


    @FXML
    private TextField gameNameField;

    @FXML
    private TextField gameTotalField;

    @FXML
    private TextField teamModeField;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;


    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        GUIManager.getInstance().lobbyController.closeGameDialog();

    }

    @FXML
    void createButtonOnAction(ActionEvent event) {
        String teamMode = teamModeField.getText();

        if (teamMode.equals("yes")) {
            teamMode = "1";
        } else {
            teamMode = "0";
        }
        System.out.println("teamMode " + teamMode);
        GUIManager.getInstance().lobbyController.sendNewGame(gameNameField.getText(), gameTotalField.getText(), teamMode);
    }

}
