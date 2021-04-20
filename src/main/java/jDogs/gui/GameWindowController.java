package jDogs.gui;

import com.sun.javafx.scene.paint.GradientUtils.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;

/**
 * this class represents the gameWindow
 */
public class GameWindowController implements Initializable {

    @FXML
    private SplitPane splitPane;

    @FXML
    private Pane paneForGrid;

    @FXML
    private Pane paneForInfoField;

    @FXML
    private TextField textFieldMakeMove;

    @FXML
    private Button makeMoveButton;

    @FXML
    private TextArea textAreaDisplayMessage;

    @FXML
    private TextField textFieldSendMessage;

    @FXML
    private Button sendButton;

    /**
     * this method receives a move to send to server
     * @param event fires if a piece is moved
     */
    @FXML
    void makeMoveButtonOnAction(ActionEvent event) {

    }

    /**
     * send message to server
     * @param event fires if a message should be sent to server
     */
    @FXML
    void sendButtonOnAction(ActionEvent event) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int boardSize = GUIManager.getInstance().getBoardSize();


        GridPane gridPane = new GridPane();
        gridPane.addRow(18);
        gridPane.addColumn(18);

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 18; j++) {
                gridPane.add(new Rectangle(30,30, Color.AQUAMARINE),i, j);
            }
        }
        paneForGrid.getChildren().add(gridPane);

        //Sphere sphere = new Sphere(10);
        Circle circle1 = new Circle(10);
        Circle circle2 = new Circle(10);
        Circle circle3 = new Circle(10);
        Circle circle4 = new Circle(10);
        Circle circle5 = new Circle(10);
        Circle circle6 = new Circle(10);
        Circle circle7 = new Circle(10);
        Circle circle8 = new Circle(10);
        Circle circle9 = new Circle(10);
        Circle circle10 = new Circle(10);





        gridPane.add(circle1, 0, 0);
        gridPane.add(circle2,1,0);
        gridPane.add(circle3,0,1);
        gridPane.add(circle4,1,1);


    }

    /**
     * a method to update the gui board
     * if updates arrive from server
     */
    public void updateGUIBoard() {

    }
}