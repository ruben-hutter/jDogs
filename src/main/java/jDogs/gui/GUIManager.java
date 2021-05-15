package jDogs.gui;


import java.io.IOException;
import java.util.List;
import javafx.animation.PathTransition;
import javafx.application.Application;
import jDogs.serverclient.clientside.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * this class handles all the relation between gui and client
 * every window is called from here
 */
public class GUIManager extends Application {
    private Stage primaryStage;
    private static GUIManager instance;
    private FXMLLoader lobbyLoader;
    public PublicLobbyController_old lobbyController;
    FXMLLoader gameLoader;
    private Client client;
    public GameWindowController gameWindowController;
    private boolean isPlaying;
    private boolean teamMode;
    private LoginController loginController;
    private SeparateLobbyController separateLobbyController;
    private FXMLLoader separateLobbyLoader;
    private Parent root;

    /**
     * start method
     * @param args not used
     */
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
       // setLoginScene();
        setSeparateLobbyScene();

    }

    /**
     * sets up the login window
     */
    private void setLoginScene() {

        // activate loginWindow

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/loginWindow.fxml"));
        root = null;
        try {
            root = loginLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // loginScene
        Scene loginScene = new Scene(root);

        loginController = loginLoader.getController();

        primaryStage.setScene(loginScene);

        //shut down application by closing the window(works for all scenes)
        primaryStage.setOnCloseRequest(e-> System.exit(-1));
        primaryStage.show();
        loginController.startIntro();
    }

    /**
     * sets up the lobby window
     */
    public void setLobbyScene() {
        isPlaying = false;

        // activate Window
        lobbyLoader = new FXMLLoader(getClass().getResource("/lobbyWindow_old.fxml"));

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
     * this method is called to return to publicLobby
     */
    public void returnToPubLobby() {
        client.sendMessageToServer("LPUB");
        client.sendMessageToServer("SESS");
    }

    /**
     * start separate lobby after joining open game
     */
    public void setSeparateLobbyScene() {
        // activate Window
        separateLobbyLoader = new FXMLLoader(getClass().getResource("/separateLobby.fxml"));

        root = null;
        try {
            root = separateLobbyLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene sepLobbyScene = new Scene(root);

        separateLobbyController = separateLobbyLoader.getController();
        // lobbyScene
        //primaryStage.getScene().setRoot(root);
        primaryStage.setScene(sepLobbyScene);
        primaryStage.show();
    }

    /**
     * starts the game
     * @param intTeamMode 1 for teamMode, else single player
     */
    public void startGame(int intTeamMode) {
        this.teamMode = intTeamMode == 1;
        isPlaying = true;
        setGameScene();
    }

    /**
     * teamMode: play in teams or not
     * @return true, if playing in teams
     */
    public boolean isTeamMode() {
        return teamMode;
    }

    /**
     * sets up the game window
     */
    private void setGameScene() {

        gameLoader = new FXMLLoader(getClass().getResource("/gameWindow.fxml"));
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

    /**
     * display PCHT message in gui
     * @param message from client
     */
    public void sendPCHTToGui (String message) {
        if (isPlaying) {
            gameWindowController.displayPCHTmsg (message);
        } else {
            lobbyController.displayPCHTmsg (message);
        }
    }

    /**
     * display LCHT message in gui
     * @param message from participants
     */
    public void sendLCHTToGui (String message) {
        if (isPlaying) {
            gameWindowController.displayLCHTmsg (message);
        } else {
            lobbyController.displayPCHTmsg (message);
        }
    }

    /**
     * display info in gui
     * @param info INFO from server
     */
    public void sendINFOtoGui (String info) {
        if (isPlaying) {
            gameWindowController.displayInfoFromServer(info);
        } else {
            lobbyController.displayInfomsg(info);
        }
    }
}
