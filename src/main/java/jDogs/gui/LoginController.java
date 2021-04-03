package jDogs.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

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
    void loginButtonClicked(ActionEvent event) {
        GuavaEventBus.INSTANCE.post(new LoginEvent());
    }
}
