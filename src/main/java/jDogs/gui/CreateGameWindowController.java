package jDogs.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * a popupWindow to create an open game
 */
public class CreateGameWindowController {
    @FXML
    private TextField gameNameField;

    @FXML
    private CheckBox checkBox;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    /**
     * button to cancel game
     * @param event fires if the cancel button is pressed
     */
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        GUIManager.getInstance().getPubLobbyController().closeGameDialog();
    }

    /**
     * button to create new game
     * @param event fires if a new game gets created in this window
     */
    @FXML
    void createButtonOnAction(ActionEvent event) {
    String teamMode;

        if ((checkBox.isSelected())) {
            teamMode = "1";
        } else {
            teamMode = "0";
        }
        System.out.println("teamMode " + teamMode);
        GUIManager.getInstance().getPubLobbyController().sendNewGame(gameNameField.getText(),teamMode);
    }
}
