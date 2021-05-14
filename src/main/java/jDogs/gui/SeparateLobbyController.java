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
    void startButtonOnAction(ActionEvent event) {

    }

    @FXML
    void changeButtonOnAction(ActionEvent event) {

        //TODO set labels with names and imageViews of 4 different colored dogs
        //TODO and then let them move if team mode is chosen and 4 players are present
        Polyline polyline = new Polyline();
        polyline.getPoints().addAll(new Double[] {
                 20.0, 20.0,
                30.0, 30.0,
                80.0, 80.0,
                100.0, 100.0,
                140.0, 200.0
                });

        PathTransition pathTransition = new PathTransition();
        pathTransition.setCycleCount(3);
        pathTransition.setDuration(Duration.seconds(1));
        pathTransition.setAutoReverse(true);
        pathTransition.setPath(polyline);
        pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setNode(imageView1);
        pathTransition.play();



    }

    @FXML
    void quitButtonOnAction(ActionEvent event) {


    }

    @FXML
    void sendButtonOnAction(ActionEvent event) {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Polyline polyline = new Polyline();
        polyline.getPoints().addAll(new Double[] {
        //yellow to green
                65.0, 80.0,
                65.0, 213.0,
                60.0, 213.0,
                70.0, 213.0,
                65.0, 213.0,

        });


        PathTransition pathTransition = new PathTransition();
        pathTransition.setCycleCount(1);
        pathTransition.setDuration(Duration.seconds(5));
        pathTransition.setAutoReverse(true);
        pathTransition.setPath(polyline);
        pathTransition.setNode(imageView1);
        pathTransition.play();

        //TODO green to blue
        //TODO blue to red
        //TODO red to yellow
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

