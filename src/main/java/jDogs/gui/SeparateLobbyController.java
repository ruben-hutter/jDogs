package jDogs.gui;
import animatefx.animation.Flash;
import jDogs.serverclient.clientside.Client;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.PathTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polyline;
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

    @FXML
    void startButtonOnAction(ActionEvent event) {

    }

    @FXML
    void changeButtonOnAction(ActionEvent event) {
        if (teamMode && namesList.size() == 4) {
            removeLabels();
            int helper = 0;
            if (count % 2 == 1) {
                crossChangeNames();
                removeLabels();
                adjustLabels();
                setOnStartPosition();
            }
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
     * changes labels for a cross change of teams
     */
    private void changeCrossLabels() {
        Label[] newLabels = new Label[4];
        newLabels[0] = labels[0];
        newLabels[1] = labels[2];
        newLabels[2] = labels[1];
        newLabels[3] = labels[3];
        labels = newLabels;
    }

    /**
     * updates the list after a continuous change of teams
     */
    private void changeContinuousLabels() {
        Label[] newLabels = new Label[4];
        int helper = 0;
        for (int i = 3; i < labels.length; i++) {
            newLabels[helper] = labels[i];
            helper++;
        }
       for (int i = 0; i < labels.length - 1; i++) {
           newLabels[helper] = labels[i];
           helper++;
       }
       labels = newLabels;
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
     * remove a user from this open game
     * @param user name
     */
    public void removePlayer(String user) {
        namesList.remove(user);
        for (Label label : labels) {
            if (label.getText().equals(user)) {
                System.out.println("removed " + user);
                label.setText("no user");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        namesList = new ArrayList<>();
        count = 0;
        labels = new Label[4];
        labelName.setText(Client.getInstance().getNickname());
        teamMode = true;
//TODO import TEAM MODE here

        lines = new Double[][] {{0.0,0.0,130.0,0.0}, {0.0, 0.0,
                130.0, 200.0}, { 0.0, 0.0,
                400.0, 200.0}, { 0.0, 0.0,
                400.0, 0.0}};
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
     * display green start symbol on start button
     */
    public void displayStart() {
        //TODO add this method
    }
}

