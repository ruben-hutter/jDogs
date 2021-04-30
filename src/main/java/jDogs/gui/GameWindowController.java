package jDogs.gui;

import jDogs.Alliance_4;
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
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        if (clickedNode != gridPane && cardClicked != null && yourTurn) {

            if (clickedNode instanceof Circle) {
                if (cardClicked.equals("JACK")) {
                    addToCirclesID(clickedNode);
                } else {
                    if (cardClicked.equals("SEVE")) {

                        addToCirclesID(clickedNode);

                    }
                     else {
                         addToCirclesID(clickedNode);
                    }
                }
            } else {
                if (clickedNode instanceof Pane) {
                    if (cardClicked.equals("SEVE")) {
                        addToGridFields(clickedNode);
                    } else {
                        addToGridFields(clickedNode);
                    }
                }
            }
        }
    }
    private void addToFadingGrids(Node clickedNode) {
        if (gridCount < totalSum) {
            fadingGrids[gridCount] = new FadeTransition(Duration.seconds(0.9),
                    clickedNode);
            fadingGrids[gridCount].setFromValue(1.0);
            fadingGrids[gridCount].setToValue(0.0);
            fadingGrids[gridCount].setCycleCount(Animation.INDEFINITE);
            fadingGrids[gridCount].play();

        } else {
            fadingGrids[gridCount].jumpTo(Duration.ZERO);
            fadingGrids[gridCount].stop();

            fadingGrids[gridCount] = new FadeTransition(Duration.seconds(0.9),
                    clickedNode);
            fadingGrids[gridCount].setFromValue(1.0);
            fadingGrids[gridCount].setToValue(0.0);
            fadingGrids[gridCount].setCycleCount(Animation.INDEFINITE);
            fadingGrids[gridCount].play();
        }
    }

    private void addToFadingCircles(Node clickedNode) {
        if (circleCount < totalSum) {
            fadingCircles[circleCount] = new FadeTransition(Duration.seconds(0.9),
                    clickedNode);
            fadingCircles[circleCount].setFromValue(1.0);
            fadingCircles[circleCount].setToValue(0.0);
            fadingCircles[circleCount].setCycleCount(Animation.INDEFINITE);
            fadingCircles[circleCount].play();

        } else {
            fadingCircles[circleCount].jumpTo(Duration.ZERO);
            fadingCircles[circleCount].stop();

            fadingCircles[circleCount] = new FadeTransition(Duration.seconds(0.9),
                    clickedNode);
            fadingCircles[circleCount].setFromValue(1.0);
            fadingCircles[circleCount].setToValue(0.0);
            fadingCircles[circleCount].setCycleCount(Animation.INDEFINITE);
            fadingCircles[circleCount].play();
        }
    }

    private void addToGridFields(Node clickedNode) {
        addToFadingGrids(clickedNode);

        if (gridCount < totalSum) {
            clickedGridFields[gridCount] = new FieldOnBoard(GridPane.getColumnIndex(clickedNode),
                    GridPane.getRowIndex(clickedNode));
            gridCount++;
        } else {
            clickedGridFields[gridCount] = new FieldOnBoard(GridPane.getColumnIndex(clickedNode),
                    GridPane.getRowIndex(clickedNode));
        }
    }

    private void addToCirclesID(Node clickedNode) {
        addToFadingCircles(clickedNode);

        Circle circle = (Circle) clickedNode;
        if (circleCount < totalSum) {
            clickedCircleIds[circleCount] =circle.getId();
            circleCount++;
        } else {
            clickedCircleIds[circleCount] = circle.getId();
        }
    }

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

                        //yourTurn = false;
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


        // get playerNumber of this client
        playerNr = ClientGame.getInstance().getYourPlayerNr();
        if (playerNr < 0) {
            System.err.println("SEVERE ERROR couldn t find nickname in list of game names");
        }
        // set color - string
        color = ColorAbbreviations.values()[playerNr].toString();

        // set labels on the board
        nameLabel2.setText(color.toString());
        nameLabel1.setText(Client.getInstance().getNickname());
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
     */


    /**
     * makes a move with jack. It removes circle1 from gui,
     * removes and adds circle2 to position of circle1,
     * adds circle1 to position of circle2
     * @param text format "YELO-1 GREN-4"
     */
    public void makeJackMove(String text) {
        int playerID1 = GuiParser.getNumber(text.substring(0,4));
        int playerID2 = GuiParser.getNumber(text.substring(7,11));
        System.out.println("player1 nr " +playerID1);
        System.out.println("player2 nr " +playerID2);

        int pieceID1 = (text.charAt(5) - 48);
        int pieceID2 = (text.charAt(12) - 48);
        System.out.println("pieceID1 -48 " + ((int) text.charAt(5)));
        System.out.println("pieceid2 -48 " + ((int) text.charAt(12)));

        System.out.println("pieceID 1 " +pieceID1);
        System.out.println("pieceID 2 " +pieceID2);



        String circleID1 = getCircleID(playerID1, pieceID1);
        String circleID2 = getCircleID(playerID2, pieceID2);

        System.out.println("circleid 1 " + circleID1);
        System.out.println("circleid 2 " + circleID2);


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
                    System.out.println("entered circlejack1 ");
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
                    System.out.println("entered circlejack2 ");
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
     * @param message
     */
    public void displayInfoFromServer(String message) {
        textLogServer.appendText(message + "\n");
    }

    /**
     * displays important info from client to the gui(instead of commandline)
     * @param message from client methods
     */
    public void displayInfoFromClient(String message) {
        textLogClient.appendText(message + "\n");
    }

    /**
     * display messages from public lobby (if this is necessary
     * otherwise we delete it)
     * @param message from public client
     */
    public void displayPCHTmsg(String message) {
    }

    /**
     * display a message from another participant of the game
     * @param message from participant
     */
    public void displayLCHTmsg(String message) {
        messageReceiveTextArea.appendText(message + "\n");
    }

    @FXML
    void onEnterPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            Client.getInstance().sendMessageToServer("LCHT " + sendMessageTextField.getText());
            sendMessageTextField.clear();
        }
    }
}
