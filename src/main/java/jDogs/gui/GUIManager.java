package jDogs.gui;


import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import jDogs.serverclient.clientside.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager extends Application {
    private Stage primaryStage;
    private boolean isInLobby = false;
    private static GUIManager instance;
    //private Client client;

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

        //activate LoginEventHandler
        LoginEventHandler.INSTANCE.setUp(this);

        // activate loginWindow

        // root
        //FXMLLoader loginLoader = new FXMLLoader(getClass().getResource(("/loginWindow.fxml")));
        URL url = null;
        try {
            url = Paths.get("src/sample/resources/loginWindow.fxml").toUri().toURL();
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

        primaryStage.show();
    }


    public void setScene(String url) {

        // activate lobbyWindow
        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/lobbyWindow.fxml"));
        // get path
        Parent root = null;

        try {
            root = lobbyLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LobbyController lobbyController = lobbyLoader.getController();



        // lobbyScene
        primaryStage.getScene().setRoot(root);


        isInLobby = true;
    }


    public void goToLobby(String nickname) {
        System.out.println("arrived");
    }
}
