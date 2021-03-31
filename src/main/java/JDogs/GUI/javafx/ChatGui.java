package JDogs.GUI.javafx;

import JDogs.ServerClientEnvironment.ClientSide.Client;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
     * @return the content of the stage GuiChat
     */
    private Parent createContent() {
        messages.setPrefHeight(550);
        input = new TextField();
        input.setOnAction(event -> {
            String string = input.getText();
            input.clear();

            //messages.appendText((string + "\n"));

            client.sendMessageToServer("PCHT " + string);
        });

        VBox root = new VBox(20, messages, input);
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
