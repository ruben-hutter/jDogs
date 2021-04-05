package jDogs.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    private String nickname;

    @FXML
    private Button loginButton;

    @FXML
    private TextField textField;

    @FXML
    private Button cancelButton;

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        //Shutdown
        System.exit(-1);
    }

    @FXML
    void loginButtonOnAction(ActionEvent event) {
        nickname = textField.getText();
        if (nickname.isEmpty()) {
            //open AlertWindow here
        } else {
            System.out.println(nickname);
            LoginEventHandler.INSTANCE.loggedIn(nickname);
        }
    }
}
