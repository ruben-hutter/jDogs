package jDogs.gui;
import animatefx.animation.AnimationFX;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import animatefx.animation.Flash;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;

public class SeparateLobbyController implements Initializable{

    private static final int RADIUS_CIRCLE = 10;
    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private ImageView imageView4;


    @FXML
    private TableView<?> tableViewPlayers;
    @FXML
    private Button quitButton;
    @FXML
    private Button changeButton;
    @FXML
    private Button startButton;
    @FXML
    private TextArea displayTextArea;
    @FXML
    private TextField sendTextField;
    @FXML
    private Button sendButton;

    @FXML
    private MenuBar menuBarTop;

    @FXML
    GridPane gridSeparateLobby;

    private int count;
    private Label[] labels;
    private Double[][] lines;
    private ImageView[] imageViews;

    @FXML
    void startButtonOnAction(ActionEvent event) {
        for (Label label : labels) {
            System.out.println(label.toString() + label.getLayoutX() + " " +  label.getLayoutY());
        }
    }

    @FXML
    void changeButtonOnAction(ActionEvent event) {
        for (Label label : labels) {
            System.out.println(label.getText() + " " + label.getLayoutX() + " " + label.getLayoutY());
        }
        int helper = 0;
        if (count % 2 == 1) {
            changeCrossLabels();
            for (Double[] line : lines) {
                Polyline polyline = new Polyline();
                polyline.getPoints().addAll(line
            );

            PathTransition pathTransition = new PathTransition();
            pathTransition.setCycleCount(1);
            pathTransition.setDuration(Duration.seconds(3));
            pathTransition.setAutoReverse(true);
            pathTransition.setPath(polyline);
            pathTransition.setNode(labels[helper]);
            pathTransition.play();
                System.out.println("helper " + helper + " label " + labels[helper].getText());
            labels[helper].setLayoutX(line[2]);
            labels[helper].setLayoutY(line[3]);
                helper++;

            }
    } else {
            changeContinuousLabels();
            for(Double[] line : lines) {
            Polyline polyline = new Polyline();
            polyline.getPoints().addAll(line
            );
            PathTransition pathTransition = new PathTransition();
            pathTransition.setCycleCount(1);
            pathTransition.setDuration(Duration.seconds(3));
            pathTransition.setAutoReverse(true);
            pathTransition.setPath(polyline);
            pathTransition.setNode(labels[helper]);
            System.out.println(labels[helper].toString() +
                    " pos x " + labels[helper].getLayoutX() + " y " +
                    labels[helper].getLayoutY());
            pathTransition.play();
            labels[helper].setLayoutX(line[2]);
            labels[helper].setLayoutY(line[3]);
            helper++;
        }
    }
    startFlash();
    count++;
    }

    /**
     * flash the labels
     */
    private void startFlash() {
        for (Label label : labels) {
            new Flash(label).setCycleCount(2).setDelay(Duration.seconds(3)).play();
        }
    }


    private void changeCrossLabels() {
        Label[] newLabels = new Label[4];
        newLabels[0] = labels[0];
        newLabels[1] = labels[2];
        newLabels[2] = labels[1];
        newLabels[3] = labels[3];
        labels = newLabels;
    }

    /**
     * updates the list after a continuous change of teams
     */
    private void changeContinuousLabels() {
        Label[] newLabels = new Label[4];
        int helper = 0;
        for (int i = 3; i < labels.length; i++) {
            newLabels[helper] = labels[i];
            helper++;
        }
       for (int i = 0; i < labels.length - 1; i++) {
           newLabels[helper] = labels[i];
           helper++;
       }
       labels = newLabels;
    }

    @FXML
    void quitButtonOnAction(ActionEvent event) {
    }

    @FXML
    void sendButtonOnAction(ActionEvent event) {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        count = 0;
        labels = new Label[4];
        int helper = 1;
        for (int i = 0 ; i < 4; i++) {
            Label label = new Label();
            label.setId("" + helper);
            label.setText("hell " + helper);
            label.setVisible(true);
            gridSeparateLobby.getChildren().add(label);
            labels[helper - 1] = label;
            helper++;
        }

        lines = new Double[][] {{0.0,0.0,130.0,0.0}, {0.0, 0.0,
                130.0, 200.0}, { 0.0, 0.0,
                400.0, 200.0}, { 0.0, 0.0,
                400.0, 0.0}};



        setOnStartPosition();
    }

    /**
     * sets the labels on start position
     */
    private void setOnStartPosition() {
        int helper = 0;
        for(Double[] line : lines) {
            Polyline polyline = new Polyline();
            polyline.getPoints().addAll(line
            );
            PathTransition pathTransition = new PathTransition();
            pathTransition.setCycleCount(1);
            pathTransition.setDuration(Duration.seconds(3));
            pathTransition.setAutoReverse(false);
            pathTransition.setPath(polyline);
            pathTransition.setNode(labels[helper]);
            pathTransition.play();
            labels[helper].setLayoutX(line[2]);
            labels[helper].setLayoutY(line[3]);
            helper++;
        }
    }
}

