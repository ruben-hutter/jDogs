package jDogs.gui;

import jDogs.ClientGame;
import jDogs.board.Board;
import jDogs.serverclient.clientside.Client;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Scanner;
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
import javafx.scene.control.DialogPane;
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
public class GameWindow2Controller implements Initializable {

    private static final double RADIUS_CIRCLE = 10;
    private ImageView[] allCardImageViews;
    private AdaptToGui adaptToGui;
    private String[] cardArray;
    private String cardClicked;
    private String color;
    private int playerNr;

    private int colIndexField;
    private int rowIndexField;

    private Circle circle1;
    private Circle circle2;
    private Pane clickedPane;


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

    @FXML
    private FadeTransition fadeTransitionGrid;

    @FXML
    private FadeTransition fadeTransitionCircle1;

    @FXML
    private FadeTransition fadeTransitionCircle2;

    @FXML
    private FadeTransition fadeTransitionCard;

    private boolean yourTurn;

    private Stage allCardsDialog;
    private AllCardsDialogController allCardsDialogController;
    private BorderPane borderPaneDialog;

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
     * card field 0 of gridpane
     * @param event onclick
     */
    @FXML
    void onMouseClick0(MouseEvent event) {
        if (yourTurn && cardArray.length > 0 && cardArray[0] != null) {
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
                    //colIndexCircle2 = GridPane.getColumnIndex(clickedNode);
                    //rowIndexCircle2 = GridPane.getRowIndex(clickedNode);

                    circle2 = (Circle) clickedNode;


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
                    //colIndexCircle1 = GridPane.getColumnIndex(clickedNode);
                    //rowIndexCircle1 = GridPane.getRowIndex(clickedNode);

                    circle1 = (Circle) clickedNode;
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
                            int intId1 = Integer.parseInt(circle1.getId());
                            int intId2 = Integer.parseInt(circle2.getId());

                            String pieceColor1 = getColorOfPiece(intId1);
                            String pieceColor2 = getColorOfPiece(intId2);

                            String pieceID1 = "" + (((intId1) % 4) + 1);
                            String pieceID2 = "" + (((intId2) % 4) + 1);

                            Client.getInstance().sendMessageToServer("MOVE JACK " + pieceColor1 + "-"
                                    + pieceID1 + " " + pieceColor2 + "-" + pieceID2);

                            System.out.println("MOVE JACK " + pieceColor1 + "-"
                                    + pieceID1 + " " + pieceColor2 + "-" + pieceID2);

                            endMoveBlinking();

                            yourTurn = false;
                    } else {
                        System.err.println("didn`t select two pieces for jack");
                    }
                } else {

                    if (fadeTransitionGrid != null) {
                        FieldOnBoard destiny = new FieldOnBoard(colIndexField, rowIndexField);
                        int destinyPos = adaptToGui.getPosNumber(destiny, playerNr);

                        int intId = Integer.parseInt(circle1.getId());
                        String colorPiece = getColorOfPiece(intId);

                        String pieceID = "" + (((intId) % 4) + 1);

                        String newPos;

                        if (destinyPos >= 64) {
                            destinyPos = destinyPos - 64;
                            newPos = "A0" + destinyPos;
                        } else {
                            newPos = "B" + destinyPos;
                            if (destinyPos < 10) {
                                newPos = "B0"+destinyPos;
                            }
                        }

                        System.out.println("MOVE " + cardClicked + " "
                                + colorPiece + "-" + pieceID + " " + newPos);

                        Client.getInstance().sendMessageToServer("MOVE " + cardClicked + " "
                                    + colorPiece + "-" + pieceID + " " + newPos);


                        colIndexField = -1;
                        rowIndexField = -1;
                        endMoveBlinking();
                        yourTurn = false;
                    }
                }
                if (imageViewCard7 != null) {
                    imageViewCard7.setBlendMode(BlendMode.DARKEN);
                }
            }
        }
        System.err.println("INFO not your turn or no card selected");
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
        if (fadeTransitionGrid != null) {
            fadeTransitionGrid.jumpTo(Duration.ZERO);
            fadeTransitionGrid.stop();
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
        if (circle1 != null) {
            circle1 = null;
        }
        if (circle2 != null) {
            circle2 = null;
        }

        if (imageViewCard7 != null) {
            imageViewCard7.setBlendMode(BlendMode.DARKEN);
        }
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


        playerNr = ClientGame.getInstance().getYourPlayerNr();
        if (playerNr < 0) {
            System.err.println("SEVERE ERROR couldn t find nickname in list of game names");
        }

        color = ColorAbbreviations.values()[playerNr].toString();
        nameLabel2.setText(color.toString());
        nameLabel1.setText(Client.getInstance().getNickname());





        setPlayerLabels();



        setOnHome();
        setAllCardImageViews();




      /*  //TODO delete and give Array from ClientGame
        String[] cardddss = new String[]{"JOKE", "JOKE", "JOKE", "JOKE", "NINE", "FOUR"};
        setHand(cardddss);

       */
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
        for (ColorTokens colorTokens: ColorTokens.values()) {
            Color color = colorTokens.getColor();

            for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
                Circle circle = new Circle(RADIUS_CIRCLE, colorTokens.getColor());
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
        FieldOnBoard newPos = adaptToGui.getTrack(newPosition);
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
            return "" + (1 * pieceID);
        } else {
            return "" + (playerNr * 4 + pieceID);
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


    public void setJack(int playerID1, int pieceID1,int newPos1,int playerID2, int pieceID2, int newPos2) {
        //newPos1 == oldPos of pieceID2
        //newPos2 == oldPos of PieceID1
        FieldOnBoard newField1 = adaptToGui.getTrack(newPos1);
        FieldOnBoard newField2 = adaptToGui.getTrack(newPos2);

        String circleID1 = getCircleID(playerID1, pieceID1);
        String circleID2 = getCircleID(playerID2, pieceID2);

        makeJackMove(circleID1,newField1,circleID2,newField2);
    }

    private void makeJackMove(String circleID1, FieldOnBoard newField1, String circleID2, FieldOnBoard newField2) {




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
        System.out.println("card chosen " + card);
        cardClicked = card;
        allCardsDialog.close();
        imageViewCard7.setImage(new Image(CardUrl.getURL(card).toString()));
    }
}
