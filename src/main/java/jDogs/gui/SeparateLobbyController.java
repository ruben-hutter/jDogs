package jDogs.gui;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import animatefx.animation.Flash;
import jDogs.serverclient.clientside.Client;
import jDogs.serverclient.clientside.ConnectionToServerMonitor;
import java.net.URISyntaxException;
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
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SeparateLobbyController implements Initializable{

    private static final int RADIUS_CIRCLE = 10;
    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private ImageView imageView4;


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

    @FXML
    void startButtonOnAction(ActionEvent event) {
        Client.getInstance().sendMessageToServer("STAR");
        new FadeIn(startButton).setCycleCount(3).play();
    }

    @FXML
    void changeButtonOnAction(ActionEvent event) {
         blink();
        if (teamMode && namesList.size() == 4) {
            removeLabels();
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
     */
    public void displayChangedTeams(String teamCombo) {
        addPlayerArray(teamCombo);
        startFlash();
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
     * display a new player who joined open game
     * @param user
     */
    public synchronized void addPlayer(String user) {
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

    private void removeLabels() {
        ObservableList<Node> nodes = gridSeparateLobby.getChildren();
        for (int i = 0; i < nodes.size(); i++) {
           if (nodes.get(i) instanceof Label) {
               Label label1 = (Label) nodes.get(i);
               gridSeparateLobby.getChildren().remove(label1);
           }
       }
    }

    private void adjustLabels() {
        int helper = 1;
        for (int i = 0 ; i < 4; i++) {
            Label label = new Label();
            label.setId("" + helper);
            if (helper - 1 < namesList.size()) {
                label.setText(namesList.get(helper - 1));
            } else {
                label.setText("no user");
            }
            System.out.println("label " + label.getText());
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

        Image image1 = new Image(getClass().getResource("/newBlink1.png").toExternalForm());
        Image image2 = new Image(getClass().getResource("/newBlink2.png").toExternalForm());
        Image image3 = new Image(getClass().getResource("/newBlink3.png").toExternalForm());
        Image image4 = new Image(getClass().getResource("/newBlink4.png").toExternalForm());
        Image image5 = new Image(getClass().getResource("/newBlink5.png").toExternalForm());
        Image image6 = new Image(getClass().getResource("/newBlink6.png").toExternalForm());

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
        scheduledExecutorService.scheduleAtFixedRate(new Blinker(), 3000, 5000, TimeUnit.MILLISECONDS);
    }
}

