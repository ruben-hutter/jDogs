package jDogs.gui;

import jDogs.board.Board;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private String color;
    private int playerNr;
    private int colIndexCircle2;
    private int rowIndexCircle2;
    private int rowIndexCircle1;
    private int colIndexCircle1;
    private int colIndexField;
    private int rowIndexField;


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

    private boolean yourTurn;

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
        if (yourTurn && cardArray.length > 0 && cardArray[0] != null) {
            startFadeTransitionCard(imageViewCard0);
            cardClicked = cardArray[0];

        }
        System.out.println("clicked card 0");
    }


    @FXML
    void onMouseClick1(MouseEvent event) {
        if (yourTurn && cardArray.length > 1 && cardArray[1] != null) {
            startFadeTransitionCard(imageViewCard1);
            cardClicked = cardArray[1];

        }
        System.out.println("clicked card 1");

    }

    @FXML
    void onMouseClick2(MouseEvent event) {
        if (yourTurn && cardArray.length > 2 && cardArray[2] != null) {
            startFadeTransitionCard(imageViewCard2);
            cardClicked = cardArray[2];

        }
        System.out.println("clicked card 2");
    }

    @FXML
    void onMouseClick3(MouseEvent event) {
        if (yourTurn && cardArray.length > 3 && cardArray[3] != null) {
            startFadeTransitionCard(imageViewCard3);
            cardClicked = cardArray[3];

        }
        System.out.println("clicked card 3");

    }

    @FXML
    void onMouseClick4(MouseEvent event) {
        if (yourTurn && cardArray.length > 4 && cardArray[4] != null) {
            startFadeTransitionCard(imageViewCard4);
            cardClicked = cardArray[4];

        }
        System.out.println("clicked card 4");

    }

    @FXML
    void onMouseClick5(MouseEvent event) {
        if (yourTurn && cardArray.length > 5 && cardArray[5] != null) {
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
        if (clickedNode != gridPane && cardClicked != null && yourTurn) {

            if (clickedNode instanceof Circle) {
                System.out.println("entered circle");

                if (cardClicked.equals("JACK") && fadeTransitionCircle1!= null) {
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
                    colIndexCircle2 = GridPane.getColumnIndex(clickedNode);
                    rowIndexCircle2 = GridPane.getRowIndex(clickedNode);
                } else {
                    if (fadeTransitionCircle1 != null) {
                        fadeTransitionCircle1.jumpTo(Duration.ZERO);
                        fadeTransitionCircle1.stop();
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
        if (cardClicked != null && yourTurn) {
            if (fadeTransitionCircle1 != null) {
                if (cardClicked.equals("JACK")) {
                    if (fadeTransitionCircle2 != null) {
                        fadeTransitionCard.jumpTo(Duration.ZERO);
                        fadeTransitionCard.stop();
                        fadeTransitionCard = null;

                        fadeTransitionCircle1.jumpTo(Duration.ZERO);
                        fadeTransitionCircle1.stop();
                        fadeTransitionCircle1 = null;

                        fadeTransitionCircle2.jumpTo(Duration.ZERO);
                        fadeTransitionCircle2.stop();
                        fadeTransitionCircle2 = null;

                        colIndexCircle2 = -1;
                        rowIndexCircle2 = -1;

                        if (fadeTransitionGrid != null) {
                            fadeTransitionGrid.jumpTo(Duration.ZERO);
                            fadeTransitionGrid.stop();
                            fadeTransitionGrid = null;
                        }
                        yourTurn = false;
                        System.out.println("jack move sent");

                    } else {
                        System.err.println("didn`t select two pieces for jack");
                    }
                } else {
                    if (fadeTransitionGrid != null) {

                        System.out.println("simple move sent");
                        FieldOnBoard destiny = new FieldOnBoard(colIndexField, rowIndexField);
                        int destinyPos = adaptToGui.getPosNumber(destiny, playerNr);
                        System.out.println("DESTINY POS " + destinyPos);
                        String pieceID = getPieceIDOnPane(colIndexCircle1, rowIndexCircle1);

                        System.out.println("PIECE ID " + pieceID);
                        String newPos;

                        if (destinyPos >= 64) {
                            destinyPos = destinyPos - 64;
                            newPos = "A" + destinyPos;
                        } else {
                            newPos = "B" + destinyPos;
                        }
                        System.out.println("MOVE " + cardClicked + " " + color + "-"
                                + pieceID + " " + newPos);
                    /*Client.getInstance().sendMessageToServer("MOVE " + cardClicked + " "
                                    + pieceID + " " + newPos);

                     */
                        colIndexField = -1;
                        rowIndexField = -1;
                        fadeTransitionCard.jumpTo(Duration.ZERO);
                        fadeTransitionCard.stop();
                        fadeTransitionGrid.jumpTo(Duration.ZERO);
                        fadeTransitionGrid.stop();
                        fadeTransitionCircle1.jumpTo(Duration.ZERO);
                        fadeTransitionCircle1.stop();
                        yourTurn = false;
                    }
                }
                rowIndexCircle1 = -1;
                colIndexCircle1 = -1;
            }
        }
        System.err.println("INFO not your turn or no card selected");

    }

    /**
     *
     * @param colIndexCircle column in gridPane
     * @param rowIndexCircle row in gridPane
     * @return the pieceID of the piece in the gridPane that was clicked by the user
     */
    private String getPieceIDOnPane(double colIndexCircle, double rowIndexCircle) {
       for (Node node : gridPane.getChildren()) {
           if (node instanceof Circle) {
               if (GridPane.getColumnIndex(node) == colIndexCircle
                       && GridPane.getRowIndex(node) == rowIndexCircle) {
                   Circle circle = (Circle) node;
                   return circle.getId();
               }
           }
       }
       return null;
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
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adaptToGui = new AdaptToGui();


        yourTurn = true;
        playerNr = 0;

        /*
        playerNr = ClientGame.getInstance().getYourPlayerNr();
        if (playerNr < 0) {
            System.err.println("SEVERE ERROR couldn t find nickname in list of game names");
        }

         */

        color = ColorAbbreviations.values()[playerNr].toString();


        setOnHome();
        makeSingleMoveTrack(0,0,15);
        setAllCardImageViews();



        //TODO delete and give Array from ClientGame
        String[] cardddss = new String[]{"ACEE", "KING", "THRE", "TWOO", "NINE", "FOUR"};
        setHand(cardddss);
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
     * client game sends the cards for this round to the gui here
     * @param cards these cards are the hand for this round
     */
    public void setHand(String[] cards) {
        this.cardArray = cards;
        int count = 0;
        for(String card : cardArray) {
            URL url = CardUrl.getURL(card);
            Image image = new Image(url.toString());
            allCardImageViews[count].setImage(image);
            count++;
        }
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

    /**
     * after using card set card invisible
     * @param i == number of card in array
     */
    private void setCardInvisible(int i) {
        allCardImageViews[i].setBlendMode(BlendMode.DARKEN);
    }

    public void setYourTurn(boolean value) {
        //TODO send message to user in GUI : your turn
        this.yourTurn = value;
    }

    /**
     * client game sends a move (which was sent from server) to the gui
     * @param playerNr yellow = player1, green = player2, blue = player3, red = player4
     * @param pieceID 0,1,2,3
     * @param newPosition position nr on server
     */
    public void makeSingleMoveTrack(int playerNr, int pieceID, int newPosition) {
        String circleID ="" + ((playerNr + 1) * (pieceID + 1));
        System.out.println("circle ID for makeSingleMoveTrack " + circleID);
        FieldOnBoard newPos = adaptToGui.getTrack(newPosition);
        makeSingleMove(circleID, newPos);

    }

    private void makeSingleMove(String circleID, FieldOnBoard newPos) {
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

    public void makeHeavenMove(int playerNumber, int pieceID, int newPos) {
        String circleID ="" + ((playerNr + 1) * (pieceID + 1));
        FieldOnBoard heavenField = adaptToGui.getHeavenField(playerNumber, newPos);
        makeSingleMove(circleID,heavenField);
    }

    public void makeHomeMove(int playerNumber, int pieceID) {
        String circleID ="" + ((playerNr + 1) * (pieceID + 1));
        int startPos = playerNumber * 16;
        FieldOnBoard homeField = adaptToGui.getHomeField(startPos,pieceID);
        makeSingleMove(circleID, homeField);

    }
}
