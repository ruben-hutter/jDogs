package jDogs.gui;

import jDogs.serverclient.clientside.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LobbyController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

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
        //System.out.println(message);
        Client.getInstance().sendMessageToServer("PCHT " + message);
    }



    public void displayPCHTmsg(String message) {
        //System.out.println(message);
        publicChatMessagesLobby.appendText(message + "\n");
    }


    public void displayWCHTmsg(String message) {
        System.out.println(message + " wcht not implemented");
    }
}
