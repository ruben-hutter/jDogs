package jDogs.gui;


import java.io.IOException;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager extends Application {
    private Stage primaryStage;
    private boolean isInLobby = false;
    LobbyController lobbyController;
    private static GUIManager instance;

    public static void main(String[] args) {
        launch(args);
    }
    //get the singleton
    public static GUIManager getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //create the singleton
        instance = this;

        this.primaryStage = primaryStage;


        //activate LoginEventHandler
        LoginEventHandler.INSTANCE.setUp(this);

        // activate loginWindow

        // root
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource(("/loginWindow.fxml")));
        Parent root = loginLoader.load();

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


        lobbyController = lobbyLoader.getController();

        // lobbyScene
        primaryStage.getScene().setRoot(root);

        isInLobby = true;
    }



    void setUpClient(String nickname) {
       // this.client = new Client(this);
        // this.client.setNickname(nickname);
    }

    public void goToLobby(String nickname) {
        System.out.println("arrived");
    }
}
