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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import jfxtras.labs.util.event.MouseControlUtil;

public class SeparateLobbyController implements Initializable{

    private static final int RADIUS_CIRCLE = 10;
    @FXML
    private ImageView imageView1;

    @FXML
    private Circle circle1;
    @FXML
    private Circle circle2;
    @FXML
    private Circle circle3;
    @FXML
    private Circle circle4;

    @FXML
    private TableView<?> tableViewPlayers;

    @FXML
    private Pane paneID;

    @FXML
    private Button joinButton;

    @FXML
    private Button createButton;

    @FXML
    private Button joinButton1;

    @FXML
    private TextArea displayTextArea;

    @FXML
    private TextField sendTextField;

    @FXML
    private Button sendButton;

    @FXML
    private MenuBar menuBarTop;

    @FXML
    void createButtonOnAction(ActionEvent event) {

    }

    @FXML
    void joinButtonOnAction(ActionEvent event) {

        //MouseControlUtil.makeDraggable(circle);
        Path path = new Path();
        path.getElements().add(new MoveTo(10,110));
        path.getElements().add(new CubicCurveTo(0,20,20,20,20,20));
        //path.getElements().add(new CubicCurveTo(20,20,20,20,20,20));
        //TODO set labels with names and imageViews of 4 different colored dogs
        //TODO and then let them move if team mode is chosen and 4 players are present

        PathTransition pathTransition = new PathTransition();
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        pathTransition.setDuration(Duration.INDEFINITE);
        pathTransition.setAutoReverse(false);
        pathTransition.setPath(path);
        pathTransition.setNode(imageView1);
        pathTransition.play();
    }

    @FXML
    void sendButtonOnAction(ActionEvent event) {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

