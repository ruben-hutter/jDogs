package jDogs.gui;

import jDogs.ClientGame;
import jDogs.board.Board;
import jDogs.serverclient.clientside.Client;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * this class is the processing part of the gameWindow
 */
public class GameWindowController implements Initializable {

    private static final double RADIUS_CIRCLE = 10;
    private ImageView[] allCardImageViews;
    private AdaptToGui adaptToGui;
    private String[] cardArray;
    private String cardClicked;
    private boolean jokerClicked;
    private String color;
    private int playerNr;

    private boolean yourTurn;

    private FadeTransition fadeTransitionGrid1;
    private FadeTransition fadeTransitionGrid2;
    private FadeTransition fadeTransitionGrid3;
    private FadeTransition fadeTransitionGrid4;

    private int colIndexField1;
    private int rowIndexField1;
    private Integer colIndexField2;
    private Integer rowIndexField2;
    private Integer rowIndexField3;
    private Integer colIndexField3;
    private Integer rowIndexField4;
    private Integer colIndexField4;

    private Circle circle1;
    private Circle circle2;
    private Circle circle3;
    private Circle circle4;

    private FadeTransition fadeTransitionCircle1;

    private FadeTransition fadeTransitionCircle2;

    private FadeTransition fadeTransitionCircle4;

    private FadeTransition fadeTransitionCircle3;

    private FadeTransition fadeTransitionCard;

    private BorderPane borderPaneDialog;

    private AllCardsDialogController allCardsDialogController;

    private Stage allCardsDialog;


    @FXML
    private Label labelPlayer0;

    @FXML
    private Label labelPlayer1;

    @FXML
    private Label labelPlayer2;

    @FXML
    private Label labelPlayer3;

    @FXML
    private TextArea textLogClient;

    @FXML
    private TextArea textLogServer;

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
    private Button roundOffButton;

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
    private ImageView imageViewCard7;


    @FXML
    private Label nameLabel1;

    @FXML
    private Label nameLabel2;


    /**
     * end application(stop game)
     * @param event on click
     */
    @FXML
    void exitMenuOnAction(ActionEvent event) {
        Client.getInstance().sendMessageToServer("EXIT");
        Client.getInstance().kill();
    }

    /**
     * go back to lobby command(stop game)
     * @param event on click
     */
    @FXML
    void reToLoMenuOnAction(ActionEvent event) {
        Client.getInstance().sendMessageToServer("QUIT");
    }

    /**
     * see stats of the game
     * @param event on click
     */
    @FXML
    void statisticMenuOnAction(ActionEvent event) {

    }

    /**
     * delete all data which was entered via gui while a card was selected
     */
    private void deleteSavedData() {
        endMoveBlinking();

        circle1 = null;
        circle2 = null;
        circle3 = null;
        circle4 = null;

    }
    /**
     * card field 0 of gridpane
     * @param event onclick
     */
    @FXML
    void onMouseClick0(MouseEvent event) {
        if (yourTurn && cardArray.length > 0 && cardArray[0] != null) {
            deleteSavedData();
            startFadeTransitionCard(imageViewCard0);
            cardClicked = cardArray[0];
            checkJokeCase();
        }
    }



    /**
     * card field 1 of gridpane
     * @param event onclick
     */
    @FXML
    void onMouseClick1(MouseEvent event) {
        if (yourTurn && cardArray.length > 1 && cardArray[1] != null) {
            deleteSavedData();
            startFadeTransitionCard(imageViewCard1);
            cardClicked = cardArray[1];
            checkJokeCase();
        }
    }
    /**
     * card field 2 of gridpane
     * @param event onclick
     */
    @FXML
    void onMouseClick2(MouseEvent event) {
        if (yourTurn && cardArray.length > 2 && cardArray[2] != null) {
            deleteSavedData();
            startFadeTransitionCard(imageViewCard2);
            cardClicked = cardArray[2];
            checkJokeCase();
        }
    }
    /**
     * card field 3 of gridpane
     * @param event onclick
     */
    @FXML
    void onMouseClick3(MouseEvent event) {
        if (yourTurn && cardArray.length > 3 && cardArray[3] != null) {
            deleteSavedData();
            startFadeTransitionCard(imageViewCard3);
            cardClicked = cardArray[3];
            checkJokeCase();
        }
    }
    /**
     * card field 4 of gridpane
     * @param event onclick
     */
    @FXML
    void onMouseClick4(MouseEvent event) {
        if (yourTurn && cardArray.length > 4 && cardArray[4] != null) {
            deleteSavedData();
            startFadeTransitionCard(imageViewCard4);
            cardClicked = cardArray[4];
            checkJokeCase();
        }
    }
    /**
     * card field 5 of gridpane
     * @param event onclick
     */
    @FXML
    void onMouseClick5(MouseEvent event) {
        if (yourTurn && cardArray.length > 5 && cardArray[5] != null) {
            deleteSavedData();
            startFadeTransitionCard(imageViewCard5);
            cardClicked = cardArray[5];
            checkJokeCase();

        }
    }

    /**
     * checks if a joker was selected as card and if so
     * pops up a new window to choose a card for joker
     */
    private void checkJokeCase() {
        if (cardClicked.equals("JOKE")) {
            jokerClicked = true;
            String allCardsDialogPath = "src/main/resources/allCardsDialog.fxml";
            URL url = null;
            try {
                url = Paths.get(allCardsDialogPath).toUri().toURL();
            } catch (
                    MalformedURLException e) {
                e.printStackTrace();
            }
            FXMLLoader allCardsDialog = new FXMLLoader(url);

            allCardsDialogController = allCardsDialog.getController();
            borderPaneDialog = null;
            try {
                borderPaneDialog = allCardsDialog.load();
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            this.allCardsDialog = new Stage();
            Scene allCardsScene = new Scene(borderPaneDialog);
            this.allCardsDialog.setScene(allCardsScene);
            this.allCardsDialog.show();
        } else {
            jokerClicked = false;
        }
    }


    @FXML
    void onMouseClickGrid(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        //TODO after move sent set cardClicked to null
        //TODO if JOKE is chosen, find solution on GUI to transform to desired card
        if (clickedNode != gridPane && cardClicked != null && yourTurn) {

            if (clickedNode instanceof Circle) {
                System.out.println("entered circle");

                if (cardClicked.equals("JACK") && circle1 != null) {
                    System.out.println("entered jack");
                    if (fadeTransitionCircle2 != null) {
                        fadeTransitionCircle2.jumpTo(Duration.seconds(9));
                        fadeTransitionCircle2.stop();
                    }
                    fadeTransitionCircle2 = new FadeTransition(Duration.seconds(0.3), clickedNode);
                    fadeTransitionCircle2.setFromValue(1.0);
                    fadeTransitionCircle2.setToValue(0.0);
                    fadeTransitionCircle2.setCycleCount(Animation.INDEFINITE);
                    fadeTransitionCircle2.play();

                    circle2 = (Circle) clickedNode;


                } else {
                    if (cardClicked.equals("SEVE")) {

                        addToCircleArray(clickedNode);

                    }


                    if (cardClicked.equals("SEVEEEEEEn")) {
                        if (circle1 != null) {
                            if (circle2 != null) {
                                if (circle3 != null) {
                                    circle4 = (Circle) clickedNode;
                                    System.out.println("SEVE 4 " + circle4.getId());

                                    if (fadeTransitionCircle4 != null) {
                                        fadeTransitionCircle4.jumpTo(Duration.ZERO);
                                        fadeTransitionCircle4.stop();
                                    }
                                    fadeTransitionCircle4 = new FadeTransition(
                                            Duration.seconds(0.9), clickedNode);
                                    fadeTransitionCircle4.setFromValue(1.0);
                                    fadeTransitionCircle4.setToValue(0.0);
                                    fadeTransitionCircle4.setCycleCount(Animation.INDEFINITE);
                                    fadeTransitionCircle4.play();
                                } else {
                                    circle3 = (Circle) clickedNode;
                                    System.out.println("SEVE 3 " + circle3.getId());
                                    System.out.println("SEVE 3 1 " + circle1.getId());

                                    if (fadeTransitionCircle3 != null) {
                                        fadeTransitionCircle3.jumpTo(Duration.ZERO);
                                        fadeTransitionCircle3.stop();
                                    }
                                    fadeTransitionCircle3 = new FadeTransition(
                                            Duration.seconds(0.9), clickedNode);
                                    fadeTransitionCircle3.setFromValue(1.0);
                                    fadeTransitionCircle3.setToValue(0.0);
                                    fadeTransitionCircle3.setCycleCount(Animation.INDEFINITE);
                                    fadeTransitionCircle3.play();
                                }
                            } else {
                                circle2 = (Circle) clickedNode;
                                System.out.println("SEVE 2 " + circle2.getId());
                                System.out.println("SEVE 2 1 " + circle1.getId());

                                if (fadeTransitionCircle2 != null) {
                                    fadeTransitionCircle2.jumpTo(Duration.ZERO);
                                    fadeTransitionCircle2.stop();
                                }
                                fadeTransitionCircle2 = new FadeTransition(Duration.seconds(0.9),
                                        clickedNode);
                                fadeTransitionCircle2.setFromValue(1.0);
                                fadeTransitionCircle2.setToValue(0.0);
                                fadeTransitionCircle2.setCycleCount(Animation.INDEFINITE);
                                fadeTransitionCircle2.play();
                            }
                        } else {
                            circle1 = (Circle) clickedNode;
                            System.out.println("SEVE 1 " + circle1.getId());
                            if (fadeTransitionCircle1 != null) {
                                fadeTransitionCircle1.jumpTo(Duration.ZERO);
                                fadeTransitionCircle1.stop();
                            }
                            fadeTransitionCircle1 = new FadeTransition(Duration.seconds(0.9),
                                    clickedNode);
                            fadeTransitionCircle1.setFromValue(1.0);
                            fadeTransitionCircle1.setToValue(0.0);
                            fadeTransitionCircle1.setCycleCount(Animation.INDEFINITE);
                            fadeTransitionCircle1.play();
                        }
                        //NOT JACK or SEVE Move
                    } else {
                        System.out.println("Entered not jack");
                        if (fadeTransitionCircle1 != null) {
                            fadeTransitionCircle1.jumpTo(Duration.ZERO);
                            fadeTransitionCircle1.stop();
                        }
                        fadeTransitionCircle1 = new FadeTransition(Duration.seconds(0.9),
                                clickedNode);
                        fadeTransitionCircle1.setFromValue(1.0);
                        fadeTransitionCircle1.setToValue(0.0);
                        fadeTransitionCircle1.setCycleCount(Animation.INDEFINITE);
                        fadeTransitionCircle1.play();
                        circle1 = (Circle) clickedNode;
                    }
                }
            } else {
                if (clickedNode instanceof Pane) {
                    if (cardClicked.equals("SEVE")) {
                        addToGridFields(clickedNode);
                        addToFadingGrids(clickedNode);
                    }


                    if (cardClicked.equals("SEVEeeenn")) {
                        if (fadeTransitionGrid1 != null) {
                            System.out.println("SEVE grid");
                           if (fadeTransitionGrid2 != null) {
                               if (fadeTransitionGrid3 != null) {
                                   if (fadeTransitionGrid4 != null) {
                                       fadeTransitionGrid4.jumpTo(Duration.ZERO);
                                       fadeTransitionGrid4.stop();
                                   }
                                   colIndexField4 = GridPane.getColumnIndex(clickedNode);
                                   rowIndexField4 = GridPane.getRowIndex(clickedNode);

                                   fadeTransitionGrid4 = new FadeTransition(Duration.seconds(0.9),
                                           clickedNode);
                                   fadeTransitionGrid4.setFromValue(1.0);
                                   fadeTransitionGrid4.setToValue(0.0);
                                   fadeTransitionGrid4.setCycleCount(Animation.INDEFINITE);
                                   fadeTransitionGrid4.play();
                               } else {
                                   colIndexField3 = GridPane.getColumnIndex(clickedNode);
                                   rowIndexField3 = GridPane.getRowIndex(clickedNode);

                                   fadeTransitionGrid3 = new FadeTransition(Duration.seconds(0.9),
                                           clickedNode);
                                   fadeTransitionGrid3.setFromValue(1.0);
                                   fadeTransitionGrid3.setToValue(0.0);
                                   fadeTransitionGrid3.setCycleCount(Animation.INDEFINITE);
                                   fadeTransitionGrid3.play();
                               }
                           } else {
                               colIndexField2 = GridPane.getColumnIndex(clickedNode);
                               rowIndexField2 = GridPane.getRowIndex(clickedNode);

                               fadeTransitionGrid2 = new FadeTransition(Duration.seconds(0.9),
                                       clickedNode);
                               fadeTransitionGrid2.setFromValue(1.0);
                               fadeTransitionGrid2.setToValue(0.0);
                               fadeTransitionGrid2.setCycleCount(Animation.INDEFINITE);
                               fadeTransitionGrid2.play();

                           }
                        } else {
                            colIndexField1 = GridPane.getColumnIndex(clickedNode);
                            rowIndexField1 = GridPane.getRowIndex(clickedNode);

                            fadeTransitionGrid1 = new FadeTransition(Duration.seconds(0.9),
                                    clickedNode);
                            fadeTransitionGrid1.setFromValue(1.0);
                            fadeTransitionGrid1.setToValue(0.0);
                            fadeTransitionGrid1.setCycleCount(Animation.INDEFINITE);
                            fadeTransitionGrid1.play();
                        }
                    } else {
                        // not JACK or SEVE
                        // stop first field blinking if one clicks another field
                        if (fadeTransitionGrid1 != null) {
                            fadeTransitionGrid1.jumpTo(Duration.ZERO);
                            fadeTransitionGrid1.stop();
                            fadeTransitionGrid1 = null;
                        }
                        colIndexField1 = GridPane.getColumnIndex(clickedNode);
                        rowIndexField1 = GridPane.getRowIndex(clickedNode);

                        fadeTransitionGrid1 = new FadeTransition(Duration.seconds(0.9),
                                clickedNode);
                        fadeTransitionGrid1.setFromValue(1.0);
                        fadeTransitionGrid1.setToValue(0.0);
                        fadeTransitionGrid1.setCycleCount(Animation.INDEFINITE);
                        fadeTransitionGrid1.play();
                    }
                }
            }
        }
    }

    private void addToFadingGrids(Node clickedNode) {
        if (gridCount < 7) {
            fadingGrids[gridCount++];
        } else {
            fadingGrids[gridCount].set(Duration.ZERO);
            fadingGrids[gridCount].stop();
            fadingGrids[gridCount] =  new FadeTransition();
        }
    }

    private void addToFadingCircles(Node clickedNode) {
        if (circleCount < 7) {
            fadingCircles[circleCount++] = new FadeTransition();
        } else {
            fadingCircles[circleCount].set(Duration.ZERO);
            fadingCircles[circleCount].stop();
            fadingCircles[circleCount] = new FadeTransition();
        }

    }

    private void addToGridFields(Node clickedNode) {
        if (gridCount < 7) {
            clickedGrids[gridCount++];
        } else {
            clickedGrids[gridCounter] = new FieldOnBoard(GridPane.getColumnIndex(clickedNode), GridPane.getRowIndex(clickedNode));
        }
    }

    private void addToCircleArray(Node clickedNode) {
        if (circleCount < 7) {
            clickedCircles[circleCount++] = (Circle) clickedNode;
        } else {
            clickedCircles[circleCount] = (Circle) clickedNode;
        }
    }

    @FXML
    void makeMoveButtonOnAction(ActionEvent event) {
        if (cardClicked != null && yourTurn) {
            if (fadeTransitionCircle1 != null) {

                if (cardClicked.equals("JACK")) {
                    if (fadeTransitionCircle2 != null) {
                        int intId1 = Integer.parseInt(circle1.getId());
                        int intId2 = Integer.parseInt(circle2.getId());

                        String pieceColor1 = getColorOfPiece(intId1);
                        String pieceColor2 = getColorOfPiece(intId2);

                        String pieceID1 = "" + (((intId1) % 4) + 1);
                        String pieceID2 = "" + (((intId2) % 4) + 1);

                        String move = "MOVE JACK ";

                        if (jokerClicked) {
                            move = "MOVE JOKE JACK ";
                        }
/*
                        Client.getInstance().sendMessageToServer(move + pieceColor1 + "-"
                                + pieceID1 + " " + pieceColor2 + "-" + pieceID2);

 */

                        System.out.println(move + pieceColor1 + "-"
                                + pieceID1 + " " + pieceColor2 + "-" + pieceID2);
                        endMoveBlinking();
                       // yourTurn = false;
                    } else {
                        System.err.println("didn`t select two pieces for jack");
                    }
                    return;
                }
                System.out.println("Circle 1 id before0 " + circle1.getId());

                if (cardClicked.equals("SEVE")) {
                    System.out.println("SEVE make move button");
                    System.out.println("Circle 1 id before1 " + circle1.getId());

                    if (fadeTransitionGrid1 != null) {
                        //TODO i need 4 transition grids clickable if seve add above
                        if (circle1 != null) {
                            System.out.println("Circle 1 id before2 " + circle1.getId());

                            int clicked = countCirclesClicked();
                            System.out.println("Circle 1 id before3 " + circle1.getId());
                            System.out.println("SEVE chosen " + clicked);
                            String message = "MOVE SEVE " + clicked;
                            if (jokerClicked) {
                               message = "MOVE JOKE SEVE " + clicked;
                           }
                           for (int i = 0; i < clicked; i++) {
                               int id = Integer.parseInt(circle1.getId());
                               System.out.println("id1 " + id);
                               int playerNumber = id / 4;
                               message += " " + getColorOfPiece(id) + "-" + id;
                               message += " " + adaptToGui.getPosNumber(new FieldOnBoard(colIndexField1,rowIndexField1),playerNumber);

                               i++;
                               if (i >= clicked) {
                                   break;
                               }

                               id = Integer.parseInt(circle2.getId());
                               System.out.println("id2 " + id);

                               playerNumber = id / 4;
                               message += " " + getColorOfPiece(id) + "-" + id;
                               message += " " + adaptToGui.getPosNumber(new FieldOnBoard(colIndexField2,rowIndexField2),playerNumber);

                               i++;
                               if (i >= clicked) {
                                   break;
                               }

                               id = Integer.parseInt(circle3.getId());
                               System.out.println("id3 " + id);

                               playerNumber = id / 4;
                               message += " " + getColorOfPiece(id) + "-" + id;
                               message += " " + adaptToGui.getPosNumber(new FieldOnBoard(colIndexField3,rowIndexField3),playerNumber);

                               i++;
                               if (i >= clicked) {
                                   break;
                               }

                               id = Integer.parseInt(circle4.getId());
                               playerNumber = id / 4;
                               message += " " + getColorOfPiece(id) + "-" + id;
                               message += " " + adaptToGui.getPosNumber(new FieldOnBoard(colIndexField4,rowIndexField4),playerNumber);
                           }
                           System.out.println(message);

                           //Client.getInstance().sendMessageToServer(message);
                            //yourTurn = false;
                            endMoveBlinking();
                        }
                    }
                    //not SEVE or JACK
                } else {
                    if (fadeTransitionGrid1 != null) {
                        FieldOnBoard destiny = new FieldOnBoard(colIndexField1, rowIndexField1);
                        System.out.println(colIndexField1 + " " + rowIndexField1);
                        String newPos = adaptToGui.getPosNumber(destiny, playerNr);

                            //TODO return if destiny is home position

                        int intId = Integer.parseInt(circle1.getId());
                        String colorPiece = getColorOfPiece(intId);

                        String pieceID = "" + (((intId) % 4) + 1);


                        String move = "MOVE ";
                        if (jokerClicked) {
                            move = "MOVE JOKE ";
                        }
                        System.out.println(move + cardClicked + " "
                                + colorPiece + "-" + pieceID + " " + newPos);
/*
                        Client.getInstance().sendMessageToServer(move + cardClicked + " "
                                + colorPiece + "-" + pieceID + " " + newPos);

 */

                        endMoveBlinking();
                        //yourTurn = false;
                    }
                }
            }
        }
        System.err.println("INFO not your turn or no card selected");
    }

    /**
     * this is used to count a SEVE Move
     * @return number of circles clicked
     */
    private int countCirclesClicked() {
        int count = 1;
        if (circle2 != null && rowIndexField2 >= 0 && colIndexField2 >= 0) {
            count++;
        }
        if (circle3 != null && rowIndexField3 >= 0 && colIndexField3 >= 0) {
            count++;
        }
        if (circle4 != null && rowIndexField4 >= 0 && colIndexField4 >= 0) {
            count++;
        }
        return count;
    }

    /**
     * this method returns the color of the piece that was selected according to the pieceID
     * @param id int of the pieceID
     * @return String of colorAbbreviation
     */
    private String getColorOfPiece(int id) {
        int playerNumber = (id / 4);
        int count = 0;
        for (ColorAbbreviations colorAbbreviations : ColorAbbreviations.values()) {
            if (count == playerNumber) {
                return colorAbbreviations.toString();
            }
            count++;
        }
        return null;
    }
    /**
     * this method is used to tell the server the player cannot play this round
     * @param event clickAction
     */
    @FXML
    void roundOffButtonOnAction(ActionEvent event) {
        if (yourTurn) {
            Client.getInstance().sendMessageToServer("MOVE SURR");
            endMoveBlinking();
        }
    }

    /**
     * this method ends any blinking items in the gui when the move is sent
     */
    private void endMoveBlinking() {
        colIndexField1 = -1;
        rowIndexField1 = -1;

        colIndexField2 = -1;
        rowIndexField2 = -1;

        colIndexField3 = -1;
        rowIndexField3 = 1;

        colIndexField4 = -1;
        rowIndexField4 = -1;

        if (fadeTransitionGrid1 != null) {
            System.out.println("Stop blink1");
            fadeTransitionGrid1.jumpTo(Duration.ZERO);
            fadeTransitionGrid1.stop();
        }

        if (fadeTransitionGrid2 != null) {
            System.out.println("Stop blink2");

            fadeTransitionGrid2.jumpTo(Duration.ZERO);
            fadeTransitionGrid2.stop();
        }

        if (fadeTransitionGrid3 != null) {
            System.out.println("Stop blink3");

            fadeTransitionGrid3.jumpTo(Duration.ZERO);
            fadeTransitionGrid3.stop();
        }

        if (fadeTransitionGrid4 != null) {
            System.out.println("Stop blink4");

            fadeTransitionGrid4.jumpTo(Duration.ZERO);
            fadeTransitionGrid4.stop();
        }

        if (fadeTransitionCard != null) {
            fadeTransitionCard.jumpTo(Duration.ZERO);
            fadeTransitionCard.stop();
        }

        if (fadeTransitionCircle1 != null) {
            fadeTransitionCircle1.jumpTo(Duration.ZERO);
            fadeTransitionCircle1.stop();
        }

        if (fadeTransitionCircle2 != null) {
            fadeTransitionCircle2.jumpTo(Duration.ZERO);
            fadeTransitionCircle2.stop();
        }

        if (fadeTransitionCircle3 != null) {
            fadeTransitionCircle3.jumpTo(Duration.ZERO);
            fadeTransitionCircle3.stop();
        }

        if (fadeTransitionCircle4 != null) {
            fadeTransitionCircle4.jumpTo(Duration.ZERO);
            fadeTransitionCircle4.stop();
        }


        if (circle1 != null) {
            circle1 = null;
        }
        if (circle2 != null) {
            circle2 = null;
        }

        if (circle3 != null) {
            circle1 = null;
        }
        if (circle4 != null) {
            circle2 = null;
        }

        if (imageViewCard7 != null) {
            imageViewCard7.setVisible(false);
        }
        cardClicked = null;
        jokerClicked = false;
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

    /**
     * start fading the card after it was selected by click
     * @param imageViewBlink the imageView with image of card
     */
    private void startFadeTransitionCard(ImageView imageViewBlink) {
        if (fadeTransitionCard != null) {
            fadeTransitionCard.jumpTo(Duration.ZERO);
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
        jokerClicked = false;

        yourTurn = true;
/*
        playerNr = ClientGame.getInstance().getYourPlayerNr();
        if (playerNr < 0) {
            System.err.println("SEVERE ERROR couldn t find nickname in list of game names");
        }

        color = ColorAbbreviations.values()[playerNr].toString();
        nameLabel2.setText(color.toString());
        nameLabel1.setText(Client.getInstance().getNickname());

        setPlayerLabels();

 */

        setOnHome();
        setAllCardImageViews();




      //TODO delete and give Array from ClientGame
        String[] cardddss = new String[]{"JOKE", "JOKE", "JOKE", "JOKE", "SEVE", "SEVE"};
        setHand(cardddss);

        makeSingleMove("0",adaptToGui.getTrack(4));
        makeSingleMove("7",adaptToGui.getTrack(7));
        makeSingleMove("12",adaptToGui.getTrack(24));



    }

    /**
     * sets the text of the labels by adding the nickname of the participating users.
     */
    private void setPlayerLabels() {

        labelPlayer0.setText(ClientGame.getInstance().getPlayerNames()[0]);
        labelPlayer1.setText(ClientGame.getInstance().getPlayerNames()[1]);
        labelPlayer2.setText(ClientGame.getInstance().getPlayerNames()[2]);
        labelPlayer3.setText(ClientGame.getInstance().getPlayerNames()[3]);
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
        for (ColorFXEnum colorFXEnum : ColorFXEnum.values()) {
            Color color = colorFXEnum.getColor();

            for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
                Circle circle = new Circle(RADIUS_CIRCLE, colorFXEnum.getColor());
                circle.setId("" + (count));
                System.out.println("circle ids " + (count));
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

    /**
     * remove a card from deck by making card blended
     * @param card
     */
    public void removeCard(String card) {
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] != null && cardArray[i].equals(card)) {
                cardArray[i] = null;
                setCardBlended(i);
                System.out.println("card " + card + " was removed from deck");
            }
        }
    }
    /**
     * after using card set card blended
     * @param i == number of card in array
     */
    private void setCardBlended(int i) {
        allCardImageViews[i].setBlendMode(BlendMode.DARKEN);
    }

    /**
     * on true: allows this user to make a move
     * @param value true, if it`s his or her turn
     */
    public void setYourTurn(boolean value) {
        //TODO send message to user in GUI : your turn
        this.yourTurn = value;
        if (yourTurn) {
            System.out.println("your turn message arrived");
            Alert alert = new Alert(AlertType.INFORMATION,"It is your turn");
            alert.show();
        }
    }

    /**
     * client game sends a move (which was sent from server) to the gui
     * @param playerNr yellow = player1, green = player2, blue = player3, red = player4
     * @param pieceID 0,1,2,3
     * @param newPosition position nr on server
     */
    public void makeSingleMoveTrack(int playerNr, int pieceID, int newPosition) {
        String circleID = getCircleID(playerNr, pieceID);
        System.out.println("circle ID for makeSingleMoveTrack " + circleID);
        System.out.println("Player NR " + playerNr);
        System.out.println("pieceId " + pieceID);
        FieldOnBoard newPos = adaptToGui.getTrack(newPosition);
        System.out.println("new Pos " + newPos.getX() + " " + newPos.getY());
        System.out.println("CircleID " + circleID);
        makeSingleMove(circleID, newPos);

    }

    /**
     * returns the ID given to the piece in gui at the beginning(by command setOnHome())
      * @param playerNr 0-3
     * @param pieceID 0-3
     * @return circleID for circle in gui
     */
    private String getCircleID(int playerNr, int pieceID) {
        if (playerNr == 0) {
            return "" + ((1 * pieceID) - 1);
        } else {
            return "" + ((playerNr * 4 + pieceID) - 1);
        }
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
    /**
     * makes a move with jack. It removes circle1 from gui,
     * removes and adds circle2 to position of circle1,
     * adds circle1 to position of circle2
     * @param playerID1 0,1,2,3
     * @param pieceID1 0,..,3
     * @param playerID2 0,..,3
     * @param pieceID2 0,..,3
     */
    public void makeJackMove(int playerID1, int pieceID1,int playerID2, int pieceID2) {
        String circleID1 = getCircleID(playerID1, pieceID1);
        String circleID2 = getCircleID(playerID2, pieceID2);

        int colIndex1 = -1;
        int rowIndex1 = -1;

        int colIndex2 = -1;
        int rowIndex2 = -1;
        //remove circle1
        Circle circleJack1 = null;
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Circle) {
                circleJack1 = (Circle) node;
                if (circleJack1.getId().equals(circleID1)) {
                    colIndex1 = GridPane.getColumnIndex(circleJack1);
                    rowIndex1 = GridPane.getRowIndex(circleJack1);
                    gridPane.getChildren().remove(circleJack1);
                    break;
                }
            }
        }
        //remove circle2 and set on position of circle1
        Circle circleJack2 = null;

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Circle) {
                circleJack2 = (Circle) node;
                if (circleJack2.getId().equals(circleID2)) {
                    colIndex2 = GridPane.getColumnIndex(circleJack2);
                    rowIndex2 = GridPane.getRowIndex(circleJack2);
                    gridPane.getChildren().remove(circleJack2);
                    gridPane.add(circleJack2,colIndex1, rowIndex1);
                    break;
                }
            }
        }
        //add move circle1 again
        gridPane.add(circleJack1, colIndex2, rowIndex2);
    }


    /**
     * sets a piece to a position in heaven
     * @param playerNumber 0-3
     * @param pieceID 0-3
     * @param newPos 0-3
     */
    public void makeHeavenMove(int playerNumber, int pieceID, int newPos) {
        String circleID ="" + ((playerNr + 1) * (pieceID + 1));
        FieldOnBoard heavenField = adaptToGui.getHeavenField(playerNumber, newPos);
        makeSingleMove(circleID,heavenField);
    }

    /**
     * sends a piece to home field(usually this is used if a piece is sent home)
     * @param playerNumber
     * @param pieceID
     */
    public void makeHomeMove(int playerNumber, int pieceID) {
        String circleID ="" + ((playerNr + 1) * (pieceID + 1));
        int startPos = playerNumber * 16;
        FieldOnBoard homeField = adaptToGui.getHomeField(startPos,pieceID);
        makeSingleMove(circleID, homeField);

    }

    /**
     * this method sets the card the user chooses because he had a joker
     * @param card ACEE,......,KING (without JOKE)
     */
    public void setCardFromJoker(String card) {
        cardClicked = card;
        allCardsDialog.close();
        imageViewCard7.setImage(new Image(CardUrl.getURL(card).toString()));
        imageViewCard7.setVisible(true);
    }
}
