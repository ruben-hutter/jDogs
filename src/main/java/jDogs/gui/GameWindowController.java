package jDogs.gui;

import com.sun.javafx.scene.paint.GradientUtils.Point;
import jDogs.ClientGame;
import jDogs.board.Board;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
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
import javafx.util.Duration;

/**
 * this class represents the gameWindow
 */
public class GameWindowController implements Initializable {

    Circle[][] tokens;

    private static final int CIRCLE_RADIUS = 10;
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


        gridPane = new GridPane();
        gridPane.addRow(18);
        gridPane.addColumn(18);

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 18; j++) {
                gridPane.add(new Rectangle(30,30, Color.BISQUE),i, j);
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
        Circle circle8 = new Circle(10,Color.GREEN);
        Circle circle9 = new Circle(10);
        Circle circle10 = new Circle(10);
        gridPane.add(circle8, 10, 10);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(circle8);
        pathTransition.setDuration(Duration.seconds(3));
        pathTransition.setPath(new Circle(30));
        pathTransition.setCycleCount(1);
        pathTransition.play();


        gridPane.add(circle1, 0, 0);
        gridPane.add(circle2,1,0);
        gridPane.add(circle3,0,1);
        gridPane.add(circle4,1,1);

       if(paneForGrid.getChildren().get(0).equals(gridPane)) {
            System.out.println("true");
        }

    for (int i = 0; i < gridPane.getChildren().size(); i++) {
        if (gridPane.getChildren().get(i).equals(circle1)) {
            System.out.println("true " + i);
            gridPane.getChildren().remove(circle1);
        }
    }

    }
/*
    private Token[][] createCircles() {
        tokens = new Token[ClientGame.getInstance().getNumPlayers()][Board.NUM_HOME_TILES];

       for (int i = 0; i < ClientGame.getInstance().getNumPlayers(); i++) {

           for (int j = 0; j < Board.NUM_HOME_TILES; j++) {
               tokens[i][j] = new Token();
           }
       }
       return tokens;
    }

 */



    /**
     * a method to update the gui board
     * if updates arrive from server
     */
    public void updateGUIBoard() {

    }

    public void updateToken(int arrayLevel, int pieceId) {
       FieldOnBoard oldFieldOnBoard = ClientGame.getInstance().getGuiTokens()[arrayLevel][pieceId].getOldField();
       FieldOnBoard newFieldOnBoard = ClientGame.getInstance().getGuiTokens()[arrayLevel][pieceId].getNewField();

       //get right token on board -> create array
        // move tokens with polyline from one field to another
       paneForGrid.getChildren().remove(oldFieldOnBoard.getX(),oldFieldOnBoard.getY());

    }
}