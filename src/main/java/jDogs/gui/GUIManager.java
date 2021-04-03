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
        //get path
        URL url = Paths.get("src/main/resources/loginWindow.fxml").toUri().toURL();

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(url);

        Scene scene1 = new Scene(root);
        primaryStage.setScene(scene1);

        LoginController loginController = loader.getController();


        primaryStage.show();
    }
}
