package JDogs.GUI.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This is an example JavaFX-Application.
 */
public class GUI extends Application {
    Stage window;
    Scene scene1, scene2, scene3;

    Button button1;
    Button button2;

    /**
     * Launching this method will not work on some platforms.
     * What you should do is to create a separate main class and launch the GUI class from there as is done in {@link Main}
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        window = stage;

        // scene1

        Label label1 = new Label("Welcome to JDogs. Please LogIn.");
        Button button1 = new Button("alright, log me in!");
        //from scene1 to scene2 by window.setScene(scene2)
        button1.setOnAction(e -> window.setScene(scene2));
        //layout1 - children are laid out in vertical column
        VBox layout1 = new VBox(100);
        layout1.getChildren().addAll(label1, button1);
        scene1 = new Scene(layout1, 600, 300);

        /** scene2
         *
         */

        //Button 2
        Button button2 = new Button("let s play a game");
        button2.setOnAction(e -> window.setScene(scene3));
        //label2
        Label label2 = new Label("this is the lobby");
        //layout2
        StackPane layout2 = new StackPane();
        layout2.getChildren().add(button2);
        scene2 = new Scene(layout2, 600, 300);

        /** scene 3
         *
         */

        HBox topMenu = new HBox();
        Button buttonA = new Button("Menu");
        Button buttonB = new Button("Show");
        Button buttonC = new Button("View");
        topMenu.getChildren().addAll(buttonA,buttonB,buttonC);

        VBox leftMenu = new VBox();
        Button buttonD = new Button("D");
        leftMenu.getChildren().add(buttonD);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftMenu);

        scene3 = new Scene(borderPane, 600, 300);







        window.setScene(scene1);
        window.setTitle("JDogs GUI");
        window.show();


        stage.show();
    }



}
