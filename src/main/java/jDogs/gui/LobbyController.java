package jDogs.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LobbyController {

    @FXML
    private TextArea publicChatMessagesLobby;

    @FXML
    private TextField messageFieldLobby;

    @FXML
    private Button sendButtonLobby;

    @FXML
    private MenuItem exitGameLobby;

    @FXML
    private TextField textField;

    @FXML
    void exitJDogs(ActionEvent event) {
        System.out.println("exit..");
    }

    @FXML
    void sendButtonOnAction(ActionEvent event) {
        System.out.println("send..");
        String message = messageFieldLobby.getText();
        messageFieldLobby.clear();
        System.out.println(message);
        displayMessage(message);
    }


    @FXML
    public void refreshTextArea() {
        //publicChatMessagesLobby.setText("");
    }

    @FXML
    public void displayMessage(String message) {
        System.out.println(message);
        publicChatMessagesLobby.appendText(message + "\n");
    }
}
