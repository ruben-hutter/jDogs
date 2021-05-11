package jDogs.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * this class represents the login window
 */
public class LoginController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private TextField textField;

    @FXML
    private Button cancelButton;

    /**
     * cancel the game
     * @param event fires if clients cancels the game
     */
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        //Shutdown
        System.exit(-1);
    }

    /**
     * send a nickname to gui
     * @param event fires if a name
     *             with length > 3 is typed in
     */
    @FXML
    void loginButtonOnAction(ActionEvent event) {
        String nickname;
        nickname = textField.getText();
        if (nickname.isEmpty() || nickname.isBlank() || nickname.length() < 3) {
            //open AlertWindow here
        } else {
            System.out.println(nickname);
            GUIManager.getInstance().goToLobby(nickname);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
