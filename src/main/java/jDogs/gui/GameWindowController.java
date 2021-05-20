package jDogs.gui;

import jDogs.ClientGame;
import jDogs.board.Board;
import jDogs.serverclient.clientside.Client;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

    private FadeTransition fadeTransitionCard;

    private BorderPane borderPaneDialog;

    private AllCardsDialogController allCardsDialogController;

    private Stage allCardsDialog;

    @FXML
    private Button refreshButton;

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
    private TextArea messageReceiveTextArea;

    @FXML
    private TextField sendMessageTextField;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem statMenuItem;

    @FXML
    private MenuItem retToLoMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem quickGuide;

    @FXML // fx:id="instructions"
    private MenuItem instructions; // Value injected by FXMLLoader

    @FXML
    private Pane leftBoardSide;

    @FXML
    private Pane rightBoardSide;

    @FXML
    private VBox vBoxGridWrapper;

    @FXML
    private GridPane gridPane;

    @FXML
    HBox hboxCardHolder;
    @FXML
    private HBox playerDisplay;

    @FXML
    private Button makeMoveButton;

    @FXML
    private Button roundOffButton;




    @FXML
    private ImageView imageViewCard0;
    @FXML
    private ImageView imageViewCard1;
    @FXML
    private ImageView imageViewCard2;
    @FXML
    private ImageView imageViewCard3;
    @FXML
    private ImageView imageViewCard4;
    @FXML
    private ImageView imageViewCard5;
    @FXML
    private ImageView imageViewCard7;
    @FXML
    private Text text1;

    private int gridCount;
    private int totalSum;

    private FadeTransition[] fadingGrids;
    private int circleCount;
    private String[] clickedCircleIds;
    private FieldOnBoard[] clickedGridFields;
    private FadeTransition[] fadingCircles;

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
     * Opens a window with a quick guide
     * @param actionEvent menu selection
     */
    @FXML
    void openQuickGuide(ActionEvent actionEvent) {
        try {
            FXMLLoader helpLoader = new FXMLLoader(getClass().getResource("/quickGuide.fxml"));
            Parent root1 = helpLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openInstructions(ActionEvent event) {
        try {
            FXMLLoader helpLoader = new FXMLLoader(getClass().getResource("/helpWindow.fxml"));
            Parent root1 = helpLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * go back to lobby command(stop game)
     * @param event on click
     */
    @FXML
    void reToLoMenuOnAction(ActionEvent event) {
        Client.getInstance().sendMessageToServer("QUIT");
        GUIManager.getInstance().returnToPubLobby();
    }

    /**
     * see stats of the game
     * @param event on click
     */
    @FXML
    void statisticMenuOnAction(ActionEvent event) {
//TODO show stats
    }

    /**
     * card field 0 of gridpane
     * @param event onclick
     */
    @FXML
    void onMouseClick0(MouseEvent event) {
        if (yourTurn && cardArray.length > 0 && cardArray[0] != null) {
            deleteClickedData();
            startFadeTransitionCard(imageViewCard0);
            cardClicked = cardArray[0];
            if (cardClicked.equals("SEVE")) {
                totalSum = 7;
            }
            if (cardClicked.equals("JACK")) {
                totalSum = 2;
            }
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
            deleteClickedData();
            startFadeTransitionCard(imageViewCard1);
            cardClicked = cardArray[1];
            if (cardClicked.equals("SEVE")) {
                totalSum = 7;
            }
            if (cardClicked.equals("JACK")) {
                totalSum = 2;
            }
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
            deleteClickedData();
            startFadeTransitionCard(imageViewCard2);
            cardClicked = cardArray[2];
            if (cardClicked.equals("SEVE")) {
                totalSum = 7;
            }
            if (cardClicked.equals("JACK")) {
                totalSum = 2;
            }
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
            deleteClickedData();
            startFadeTransitionCard(imageViewCard3);
            cardClicked = cardArray[3];
            if (cardClicked.equals("SEVE")) {
                totalSum = 7;
            }
            if (cardClicked.equals("JACK")) {
                totalSum = 2;
            }
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
            deleteClickedData();
            startFadeTransitionCard(imageViewCard4);
            cardClicked = cardArray[4];
            if (cardClicked.equals("SEVE")) {
                totalSum = 7;
            }
            if (cardClicked.equals("JACK")) {
                totalSum = 2;
            }
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
            deleteClickedData();
            startFadeTransitionCard(imageViewCard5);
            cardClicked = cardArray[5];
            if (cardClicked.equals("SEVE")) {
                totalSum = 7;
            }
            if (cardClicked.equals("JACK")) {
                totalSum = 2;
            }
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
            FXMLLoader allCardsDialog = new FXMLLoader(getClass().getResource("/allCardsDialog.fxml"));

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

    /**
     * clicking somewhere in the gridpane
     * and if the clicked node is a pane or a circle
     * it will be processed
     * @param event click
     */
    @FXML
    void onMouseClickGrid(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != gridPane && cardClicked != null && yourTurn) {
            if (clickedNode instanceof Circle) {
                addToCirclesID(clickedNode);
            } else {
                if (clickedNode instanceof Pane) {
                    addToGridFields(clickedNode);
                }
            }
        }
    }

    /**
     * save a clicked grid
     * @param clickedNode grid in grid pane
     */
    private void addToFadingGrids(Node clickedNode) {
        if (gridCount < totalSum) {
            fadingGrids[gridCount] = new FadeTransition(Duration.seconds(0.9),
                    clickedNode);
            fadingGrids[gridCount].setFromValue(1.0);
            fadingGrids[gridCount].setToValue(0.0);
            fadingGrids[gridCount].setCycleCount(Animation.INDEFINITE);
            fadingGrids[gridCount].play();

        } else {
            fadingGrids[gridCount - 1].jumpTo(Duration.ZERO);
            fadingGrids[gridCount - 1].stop();

            fadingGrids[gridCount - 1] = new FadeTransition(Duration.seconds(0.9),
                    clickedNode);
            fadingGrids[gridCount - 1].setFromValue(1.0);
            fadingGrids[gridCount - 1].setToValue(0.0);
            fadingGrids[gridCount - 1].setCycleCount(Animation.INDEFINITE);
            fadingGrids[gridCount - 1].play();
        }
    }

    /**
     * make this clicked circle fading;
     * if total of clickable circles
     * for this card is already clicked
     * then one clicked circle will stop
     * fading and is deleted
     * @param clickedNode circle
     */
    private void addToFadingCircles(Node clickedNode) {
        if (circleCount < totalSum) {
            fadingCircles[circleCount] = new FadeTransition(Duration.seconds(0.9),
                    clickedNode);
            fadingCircles[circleCount].setFromValue(1.0);
            fadingCircles[circleCount].setToValue(0.0);
            fadingCircles[circleCount].setCycleCount(Animation.INDEFINITE);
            fadingCircles[circleCount].play();

        } else {
            fadingCircles[circleCount - 1].jumpTo(Duration.ZERO);
            fadingCircles[circleCount - 1].stop();

            fadingCircles[circleCount - 1] = new FadeTransition(Duration.seconds(0.9),
                    clickedNode);
            fadingCircles[circleCount - 1].setFromValue(1.0);
            fadingCircles[circleCount - 1].setToValue(0.0);
            fadingCircles[circleCount - 1].setCycleCount(Animation.INDEFINITE);
            fadingCircles[circleCount - 1].play();
        }
    }

    /**
     * adds a grid to the clicked grids,
     * which will be remembered
     * if user wants to make a move
     * @param clickedNode circle
     */
    private void addToGridFields(Node clickedNode) {
        addToFadingGrids(clickedNode);
        if (gridCount < totalSum) {
            clickedGridFields[gridCount] = new FieldOnBoard(GridPane.getColumnIndex(clickedNode),
                    GridPane.getRowIndex(clickedNode));
            gridCount++;
            FieldOnBoard fieldOnBoard = new FieldOnBoard(GridPane.getColumnIndex(clickedNode), GridPane.getRowIndex(clickedNode));
            System.out.println("Clicked Grid not full tot: " + totalSum + " gridCount: "
                     + gridCount + " field: x " + fieldOnBoard.getX() + " y " + fieldOnBoard.getY());
        } else {
            clickedGridFields[gridCount - 1] = new FieldOnBoard(GridPane.getColumnIndex(clickedNode),
                    GridPane.getRowIndex(clickedNode));
            FieldOnBoard fieldOnBoard = new FieldOnBoard(GridPane.getColumnIndex(clickedNode), GridPane.getRowIndex(clickedNode));

            System.out.println("Clicked Grid full tot: " + totalSum + " gridCount: " + gridCount +
                    " field: x " + fieldOnBoard.getX() + " y " + fieldOnBoard.getY());
        }
    }

    /**
     * adds this circle to the clicked circles
     * so that if one wants to make a move the id
     * is already known
     * @param clickedNode circle
     */
    private void addToCirclesID(Node clickedNode) {
        System.out.println("CircleID count " + circleCount);
        System.out.println("TotalCircle " + totalSum);
        addToFadingCircles(clickedNode);

        Circle circle = (Circle) clickedNode;
        if (circleCount < totalSum) {
            clickedCircleIds[circleCount] =circle.getId();
            circleCount++;
        } else {
            clickedCircleIds[circleCount] = circle.getId();
        }
    }

    /**
     * click to send a move to server
     * clicked card and clicked circles and grids will be selected,
     * processed to a string and sent as MOVE to server
     * @param event
     */
    @FXML
    void makeMoveButtonOnAction(ActionEvent event) {
        if (cardClicked != null && yourTurn) {
            if (clickedCircleIds[0] != null) {
                if (cardClicked.equals("JACK")) {
                    if (clickedCircleIds[1] != null) {
                        int intId1 = Integer.parseInt(clickedCircleIds[0]);
                        int intId2 = Integer.parseInt(clickedCircleIds[1]);

                        String pieceColor1 = getColorOfPiece(intId1);
                        String pieceColor2 = getColorOfPiece(intId2);

                        String pieceID1 = "" + (((intId1) % 4) + 1);
                        String pieceID2 = "" + (((intId2) % 4) + 1);

                        String move = "MOVE JACK ";

                        if (jokerClicked) {
                            move = "MOVE JOKE JACK ";
                        }

                        Client.getInstance().sendMessageToServer(move + pieceColor1 + "-"
                                + pieceID1 + " " + pieceColor2 + "-" + pieceID2);


                        System.out.println(move + pieceColor1 + "-"
                                + pieceID1 + " " + pieceColor2 + "-" + pieceID2);

                        yourTurn = false;
                        deleteClickedData();
                    } else {
                        System.err.println("INFO not clicked a second circle for JACK");
                    }
                } else {
                    if (clickedGridFields[0] != null) {
                        if (cardClicked.equals("SEVE")) {
                            int clicked = circleCount;
                            String message = "MOVE SEVE " + clicked;
                            if (jokerClicked) {
                                message = "MOVE JOKE SEVE " + clicked;
                            }

                            for (int i = 0; i < clicked; i++) {
                                int id = Integer.parseInt(clickedCircleIds[i]);
                                int playerNumber = id / 4;
                                String pieceId = "" + ((id % 4) + 1);
                                message += " " + getColorOfPiece(id) + "-" + pieceId;
                                message += " " + adaptToGui.getPosNumber(clickedGridFields[i],playerNumber);
                            }

                            System.out.println(message);

                            Client.getInstance().sendMessageToServer(message);
                            yourTurn = false;

                            deleteClickedData();
                        } else {
                        //not SEVE or JACK
                            FieldOnBoard destiny = clickedGridFields[0];
                            int intID = Integer.parseInt(clickedCircleIds[0]);
                            int playerNumb = intID / 4;
                            String newPos = adaptToGui.getPosNumber(destiny, playerNumb);
                            String colorPiece = getColorOfPiece(intID);
                            String pieceID = "" + (((intID) % 4) + 1);

                            String move = "MOVE ";
                            if (jokerClicked) {
                                move = "MOVE JOKE ";
                            }
                            System.out.println(move + cardClicked + " "
                                    + colorPiece + "-" + pieceID + " " + newPos);

                        Client.getInstance().sendMessageToServer(move + cardClicked + " "
                                + colorPiece + "-" + pieceID + " " + newPos);

                            yourTurn = false;
                            deleteClickedData();
                        }
                    }
                }
            }
        } else {
            System.err.println("INFO not your turn or no card selected");
        }
    }

    /**
     * this is used to count a SEVE Move
     * @return number of circles clicked
     */
    private int countCirclesClicked() {
        int count = 1;
       for (int i = 0; i < totalSum; i++) {
           if (clickedCircleIds[i] == null) {
               return count;
           }
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
            deleteClickedData();
            yourTurn = false;
            setAllCardsDarkened();
        }
    }

    /**
     * delete this card instead of taking the whole round off
     * @param event
     */
    @FXML
    void skipButtonOnAction(ActionEvent event) {
        if (yourTurn) {
            if (cardClicked != null) {
                Client.getInstance().sendMessageToServer("MOVE " + cardClicked + " SKIP");
                removeCard((cardClicked));
                yourTurn = false;
                deleteClickedData();
            }
        }
    }
    /**
     * sets all cards darkened, if
     * user takes round off
     */
    private void setAllCardsDarkened() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        for(ImageView imageView : allCardImageViews) {
            if (imageView != null) {
                imageView.setEffect(colorAdjust);
            }
        }
    }

    /**
     * this method ends any blinking items in the gui when the move is sent
     */
    private void deleteClickedData() {
        //stop blinking of fading grids
        for (int i = 0; i < gridCount; i++) {
            fadingGrids[i].jumpTo(Duration.ZERO);
            fadingGrids[i].stop();
        }
        //stop blinking of fading circles
        for (int i = 0; i < circleCount; i++) {
            fadingCircles[i].jumpTo(Duration.ZERO);
            fadingCircles[i].stop();
        }

        fadingGrids = null;
        fadingCircles = null;
        clickedCircleIds = null;
        clickedGridFields = null;

        fadingGrids = new FadeTransition[7];
        fadingCircles = new FadeTransition[7];
        clickedCircleIds = new String[7];
        clickedGridFields = new FieldOnBoard[7];

        gridCount = 0;
        circleCount = 0;

        totalSum = 1;


        if (fadeTransitionCard != null) {
            fadeTransitionCard.jumpTo(Duration.ZERO);
            fadeTransitionCard.stop();
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

        // create object to translate track numbers to gui positions
        adaptToGui = new AdaptToGui();

        // initialise boolean values
        jokerClicked = false;
        yourTurn = false;
/*
        // get playerNumber of this client
        playerNr = ClientGame.getInstance().getYourPlayerNr();
        if (playerNr < 0) {
            displayInfoErrors("SEVERE ERROR couldn t find nickname in list of game names");
        }
        // set text with name

       text1.setText(Client.getInstance().getNickname());

 */
        text1.setText("Gregor");

        text1.setFont(Font.font(null, FontWeight.BOLD, 20));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        dropShadow.setRadius(3);
        dropShadow.setWidth(3);
        dropShadow.setHeight(3);
        dropShadow.setSpread(12);
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);

        if (GUIManager.getInstance().isTeamMode()) {
            dropShadow.setColor(Color.web(TextColors.getTeamShadowColor()[playerNr % 2]));
            text1.setFill(TextColors.getTextGradients()[playerNr % 2]);
            text1.setEffect(dropShadow);
        } else {
            dropShadow.setColor(Color.web(TextColors.getSingleShadowColor()[playerNr]));
            text1.setFill(Color.web(TextColors.getSingleTextColor()[playerNr]));
            text1.setEffect(dropShadow);
        }
        text1.setEffect(dropShadow);
        text1.setStroke(Color.BLACK);
        // set player labels

        /*
        setPlayerLabels();

        // prepare click grids and circles
        fadingGrids = new FadeTransition[7];
        fadingCircles = new FadeTransition[7];
        clickedGridFields = new FieldOnBoard[7];
        clickedCircleIds = new String[7];
        gridCount = 0;
        circleCount = 0;

        // set circles on home
        setOnHome();

        // set up an array with imageViews for cards
        setAllCardImageViews();

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

        if (GUIManager.getInstance().isTeamMode()) {
            Stop[] stops1 = new Stop[] { new Stop(0, Color.YELLOW), new Stop(1, Color.BLUE), new Stop(2, Color.MEDIUMVIOLETRED)};
            LinearGradient lgTeamYellowBlue = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops1);

            Stop[] stops2 = new Stop[] { new Stop(0, Color.RED), new Stop(1, Color.GREEN), new Stop(2, Color.GREENYELLOW)};
            LinearGradient lgTeamGreenRed = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops2);


            labelPlayer0.setBackground(new Background(
            new BackgroundFill(lgTeamYellowBlue, new CornerRadii(5.0), new Insets(-5.0))));
            labelPlayer2.setBackground(new Background(
                    new BackgroundFill(lgTeamYellowBlue,
                            new CornerRadii(5.0), new Insets(-5.0))));

            labelPlayer1.setBackground(new Background(
                    new BackgroundFill(lgTeamGreenRed,
                            new CornerRadii(5.0), new Insets(-5.0))));
            labelPlayer3.setBackground(new Background(
                    new BackgroundFill(lgTeamGreenRed,
                            new CornerRadii(5.0), new Insets(-5.0))));
        } else {
            labelPlayer0.setBackground(new Background(
                    new BackgroundFill(Color.YELLOW,
                            new CornerRadii(5.0), new Insets(-5.0))));
            labelPlayer1.setBackground(new Background(
                    new BackgroundFill(Color.LAWNGREEN,
                            new CornerRadii(5.0), new Insets(-5.0))));
            labelPlayer2.setBackground(new Background(
                    new BackgroundFill(Color.CORNFLOWERBLUE,
                            new CornerRadii(5.0), new Insets(-5.0))));
            labelPlayer3.setBackground(new Background(
                    new BackgroundFill(Color.INDIANRED,
                            new CornerRadii(5.0), new Insets(-5.0))));
        }
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
                circle.setEffect(getDropshadow());
                gridPane.add(circle, homeArray[count].getX(), homeArray[count].getY());
                count++;
            }
        }
    }

    /**
     * returns a dropshadow used for circles
     * @return dropshadow instance
     */
    private DropShadow getDropshadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(-4);
        dropShadow.setOffsetY(0);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        return dropShadow;
    }

    /**
     * client game sends the cards for this round to the gui here
     * @param cards these cards are the hand for this round
     */
    public void setHand(String[] cards) {
        displayInfoErrors("INFO new cards displaying");
        this.cardArray = cards;
        //set cards invisible if they are not used this round
        setAllCArdImageViewsInvisible();

        // take off darkened mode
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);

        int count = 0;
        for(String card : cardArray) {
            String url = CardUrl.getURL(card);
            Image image = null;
            try {
                image = new Image(getClass().getResource(url).toURI().toString(), 179, 250,
                        false, false);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            allCardImageViews[count].setImage(image);
            allCardImageViews[count].setVisible(true);
            allCardImageViews[count].setEffect(colorAdjust);
            count++;
        }
    }

    /**
     * sets all imageViews invisible, so that users don`t see
     * old cards but not replaced this round
     */
    private void setAllCArdImageViewsInvisible() {
        for (ImageView imageView : allCardImageViews) {
            imageView.setVisible(false);
        }
    }

    /**
     * remove a card from deck by making card blended
     * @param card the played card
     */
    public void removeCard(String card) {
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] != null && cardArray[i].equals(card)) {
                cardArray[i] = null;
                setCardDarkened(i);
                System.out.println("card " + card + " was removed from deck");
                break;
            }
        }
    }
    /**
     * after using card set card darkened
     * @param i == number of card in array
     */
    private void setCardDarkened(int i) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        allCardImageViews[i].setEffect(colorAdjust);
    }

    /**
     * on true: allows this user to make a move
     * @param value true, if it`s his or her turn
     */
    public void setYourTurn(boolean value) {
        this.yourTurn = value;
        if (yourTurn) {
            displayGameInfo("it is your turn");
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
    public void makeTrackMove(int playerNr, int pieceID, int newPosition) {
        String circleID = getCircleID(playerNr, pieceID);
        FieldOnBoard newPos = adaptToGui.getTrack(newPosition);
        makeSingleMove(circleID, newPos);
    }

    /**
     * returns the ID given to the piece in gui at the beginning(by command setOnHome())
      * @param playerNumber 0-3
     * @param pieceID 0-3
     * @return circleID for circle in gui
     */
    private String getCircleID(int playerNumber, int pieceID) {
        return "" + ((playerNumber * 4) + pieceID);
    }

    /**
     * make a single move
     * @param circleID 0 - 15
     * @param newPos FieldOnBoard(x,y)
     */
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
     * @param text format "YELO-1 GREN-4"
     */
    public void makeJackMove(String text) {
        int playerID1 = GuiParser.getNumber(text.substring(0,4));
        int playerID2 = GuiParser.getNumber(text.substring(7,11));

        int pieceID1 = (text.charAt(5) - 48);
        int pieceID2 = (text.charAt(12) - 48);

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
        String circleID = getCircleID(playerNumber, pieceID);
        FieldOnBoard heavenField = adaptToGui.getHeavenField(playerNumber, newPos);
        makeSingleMove(circleID,heavenField);
    }

    /**
     * sends a piece to home field(usually this is used if a piece is sent home)
     * @param playerNumber int between 0-3
     * @param pieceID int between 1-4
     */
    public void makeHomeMove(int playerNumber, int pieceID) {
        String circleID = getCircleID(playerNumber, pieceID);
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
        if (card.equals("SEVE")) {
            totalSum = 7;
        }
        if (card.equals("JACK")) {
            totalSum = 2;
        }
        allCardsDialog.close();
        imageViewCard7.setImage(new Image(CardUrl.getURL(card).toString()));
        imageViewCard7.setVisible(true);
    }

    /**
     * displays infos from Server
     * @param message the given message
     */
    public void displayGameInfo(String message) {
        textLogServer.appendText(message + "\n");
    }

    /**
     * displays important info from client to the gui(instead of commandline)
     * @param message from client methods
     */
    public void displayInfoErrors(String message) {
        textLogClient.appendText(message + "\n");
    }

    /**
     * display a message from another participant of the game
     * @param message from participant
     */
    public void displayPCHTmsg(String message) {
        messageReceiveTextArea.appendText(message + "\n");
    }

    /**
     * by clicking enter a PCHT-message is sent to server
     * @param event enter on keyboard
     */
    @FXML
    void onEnterPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            Client.getInstance().sendMessageToServer("PCHT " + sendMessageTextField.getText());
            sendMessageTextField.clear();
        }
    }

    /**
     * click refresh button to delete all
     * clicked circles and grid
     * @param event click
     */
    @FXML
    void refreshButtonOnAction(ActionEvent event) {
        deleteClickedData();
    }

    /**
     * declares that the submitted name won the game
     * and returns to lobby after clicking ok
     * @param winner name
     */
    public void declareVictory(String winner) {
        Alert victoryAlert = new Alert(AlertType.INFORMATION, "winner is " + winner);
        victoryAlert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response -> returnToPubLobby());
    }

    /**
     * return to pub lobby-gui
     */
    public void returnToPubLobby() {
        GUIManager.getInstance().returnToPubLobby();
    }
}
