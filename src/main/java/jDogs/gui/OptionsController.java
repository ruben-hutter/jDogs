package jDogs.gui;

import jDogs.serverclient.clientside.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class OptionsController implements Initializable {


    @FXML
    private TableView<?> tableViewHighScore;

    @FXML
    private Pane pane;

    @FXML
    private TextField textFieldName;

    @FXML
    private Text textDisplay;

    @FXML
    void changeNameButtonOnAction(ActionEvent event) {
        if (GUIManager.getInstance().getState().equals("publicLobby")) {
            String name = textFieldName.getText();
            if (!name.isEmpty() && !name.isBlank()) {
                Client.getInstance().sendMessageToServer("USER " + name);
            }
            textFieldName.clear();
        }
    }

    public void updateHighScoreList() {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textDisplay.setText("Your name: " + Client.getInstance().getNickname());



    }
}
