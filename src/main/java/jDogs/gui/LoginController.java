package jDogs.gui;

import animatefx.animation.AnimationFX;
import animatefx.animation.BounceIn;
import animatefx.animation.BounceInDown;
import animatefx.util.ParallelAnimationFX;
import jDogs.serverclient.clientside.Client;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

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

    @FXML
    private ImageView jDogsLogo;

    private MediaPlayer player;

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
           // GUIManager.getInstance().goToLobby(nickname);
            GUIManager.getInstance().startGame(0);
        }
    }

    /**
     * plays "Who let the dogs out" and let the logo bounce
     */
    public void startIntro() {
        BounceIn bounceIn = new BounceIn(jDogsLogo);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> bounceIn.play()),
                new KeyFrame(Duration.ZERO, e -> player.play())
                );
        timeline.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       URL path = getClass().getClassLoader().getResource("music/whoLetTheDogsOut.wav");
        Media media = null;
        try {
            media = new Media(path.toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
        player = new MediaPlayer(media);
    }

}
