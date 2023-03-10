package jDogs.gui;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import animatefx.animation.Flash;
import jDogs.serverclient.clientside.Client;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SeparateLobbyController implements Initializable{

    private static final int RADIUS_CIRCLE = 10;
    @FXML
    private Circle circle1;
    @FXML
    private Circle circle2;
    @FXML
    private Circle circle3;
    @FXML
    private Circle circle4;


    @FXML
    private TableView<?> tableViewPlayers;
    @FXML
    private Button quitButton;
    @FXML
    private Button changeButton;
    @FXML
    private Button startButton;
    @FXML
    private TextArea displayTextArea;
    @FXML
    private TextField sendTextField;
    @FXML
    private Button sendButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MenuBar menuBarTop;

    @FXML
    private GridPane gridSeparateLobby;

    @FXML
    private Label labelName;

    private int count;
    private Label[] labels;
    private Double[][] lines;
    private ImageView[] imageViews;
    private boolean teamMode;
    private ArrayList<String> namesList;
    private RotateTransition rotate;
    private Timeline t;
    private OptionsController optionsController;

    /**
     * exit game
     * @param event on click
     */
    @FXML
    void exitMenuItemOnAction(ActionEvent event) {
        Client.getInstance().kill();
    }

    /**
     * open the options
     * @param event on click
     */
    @FXML
    void optionsOnClick(ActionEvent event) {
        Client.getInstance().sendMessageToServer("SCOR");
        try {
            Stage optionsStage = new Stage();
            FXMLLoader optionsLoader = new FXMLLoader(getClass().getResource("/fxml/options.fxml"));
            Parent root = optionsLoader.load();
            Scene optionScene = new Scene(root);
            optionsController = optionsLoader.getController();
            optionsStage.setScene(optionScene);
            optionsStage.show();
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
    /**
     * Opens a window with a quick guide
     * @param event menu selection
     */
    @FXML
    void quickGuideOnClick(ActionEvent event) {
        try {
            FXMLLoader helpLoader = new FXMLLoader(getClass().getResource("/fxml/quickGuide.fxml"));
            Parent root1 = helpLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a window with the game instructions
     * @param event menu selection
     */
    @FXML
    void openInstructions(ActionEvent event) {
        try {
            FXMLLoader helpLoader = new FXMLLoader(getClass().getResource("/fxml/helpWindow.fxml"));
            Parent root1 = helpLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * start Game
     * @param event on click
     */
    @FXML
    void startButtonOnAction(ActionEvent event) {
        Client.getInstance().sendMessageToServer("STAR");
        new FadeIn(startButton).setCycleCount(3).play();
    }

    /**
     * change teams
     * @param event on click
     */
    @FXML
    void changeButtonOnAction(ActionEvent event) {
        if (teamMode && namesList.size() == 4) {
            int helper = 0;
            if (count % 2 == 1) {
                crossChangeNames();
                removeLabels();
                adjustLabels();
                setOnStartPosition();
            } else {
                continuousChangeNames();
                removeLabels();
                adjustLabels();
                setOnStartPosition();
            }
            startFlash();
            count++;
           Client.getInstance().sendMessageToServer("TEAM 4"
                    + getParticipantsString());
       }
    }


    /**
     * remove all labels
     */
    private void removeLabels() {
        ObservableList<Node> nodes = gridSeparateLobby.getChildren();
        int lengthLabel = labels.length;
        for (int i = 0; i < lengthLabel; i++) {
            nodes.remove(labels[i]);
        }
        labels = null;
    }

    /**
     * set up new labels
     */
    private void adjustLabels() {
        int helper = 1;
        labels = new Label[4];
        for (int i = 0 ; i < 4; i++) {
            Label label = new Label();
            label.setId("" + helper);
            if (helper - 1 < namesList.size()) {
                label.setText(namesList.get(helper - 1));
            } else {
                label.setText("no user");
            }
            gridSeparateLobby.getChildren().add(label);
            labels[helper - 1] = label;
            helper++;
        }
    }

    /**
     * sets the labels on start position
     */
    private void setOnStartPosition() {
        int helper = 0;
        for(Double[] line : lines) {
            Polyline polyline = new Polyline();
            polyline.getPoints().addAll(line
            );
            PathTransition pathTransition = new PathTransition();
            pathTransition.setCycleCount(1);
            pathTransition.setDuration(Duration.seconds(3));
            pathTransition.setAutoReverse(false);
            pathTransition.setPath(polyline);
            pathTransition.setNode(labels[helper]);
            pathTransition.play();
            labels[helper].setLayoutX(line[2]);
            labels[helper].setLayoutY(line[3]);
            helper++;
        }
    }


    /**
     * change names list by 1 forward
     */
    private void continuousChangeNames() {
        ArrayList<String> newList = new ArrayList<>();
        for (int i = 1; i < namesList.size(); i++) {
            newList.add(namesList.get(i));
        }
        newList.add(namesList.get(0));
        namesList = newList;
    }

    /**
     * change names by crossing
     */
    private void crossChangeNames() {
        ArrayList<String> newList = new ArrayList<>();
        newList.add(namesList.get(0));
        newList.add(namesList.get(2));
        newList.add(namesList.get(1));
        newList.add(namesList.get(3));

        namesList = newList;
    }


    /**
     * returns participants as string
     * @return all participants in order in a String
     */
    private String getParticipantsString() {
        String participants = "";
        for (String name : namesList) {
            participants += " " + name;
        }
        return participants;
    }

    /**
     * flash the labels
     */
    private void startFlash() {
        for (Label label : labels) {
            new Flash(label).setCycleCount(1).setDelay(Duration.seconds(3)).play();
        }
    }

    /**
     * displays new team combination
     * @param teamCombo String of team combination
     */
    public void displayChangedTeams(String teamCombo) {
        addPlayerArray(teamCombo);
        startFlash();
    }

    /**
     * display a new player who joined open game
     * @param user user name
     */
    public void addPlayer(String user) {
        int exists = 1;
        for (String name : namesList) {
            if (name.equals(user)) {
                exists = 0;
                break;
            }
        }
        if (exists == 1) {
            namesList.add(user);
            removeLabels();
            adjustLabels();
            setOnStartPosition();
        } else {
            System.out.println("duplicate " + user);
        }
    }

    /**
     * adds a whole array of players
     * @param newPlayers multiple players in string
     */
    public void addPlayerArray(String newPlayers) {
        ArrayList<String> newNamesList = new ArrayList<>();
        String[] array = GuiParser.getArray(newPlayers);
        for (String name : array) {
            newNamesList.add(name);
        }
        namesList = newNamesList;
        removeLabels();
        adjustLabels();
        setOnStartPosition();
    }

    /**
     * remove a user from this open game
     * @param user name
     */
    public void removePlayer(String user) {
        if (namesList.remove(user)) {
            removeLabels();
            adjustLabels();
            setOnStartPosition();
            startFlash();
        }
    }

    @FXML
    void quitButtonOnAction(ActionEvent event) {
        Client.getInstance().sendMessageToServer("QUIT");
        GUIManager.getInstance().returnToPubLobby();
    }

    @FXML
    void sendButtonOnAction(ActionEvent event) {
        String message = sendTextField.getText();
        sendTextField.clear();
        if (message.isBlank() || message.isEmpty()) {
            //Do nothing
        } else {
            if (message.charAt(0) == '@') {
                String parsedMsg;
                if ((parsedMsg = GuiParser.sendWcht(message.substring(1))) == null) {
                    new Alert(AlertType.ERROR,
                            "wrong Wcht format entered. E.g. '@nickname message' ");
                } else {
                    displayWCHTmsg(message);
                    Client.getInstance().sendMessageToServer("WCHT " + parsedMsg);
                }
            } else {
                Client.getInstance().sendMessageToServer("PCHT " + message);
            }
        }
    }

    /**
     * message displayed as whisper chat message
     * @param message whisper chat message
     */
    public void displayWCHTmsg(String message) {
        displayTextArea.appendText(message + "\n");
    }

    /**
     * message displayed as public chat message
     * @param message public chat message
     */
    public void displayPCHTmsg(String message) {
        displayTextArea.appendText(message + "\n");
    }


    /**
     * display an info message
     * @param info message
     */
    public void displayInfomsg(String info) {
        displayTextArea.appendText(info + "\n");
    }


    /**
     * display bouncing and then green start symbol on start button
     */
    public void displayStart() {
        new BounceIn(startButton).setCycleCount(3).play();

        final Rectangle rotatingRect = new Rectangle(5,5,10,6);
        rotatingRect.setFill(Color.GREEN);

        final Pane rectHolder = new Pane();
        rectHolder.setMinSize(20, 16);
        rectHolder.setPrefSize(20, 16);
        rectHolder.setMaxSize(20, 16);
        rectHolder.getChildren().add(rotatingRect);

        rotate = new RotateTransition(Duration.seconds(4), rotatingRect);
        rotate.setByAngle(360);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        Platform.runLater(
                () -> {
                    startButton.setGraphic(rectHolder);
                });

        rotate.play();
    }

    /**
     * cancel start of game if a user withdrew from open game
     */
    public void cancelStart() {
        rotate.stop();
    }

    /**
     * let the dog blink
     */
    public void blink() {
        t.play();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        namesList = new ArrayList<>();
        count = 0;
        labels = new Label[4];
        labelName.setText(Client.getInstance().getNickname());
        teamMode = GUIManager.getInstance().isTeamMode();
        lines = new Double[][] {{0.0,0.0,130.0,0.0}, {0.0, 0.0,
                130.0, 200.0}, { 0.0, 0.0,
                400.0, 200.0}, { 0.0, 0.0,
                400.0, 0.0}};

        optionsController = null;

        //drop shadow
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(-4);
        dropShadow.setOffsetY(0);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        if (teamMode) {
            RadialGradient rgTeam1 = new RadialGradient(0, .2, -20, -10, circle1.getRadius(),
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0,Color.web("#559869")),
                    new Stop(1, Color.web("#f06017")));


            RadialGradient rgTeam2 = new RadialGradient(0, .1, -10, -10, circle1.getRadius(),
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0,Color.web("#d2b710")),
                    new Stop(1, Color.web("#10a1d2")));

            circle1.setFill(rgTeam2);
            circle1.setStroke(Color.YELLOWGREEN);
            circle1.setStrokeWidth(2);
            circle1.setEffect(dropShadow);

            circle2.setFill(rgTeam1);
            circle2.setStroke(Color.GREEN);
            circle2.setEffect(dropShadow);
            circle2.setStrokeWidth(2);

            circle3.setFill(rgTeam2);
            circle3.setStroke(Color.YELLOWGREEN);
            circle3.setEffect(dropShadow);
            circle3.setStrokeWidth(2);

            circle4.setFill(rgTeam1);
            circle4.setStroke(Color.GREEN);
            circle4.setEffect(dropShadow);
            circle4.setStrokeWidth(2);
        } else {
            circle1.setFill(Color.web("#D2B710"));
            circle1.setEffect(dropShadow);

            circle2.setFill(Color.web("#559869"));
            circle2.setEffect(dropShadow);

            circle3.setFill(Color.web("#10A1D2"));
            circle3.setEffect(dropShadow);

            circle4.setFill(Color.web("#F06017"));
            circle4.setEffect(dropShadow);
        }

        // blinking pug

        Image image1 = new Image(getClass().getResource("/images/newBlink1.png").toExternalForm());
        Image image2 = new Image(getClass().getResource("/images/newBlink2.png").toExternalForm());
        Image image3 = new Image(getClass().getResource("/images/newBlink3.png").toExternalForm());
        Image image4 = new Image(getClass().getResource("/images/newBlink4.png").toExternalForm());
        Image image5 = new Image(getClass().getResource("/images/newBlink5.png").toExternalForm());
        Image image6 = new Image(getClass().getResource("/images/newBlink6.png").toExternalForm());

        ImageView imageView1 = new ImageView(image1);
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image3);
        ImageView imageView4 = new ImageView(image4);
        ImageView imageView5 = new ImageView(image5);
        ImageView imageView6 = new ImageView(image6);

        Group groupMops = new Group();
        //anchorPane.getChildren().add(groupMops);
        anchorPane.getChildren().add(groupMops);
        groupMops.getChildren().addAll(imageView1);
        t = new Timeline();
        t.setCycleCount(2);

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(200),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView2);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(300),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView3);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(400),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView4);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(500),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView5);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(600),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView6);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(700),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView5);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(800),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView4);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(900),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView3);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(1000),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView2);
                }
        ));

        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(1100),
                (ActionEvent event) -> {
                    groupMops.getChildren().setAll(imageView1);
                }
        ));

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Blinker("separate"), 3000, 5000, TimeUnit.MILLISECONDS);
    }

    public OptionsController getOptionsController() {
        return optionsController;
    }
}

