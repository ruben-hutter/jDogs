package jDogs.gui;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
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
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;

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
    private int count;
    private Label[] continuousLabels;
    private Label[] crossLabels;
    private Double[][] crossLines;
    private Double[][] continuousLines;

    @FXML
    void startButtonOnAction(ActionEvent event) {

    }

    @FXML
    void changeButtonOnAction(ActionEvent event) {
        int helper = 0;
        if (count % 2 == 0) {
            for (Double[] line : crossLines) {
                Polyline polyline = new Polyline();
                polyline.getPoints().addAll(line
            );

            PathTransition pathTransition = new PathTransition();
            pathTransition.setCycleCount(1);
            pathTransition.setDuration(Duration.seconds(5));
            pathTransition.setAutoReverse(true);
            pathTransition.setPath(polyline);
            pathTransition.setNode(crossLabels[helper]);
            helper++;
            pathTransition.play();
        }
    } else {
        for(Double[] line : continuousLines) {
            Polyline polyline = new Polyline();
            polyline.getPoints().addAll(line
            );

            PathTransition pathTransition = new PathTransition();
            pathTransition.setCycleCount(1);
            pathTransition.setDuration(Duration.seconds(10));
            pathTransition.setAutoReverse(true);
            pathTransition.setPath(polyline);
            pathTransition.setNode(continuousLabels[helper]);
            helper++;
            pathTransition.play();
        }
    }
    count++;
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
        continuousLabels = new Label[] {label1, label2, label3, label4};
        crossLabels = new Label[] {label1, label3, label2, label4};
        continuousLines = new Double[][] {{10.0,10.0,23.5,155.0}, {10.0, 10.0,
                277.0, 10.0}, { 10.0, 10.0,
                23.0, -135.0}, { 10.0, 10.0,
                -231.0, 10.0}};
        crossLines = new Double[][] {
                {10.0, 10.0,
                        278.0, 155.0,},
                {10.0, 10.0,
                        -231.0, -136.0,},
                {10.0, 10.0,
                        277.3, -136.0},
                {10.0, 10.0,
                        -231.0, 155.0}
        };

    /*
    //yellow to green
                23.5, 10.0,
                23.5, 155.0,

                 //green to blue
                24.2, 100.0,
                277.0, 10.0,

                   //blue to red
               10.0, 70.0,
                23.0, -135.0,

                 //red to yellow
               10.0, 10.0,
                -30.0, 10.0,
                -231.0, 10.0,
     */

    }
    private Circle[] createCircles() {
        Circle[] circles = new Circle[4];
        int count = 0;
        for (ColorFXEnum color : ColorFXEnum.values()) {
            circles[count]= new Circle(RADIUS_CIRCLE,color.getColor());
            count++;
        }
        return circles ;
    }
}

