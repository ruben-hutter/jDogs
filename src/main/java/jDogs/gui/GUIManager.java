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

/**
 * this class handles all the relation between gui and client
 * every window is called from here
 */

public class GUIManager extends Application {
    private Stage primaryStage;
    private static GUIManager instance;
    FXMLLoader lobbyLoader;
    public LobbyController lobbyController;
    FXMLLoader gameLoader;
    public GameWindowController gameWindowController;
    private Client client;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * this is important to call this object as a singleton
     * @return static instance
     */
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

    /**
     * sets up the login window
     */
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

    /**
     * sets up the lobby window
     */
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



    }

    /**
     * this method is called to start client and lobby
     * @param nickname name entered in the login window
     */
    public void goToLobby(String nickname) {
        client = new Client();
        client.setNickname(nickname);
        //get public lobby guests from server
        client.sendMessageToServer("LPUB");

        //get all open games from server
        client.sendMessageToServer("SESS");

        setLobbyScene();
    }


    /**
     * starts the game
     */
    public void startGame() {
        setGameScene();
    }

    /**
     * sets up the game window
     */
    private void setGameScene() {

        String lobbyPath = "src/main/resources/gameWindow.fxml";
        // activate Window
        //FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/lobbyWindow.fxml"));
        URL url = null;
        try {
            url = Paths.get(lobbyPath).toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        gameLoader = new FXMLLoader(url);
        // get path
        Parent root = null;
        try {
            root = gameLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameWindowController = gameLoader.getController();
        // lobbyScene
        primaryStage.getScene().setRoot(root);

    }

}
