package jDogs.gui;


import jDogs.serverclient.clientside.Client;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager extends Application {
    private Client client;
    private Stage primaryStage;
    private boolean isInLobby = false;
    LobbyController lobbyController;

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

        setUpClient(nickname);

        // activate lobbyWindow

        // get path
        URL url = null;
        try {
            url = Paths.get("src/main/java/resources/lobbyWindow.fxml").toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        lobbyController = new LobbyController();

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

        isInLobby = true;

        Platform.runLater(new Runnable() {
                              @Override
                              public void run() {
                                  lobbyController.displayPCHTmsg(nickname + "  sent this message from GUI");
                              }
        });





    }

    public void displayPCHTmsg(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lobbyController.displayPCHTmsg(message);
            }
        });
    }

    public void displayWCHTmsg (String message) {
        if (isInLobby) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lobbyController.displayWCHTmsg(message);
                }
            });
        }
    }



    void setUpClient(String nickname) {
       // this.client = new Client(this);
        // this.client.setNickname(nickname);
    }

}
