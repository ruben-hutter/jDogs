package jDogs.gui;

import jDogs.ClientGame;
import jDogs.board.Board;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ImageView imageViewCard0;


    @FXML
    private FadeTransition fadeTransitionGrid;

    @FXML
    private FadeTransition fadeTransitionCircle1;

    @FXML
    private FadeTransition fadeTransitionCircle2;

    private Group circleGroup;

    private AdaptToGui adaptToGui;

    private boolean jackIsSelected;

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


            if (clickedNode instanceof Circle) {
                System.out.println("entered circle");

                if (jackIsSelected && fadeTransitionCircle1!= null) {
                    System.out.println("entered jack");
                    if (fadeTransitionCircle2 != null) {
                        fadeTransitionCircle2.jumpTo(Duration.seconds(5));
                        fadeTransitionCircle2.stop();
                    }
                    fadeTransitionCircle2 = new FadeTransition(Duration.seconds(0.3), clickedNode);
                    fadeTransitionCircle2.setFromValue(1.0);
                    fadeTransitionCircle2.setToValue(0.0);
                    fadeTransitionCircle2.setCycleCount(Animation.INDEFINITE);
                    fadeTransitionCircle2.play();
                } else {
                    if (fadeTransitionCircle1 != null) {
                        fadeTransitionCircle1.jumpTo(Duration.ZERO);
                        fadeTransitionCircle1.stop();
                        fadeTransitionCircle1 = null;
                    }
                    fadeTransitionCircle1 = new FadeTransition(Duration.seconds(0.9), clickedNode);
                    fadeTransitionCircle1.setFromValue(1.0);
                    fadeTransitionCircle1.setToValue(0.0);
                    fadeTransitionCircle1.setCycleCount(Animation.INDEFINITE);
                    fadeTransitionCircle1.play();
                }
            } else {
                if (clickedNode instanceof Pane) {

                    // stop first field blinking if one clicks another field
                    if (fadeTransitionGrid != null) {
                        fadeTransitionGrid.jumpTo(Duration.ZERO);
                        fadeTransitionGrid.stop();
                    }
                    Integer colIndex = GridPane.getColumnIndex(clickedNode);
                    Integer rowIndex = GridPane.getRowIndex(clickedNode);
                    System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
                    int clicked = event.getClickCount();
                    fadeTransitionGrid = new FadeTransition(Duration.seconds(0.9), clickedNode);
                    fadeTransitionGrid.setFromValue(1.0);
                    fadeTransitionGrid.setToValue(0.0);
                    fadeTransitionGrid.setCycleCount(Animation.INDEFINITE);
                    fadeTransitionGrid.play();
                }
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jackIsSelected = false;
        setOnHome();
        makeSingleMove(0,0,7);
        String path1 = "src/main/resources/4C.png";

        URL url = getUrl(path1);

        Image image1 = new Image(url.toString());

        imageViewCard0.setImage(image1);
        imageViewCard0.toFront();

        leftBoardSide.getChildren().add(new ImageView(image1));

    }

    private URL getUrl(String path1) {
        // activate Window
        //FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/lobbyWindow.fxml"));
        URL url = null;
        try {
            return Paths.get(path1).toUri().toURL();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
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
                circle.setId("" + (count + 1));
                System.out.println("circle ids " + (count + 1));
                gridPane.add(circle, homeArray[count].getX(), homeArray[count].getY());
                count++;
            }
        }
    }

    public void makeSingleMove(int playerNr, int pieceID, int newPosition) {
        String circleID ="" + ((playerNr + 1) * (pieceID + 1));
        System.out.println("circle ID " + circleID);
        FieldOnBoard newPos = adaptToGui.getTrack(newPosition);

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                if (circle.getId().equals(circleID)) {
                    gridPane.getChildren().remove(circle);
                    System.out.println("removed circle");
                    gridPane.add(circle,newPos.getX(), newPos.getY());
                    break;
                }
            }
        }
    }

    public void sendHome() {

    }

    public void setJack() {

    }
}
