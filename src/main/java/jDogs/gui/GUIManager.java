package jDogs.gui;


import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.application.Application;
import jDogs.serverclient.clientside.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager extends Application {
    private Stage primaryStage;
    private static GUIManager instance;
    FXMLLoader lobbyLoader;
    public LobbyController lobbyController;
    private Client client;

    public static void main(String[] args) {
        launch(args);
    }

    //get singleton
    public static GUIManager getInstance() {
        return instance;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        //create the singleton
        instance = this;

        this.primaryStage = primaryStage;

        setLoginScene();
    }

    private void setLoginScene() {

        // activate loginWindow

        // TODO why does it not work:
        //FXMLLoader loginLoader = new FXMLLoader(getClass().getResource(("/resources/loginWindow.fxml")));

        URL url = null;

        try {
            url = Paths.get("src/main/resources/loginWindow.fxml").toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FXMLLoader loginLoader = new FXMLLoader(url);
        Parent root = null;
        try {
            root = loginLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // loginScene
        Scene loginScene = new Scene(root);
        primaryStage.setScene(loginScene);

        //shut down application by closing the window(works for all scenes)
        primaryStage.setOnCloseRequest(e-> System.exit(-1));

        primaryStage.show();
    }


    public void setLobbyScene() {
        String lobbyPath = "src/main/resources/lobbyWindow.fxml";
        // activate Window
        //FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/lobbyWindow.fxml"));
        URL url = null;
        try {
            url = Paths.get(lobbyPath).toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        lobbyLoader = new FXMLLoader(url);
        // get path
        Parent root = null;
        try {
            root = lobbyLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lobbyController = lobbyLoader.getController();
        // lobbyScene
        primaryStage.getScene().setRoot(root);

        lobbyController.displayPendentGameInLobby("Spiel1;Fritz;2;4");
        lobbyController.displayPendentGameInLobby("Spiel2;Fritz;2;4");

        lobbyController.displayPlayersInLobby("Hans");
        lobbyController.displayPlayersInLobby("Fritz");
        lobbyController.displayPlayersInLobby("Josef");









    }


    public void goToLobby(String nickname) {
        //client = new Client();
        setLobbyScene();
    }



    public void startGame() {
        setGameScene();
    }

    private void setGameScene() {

    }

}
