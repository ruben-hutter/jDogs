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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlendMode;
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
    private ImageView[] allCardImageViews;
    private AdaptToGui adaptToGui;
    private String[] cardArray;
    private String cardClicked;
    private Integer colIndexCircle2;
    private Integer rowIndexCircle2;
    private Integer rowIndexCircle1;
    private Integer colIndexCircle1;
    private Integer colIndexField;
    private Integer rowIndexField;

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
    private Button makeMoveButton;


    @FXML
    private ImageView imageViewCard1;

    @FXML
    private ImageView imageViewCard3;

    @FXML
    private ImageView imageViewCard0;

    @FXML
    private ImageView imageViewCard4;

    @FXML
    private ImageView imageViewCard5;

    @FXML
    private ImageView imageViewCard2;

    @FXML
    private FadeTransition fadeTransitionGrid;

    @FXML
    private FadeTransition fadeTransitionCircle1;

    @FXML
    private FadeTransition fadeTransitionCircle2;

    @FXML
    private FadeTransition fadeTransitionCard;

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
    void onMouseClick0(MouseEvent event) {
        if (cardArray.length > 0 && cardArray[0] != null) {
            startFadeTransitionCard(imageViewCard0);
            cardClicked = cardArray[0];

        }
        System.out.println("clicked card 0");
    }


    @FXML
    void onMouseClick1(MouseEvent event) {
        if (cardArray.length > 1 && cardArray[1] != null) {
            startFadeTransitionCard(imageViewCard1);
            cardClicked = cardArray[1];

        }
        System.out.println("clicked card 1");

    }

    @FXML
    void onMouseClick2(MouseEvent event) {
        if (cardArray.length > 2 && cardArray[2] != null) {
            startFadeTransitionCard(imageViewCard2);
            cardClicked = cardArray[2];

        }
        System.out.println("clicked card 2");
    }

    @FXML
    void onMouseClick3(MouseEvent event) {
        if (cardArray.length > 3 && cardArray[3] != null) {
            startFadeTransitionCard(imageViewCard3);
            cardClicked = cardArray[3];

        }
        System.out.println("clicked card 3");

    }

    @FXML
    void onMouseClick4(MouseEvent event) {
        if (cardArray.length > 4 && cardArray[4] != null) {
            startFadeTransitionCard(imageViewCard4);
            cardClicked = cardArray[4];

        }
        System.out.println("clicked card 4");

    }

    @FXML
    void onMouseClick5(MouseEvent event) {
        if (cardArray.length > 5 && cardArray[5] != null) {
            startFadeTransitionCard(imageViewCard5);
            cardClicked = cardArray[5];

        }
        System.out.println("clicked card 5");

    }


    @FXML
    void onMouseClickGrid(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        //TODO after move sent set cardClicked to null
        //TODO if JOKE is chosen, find solution on GUI to transform to desired card
        if (clickedNode != gridPane && cardClicked != null) {


            if (clickedNode instanceof Circle) {
                System.out.println("entered circle");

                if (cardClicked.equals("JACK") && fadeTransitionCircle1!= null) {
                    System.out.println("entered jack");
                    if (fadeTransitionCircle2 != null) {
                        fadeTransitionCircle2.jumpTo(Duration.seconds(5));
                        fadeTransitionCircle2.stop();
                        fadeTransitionCircle2 = null;
                    }
                    fadeTransitionCircle2 = new FadeTransition(Duration.seconds(0.3), clickedNode);
                    fadeTransitionCircle2.setFromValue(1.0);
                    fadeTransitionCircle2.setToValue(0.0);
                    fadeTransitionCircle2.setCycleCount(Animation.INDEFINITE);
                    fadeTransitionCircle2.play();
                    colIndexCircle2 = GridPane.getColumnIndex(clickedNode);
                    rowIndexCircle2 = GridPane.getRowIndex(clickedNode);
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
                    colIndexCircle1 = GridPane.getColumnIndex(clickedNode);
                    rowIndexCircle1 = GridPane.getRowIndex(clickedNode);
                }
            } else {
                if (clickedNode instanceof Pane) {

                    // stop first field blinking if one clicks another field
                    if (fadeTransitionGrid != null) {
                        fadeTransitionGrid.jumpTo(Duration.ZERO);
                        fadeTransitionGrid.stop();
                        fadeTransitionGrid = null;
                    }
                    colIndexField = GridPane.getColumnIndex(clickedNode);
                    rowIndexField = GridPane.getRowIndex(clickedNode);

                    fadeTransitionGrid = new FadeTransition(Duration.seconds(0.9), clickedNode);
                    fadeTransitionGrid.setFromValue(1.0);
                    fadeTransitionGrid.setToValue(0.0);
                    fadeTransitionGrid.setCycleCount(Animation.INDEFINITE);
                    fadeTransitionGrid.play();
                }
            }
        }
    }


    @FXML
    void makeMoveButtonOnAction(ActionEvent event) {
        if (cardClicked != null) {
            if (colIndexCircle1 != -1 && rowIndexCircle1 != -1) {
                if (cardClicked.equals("JACK") && colIndexCircle2 != -1 && rowIndexCircle2 != -1) {
                    System.out.println("jack move sent");
                } else if (colIndexField != -1 && rowIndexCircle1 != -1) {
                    System.out.println("simple move sent");

                }
            }
        }

    }

    private void startFadeTransitionCard(ImageView imageViewBlink) {
        if (fadeTransitionCard != null) {
            fadeTransitionCard.jumpTo(Duration.seconds(5));
            fadeTransitionCard.stop();
            fadeTransitionCard = null;
        }
        fadeTransitionCard = new FadeTransition(Duration.seconds(0.9), imageViewBlink);
        fadeTransitionCard.setFromValue(1.0);
        fadeTransitionCard.setToValue(0.0);
        fadeTransitionCard.setCycleCount(Animation.INDEFINITE);
        fadeTransitionCard.play();
        System.out.println("FADE transition now");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOnHome();
        makeSingleMove(0,0,15);
        setAllCardImageViews();


        cardArray = new String[6];
        String path1 = "src/main/resources/4C.png";


        URL url = getUrl(path1);

        Image image1 = new Image(url.toString());

        imageViewCard0.setImage(image1);
        imageViewCard1.setImage(image1);
        imageViewCard2.setImage(image1);
        imageViewCard3.setImage(image1);
        imageViewCard4.setImage(image1);
        imageViewCard5.setImage(image1);
        String[] cardddss = new String[]{"ACEE", "KING", "THRE", "TWOO", "NINE", "FOUR"};
        //setCardArray(cardddss);
    }

    /**
     * set up an array to get wanted imageView by number
     */
    private void setAllCardImageViews() {
        allCardImageViews = new ImageView[6];

        allCardImageViews[0] = imageViewCard0;
        allCardImageViews[1] = imageViewCard1;
        allCardImageViews[2] = imageViewCard2;
        allCardImageViews[3] = imageViewCard3;
        allCardImageViews[4] = imageViewCard4;
        allCardImageViews[5] = imageViewCard5;
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

    /**
     * client game sends a move (which was sent from server) to the gui
     * @param playerNr yellow = player1, green = player2, blue = player3, red = player4
     * @param pieceID 0,1,2,3
     * @param newPosition position nr on server
     */
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

    /**
     * client game sends the cards for this round to the gui here
     * @param cards these cards are the hand for this round
     */
    public void setCardArray(String[] cards) {
        this.cardArray = cards;
        int count = 0;
        for(String card : cardArray) {
           URL url = getImageUrl(card);

           Image image = new Image(url.toString());
           allCardImageViews[count].setImage(image);
           count++;
       }
    }

    /**
     * get the url of the image to display in gui
     * @param card ACEE, KING, THRE etc.
     * @return ace.png, king.png, thre.png etc.
     */
    private URL getImageUrl(String card) {
        return CardUrl.getURL(card);
    }

    public void removeCard(String card) {
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] != null && cardArray[i].equals(card)) {
                cardArray[i] = null;
                setCardInvisible(i);
                System.out.println("card " + card + " was removed from deck");
            }
        }
    }

    private void setCardInvisible(int i) {
        allCardImageViews[i].setBlendMode(BlendMode.DARKEN);
    }
}
