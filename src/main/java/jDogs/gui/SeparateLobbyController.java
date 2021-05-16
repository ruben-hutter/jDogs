package jDogs.gui;
import animatefx.animation.Flash;
import jDogs.serverclient.clientside.Client;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    GridPane gridSeparateLobby;

    private int count;
    private Label[] labels;
    private Double[][] lines;
    private ImageView[] imageViews;
    private boolean teamMode;
    private ArrayList<String> namesList;

    @FXML
    void startButtonOnAction(ActionEvent event) {
        for (Label label : labels) {
            System.out.println(label.toString() + label.getLayoutX() + " " +  label.getLayoutY());
        }
    }

    @FXML
    void changeButtonOnAction(ActionEvent event) {
        if (teamMode && namesList.size() == 4) {
            int helper = 0;
            if (count % 2 == 1) {
                changeCrossLabels();
                for (Double[] line : lines) {
                    Polyline polyline = new Polyline();
                    polyline.getPoints().addAll(line
                    );

                    PathTransition pathTransition = new PathTransition();
                    pathTransition.setCycleCount(1);
                    pathTransition.setDuration(Duration.seconds(3));
                    pathTransition.setAutoReverse(true);
                    pathTransition.setPath(polyline);
                    pathTransition.setNode(labels[helper]);
                    pathTransition.play();
                    System.out.println("helper " + helper + " label " + labels[helper].getText());
                    labels[helper].setLayoutX(line[2]);
                    labels[helper].setLayoutY(line[3]);
                    helper++;

                }
            } else {
                changeContinuousLabels();
                for (Double[] line : lines) {
                    Polyline polyline = new Polyline();
                    polyline.getPoints().addAll(line
                    );
                    PathTransition pathTransition = new PathTransition();
                    pathTransition.setCycleCount(1);
                    pathTransition.setDuration(Duration.seconds(3));
                    pathTransition.setAutoReverse(true);
                    pathTransition.setPath(polyline);
                    pathTransition.setNode(labels[helper]);
                    System.out.println(labels[helper].toString() +
                            " pos x " + labels[helper].getLayoutX() + " y " +
                            labels[helper].getLayoutY());
                    pathTransition.play();
                    labels[helper].setLayoutX(line[2]);
                    labels[helper].setLayoutY(line[3]);
                    helper++;
                }
            }
            startFlash();
            count++;
            Client.getInstance().sendMessageToServer("TEAM 4"
                    + "" + getParticipantsString());
        }
    }

    /**
     * returns participants as string
     * @return all participants in order in a String
     */
    private String getParticipantsString() {
        String participants = "";
        for (Label label : labels) {
            if (label.isVisible()) {
                participants += " " + label.getText();
            }
        }
        return participants;
    }

    /**
     * flash the labels
     */
    private void startFlash() {
        for (Label label : labels) {
            new Flash(label).setCycleCount(2).setDelay(Duration.seconds(3)).play();
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
        String[] names = GuiParser.getArray(teamCombo);
        adaptLabelsToNames(names);
        displayReceivedTeams();
    }

    /**
     * displays the received team combination
     */
    private void displayReceivedTeams() {
        int helper = 0;
        for(Double[] line : lines) {
            Polyline polyline = new Polyline();
            polyline.getPoints().addAll(line
            );
            PathTransition pathTransition = new PathTransition();
            pathTransition.setCycleCount(1);
            pathTransition.setDuration(Duration.seconds(3));
            pathTransition.setAutoReverse(true);
            pathTransition.setPath(polyline);
            pathTransition.setNode(labels[helper]);
            pathTransition.play();
            labels[helper].setLayoutX(line[2]);
            labels[helper].setLayoutY(line[3]);
            helper++;
        }
        startFlash();
    }

    /**
     * this method updates the labels so that they correspond to the
     * new team combination
     * @param names new team combination
     */
    private void adaptLabelsToNames(String[] names) {
        for (String name : names) {
            System.out.println("name " + name);
        }
        addNames(names);
        Label[] newLabels = new Label[names.length];
        int helper;
        int helper2 = 0;
        for (String name : names) {
            helper = 0;
            for (Label label : labels) {
                System.out.println("label " + labels[helper].getText());
                if (label.getText().equals(name)) {
                    break;
                }
                helper++;
            }
            newLabels[helper2] = labels[helper];
            helper2++;
        }
        labels = newLabels;
    }

    /**
     * adds missing names if they don t exist, delete this method later
     * @param names
     */
    private void addNames(String[] names) {
        int exists;
        for (String name : names) {
            exists = -1;
            for (Label label : labels) {
                if (label.getText().equals(name)) {
                    exists = 1;
                    break;
                }
            }

            if (exists == -1) {
               for (Label label : labels) {
                   if (!label.isVisible()) {
                       label.setText(name);
                       label.setVisible(true);
                       break;
                   }
               }
            }
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
     * display a new player who joined open game
     * @param user
     */
    public void addUser(String user) {
        int mode = 0;
        // avoid duplicates for any reason
        for (String name : namesList) {
            if(name.equals(user)) {
                mode = -1;
                System.out.println("attention duplicate " + user);
                break;
            }
        }
        if (mode == 0) {
            namesList.add(user);
            for (Label label : labels) {
                if (label.getText().equals("open place")) {
                    System.out.println("label text " + label.getText() + " id "  + label.getId());
                    label.setText(user);
                    System.out.println("added user " + user);
                    break;
                }
            }
            adjustLabels();
            setOnStartPosition();
        }
    }

    private void adjustLabels() {
        //TODO set up labels anew
        for (int i = 0 ; i < 4; i++) {
            Label label = new Label();
            label.setId("" + helper);
            label.setText("open place");
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
                label.setText("open place");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        namesList = new ArrayList<>();
        count = 0;
        labels = new Label[4];
        int helper = 1;
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

