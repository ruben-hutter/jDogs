package jDogs.gui;

import java.net.URL;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //activate LoginEventListener


        LoginEventListener loginEventListener = new LoginEventListener(this);

        GuavaEventBus.INSTANCE.addToEventBus(loginEventListener);

        //get path
        URL url = Paths.get("src/main/java/resources/loginWindow.fxml").toUri().toURL();

        //root
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(url);

        //scene1
        Scene scene1 = new Scene(root);
        primaryStage.setScene(scene1);


        primaryStage.show();
    }

    public void goToLobby() {

       System.out.println("arrived at lobby");


    }
}
