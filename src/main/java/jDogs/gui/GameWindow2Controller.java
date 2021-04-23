package jDogs.gui;

import jDogs.board.Board;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class GameWindow2Controller implements Initializable {

    private static final double RADIUS_CIRCLE = 10;
    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem statMenuItem;

    @FXML
    private MenuItem retToLoMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private Pane leftBoardSide;

    @FXML
    private Pane rightBoardSide;

    @FXML
    private VBox vBoxGridWrapper;

    @FXML
    private GridPane gridPane;

    @FXML
    private HBox playerDisplay;

    @FXML
    private FadeTransition fadeTransitionGrid;

    private Group circleGroup;

    private AdaptToGui adaptToGui;

    @FXML
    void exitMenuOnAction(ActionEvent event) {

    }

    @FXML
    void reToLoMenuOnAction(ActionEvent event) {

    }

    @FXML
    void statisticMenuOnAction(ActionEvent event) {

    }
    @FXML
    void onMouseClickGrid(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != gridPane) {
            // stop first field blinking if one clicks another field
            if (fadeTransitionGrid != null) {
                fadeTransitionGrid.stop();
            }
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
            int clicked = event.getClickCount();
            fadeTransitionGrid = new FadeTransition(Duration.seconds(0.9), clickedNode);
            fadeTransitionGrid.setFromValue(0.0);
            fadeTransitionGrid.setToValue(1.0);
            fadeTransitionGrid.setCycleCount(Animation.INDEFINITE);
            fadeTransitionGrid.play();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

       setOnHome();
    }

    /**
     * set all pieces on their respective home position
     */
    private void setOnHome() {

        adaptToGui = new AdaptToGui();
        int count = 0;

        FieldOnBoard[] homeArray = adaptToGui.getHomeFieldArray();
        for (ColorTokens colorTokens: ColorTokens.values()) {
            Color color = colorTokens.getColor();

            for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
                Circle circle = new Circle(RADIUS_CIRCLE, colorTokens.getColor());
                circle.setId("" + count);
                gridPane.add(circle, homeArray[count].getX(), homeArray[count].getY());
                count++;
            }
        }
    }
}
