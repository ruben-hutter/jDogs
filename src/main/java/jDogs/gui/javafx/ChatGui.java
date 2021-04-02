package jDogs.gui.javafx;

import jDogs.serverClientEnvironment.clientSide.Client;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class ChatGui extends Application {
    Client client = new Client(this);
    TextField input;


    public static void main(String[] args) {
        launch(args);
    }

    private TextArea messages = new TextArea();

    /**
     *
     * @return the content of the stage GuiChat(i.e. the scene)
     */
    private Parent createContent() {
        messages.setPrefHeight(550);
        input = new TextField();
        Button button = new Button();
        button.setText("Send me");
        button.setOnAction(e -> {
            String string = input.getText();
            input.clear();
            client.sendMessageToServer("PCHT " + string);
            //messages.appendText(string + "\n");
        });

        VBox root = new VBox(30, messages, input);
        root.setPadding(new Insets(10, 50, 50, 50));
        root.getChildren().add(button);
        root.setPrefSize(600,600);
        return root;
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public void displayMessage(String message) {
        System.out.println("displayMessage was used");
        messages.appendText(message + "\n");
    }
}
