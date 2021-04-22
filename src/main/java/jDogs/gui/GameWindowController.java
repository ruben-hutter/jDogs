package jDogs.gui;

import com.sun.javafx.scene.paint.GradientUtils.Point;
import jDogs.Alliance_4;
import jDogs.ClientGame;
import jDogs.board.Board;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


/**
 * this class represents the gameWindow
 */
public class GameWindowController implements Initializable {


    private static final int CIRCLE_RADIUS = 10;
    AdaptToGui adaptToGui = new AdaptToGui();

    private ArrayList<Circle> circles;

    @FXML
    private ImageView imageBackground;

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

    @FXML
    private GridPane gridPane;
    @FXML
    private Circle[][] circlePieces;
    private FieldOnBoard[] homeArray;

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
        //int boardSize = GUIManager.getInstance().getBoardSize();
        //circlePieces = createCircles();


        //set up gridPane
        gridPane = new GridPane();
        //gridPane.setMaxSize(600,600);
        gridPane.getBoundsInParent();
        gridPane.setMinSize(600,600);
        gridPane.addRow(18);
        gridPane.addColumn(18);
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        double valX = 0;
        double valY = 0;


        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
             Rectangle rectangle = new Rectangle(20, 20 , Color.DEEPPINK);
                //rectangle.setLayoutX(valX++);
                //rectangle.setLayoutY(valY);
             //rectangle.setY(i);
             gridPane.add(rectangle,j, i);
            }
        }
        circles = createCircles();
        Group groupCircle = new Group();
        for (Circle circle : circles) {
            groupCircle.getChildren().add(circle);
        }
        paneForGrid.getChildren().addAll(gridPane, groupCircle);
        setCirclesToHome();

        gridPane.prefHeightProperty().bind(paneForGrid.heightProperty());
        gridPane.prefWidthProperty().bind(paneForGrid.widthProperty());
    }


    private void setCirclesToHome() {
        homeArray = AdaptToGui.getInstance().getHomeFieldArray();

        int count = 0;
        for (Circle circle : circles) {
            circle.setCenterY(homeArray[count].getY()*25);
            circle.setCenterX(homeArray[count++].getX()*35);
            if (homeArray.length < count) {
                System.err.println("homeArray too short for all circles");
                break;
            }
        }
    }


    private ArrayList<Circle> createCircles() {
        ArrayList<Circle> newCircles =  new ArrayList<>();
        for (ColorTokens colorTokens : ColorTokens.values()) {

            for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
                newCircles.add(new Circle(10, colorTokens.getColor()));
            }
        }
        return newCircles;
    }




    /**
     * a method to update the gui board
     * if updates arrive from server
     */
    public void updateGUIBoard() {

    }

    public void updateToken(int arrayLevel, int pieceId) {


       //get right token on board -> create array
        // move tokens with polyline from one field to another
     //  paneForGrid.getChildren().remove(oldFieldOnBoard.getX(),oldFieldOnBoard.getY());

    }
}