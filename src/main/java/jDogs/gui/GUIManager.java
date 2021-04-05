package jDogs.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.Action;

public class GUIManager extends Application {

    Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        //activate LoginEventHandler
        LoginEventHandler.INSTANCE.setUp(this);

        // activate loginWindow

        // get path
        URL url = Paths.get("src/main/java/resources/loginWindow.fxml").toUri().toURL();

        // root
        FXMLLoader loginLoader = new FXMLLoader();
        Parent root = loginLoader.load(url);

        // loginScene
        Scene loginScene = new Scene(root);
        primaryStage.setScene(loginScene);

        primaryStage.show();
    }

    public void goToLobby(String nickname) {
        System.out.println("your nickname is:  " + nickname);

        // activate loginWindow

        // get path
        URL url = null;
        try {
            url = Paths.get("src/main/java/resources/lobbyWindow.fxml").toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        LobbyController lobbyController = new LobbyController();

        FXMLLoader lobbyLoader = new FXMLLoader(url);
        lobbyLoader.setController(lobbyController);
        Parent root = null;
        try {
            root = lobbyLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // lobbyScene
        Scene lobbyScene = new Scene(root);


        primaryStage.setScene(lobbyScene);

        primaryStage.show();

        Platform.runLater(new Runnable() {
                              @Override
                              public void run() {
                                  lobbyController.refreshTextArea();
                                  lobbyController.displayMessage(nickname + "sent this message from GUI");
                              }
        });





    }






}
