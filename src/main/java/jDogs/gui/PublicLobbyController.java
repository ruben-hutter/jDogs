package jDogs.gui;

import animatefx.animation.BounceIn;
import jDogs.serverclient.clientside.Client;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PublicLobbyController implements Initializable {

    @FXML
    ObservableList<OpenGame> openGamesList;
    @FXML
    ObservableList<Participant> playersInPubList;

    @FXML
    private MenuItem quickGuide;

    @FXML // fx:id="instructions"
    private MenuItem instructions; // Value injected by FXMLLoader

    @FXML
    private TableView<?> tableViewPlayers;
    @FXML
    private Label labelName;
    @FXML
    private TableView<?> tableViewGames;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button joinButton;

    @FXML
    private Button createButton;

    @FXML
    private TextArea displayTextArea;

    @FXML
    private TextField sendTextField;

    @FXML
    private Button sendButton;

    @FXML
    private MenuBar menuBarTop;
    private OpenGame selectedGame;
    private String selectedGameID;
    private Stage gameDialog;
    private Timeline t;

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

    /**
     * Opens a window with the game instructions
     * @param event menu selection
     */
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
     * sets up a new open game
     * @param event fires if button is pressed
     *              and a new window to create
     *              an open game window gets created
     */
    @FXML
    void createButtonOnAction(ActionEvent event) {
        FXMLLoader dialogPaneGameLoader = new FXMLLoader(getClass().getResource("/createGameWindow.fxml"));

        CreateGameWindowController createGameWindowController = dialogPaneGameLoader.getController();
        AnchorPane anchorPane = null;
        try {
            anchorPane = dialogPaneGameLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameDialog = new Stage();
        Scene gameScene = new Scene(anchorPane);
        gameDialog.setScene(gameScene);
        gameDialog.show();
    }

    /**
     *  this method receives a new openGame from the createGameWindowController
     * @param gameId this is the game name
     * @param teamMode 0 for singleMode and 1 for teamMode
     */
    public void sendNewGame(String gameId, String teamMode) {
        System.out.println("Send new game");
        Client.getInstance().sendMessageToServer("OGAM " + gameId + " " + teamMode);
        gameDialog.close();
    }

    /**
     * closes the createOpenGameWindow from the createOpenGameWindow
     */
    public void closeGameDialog() {
        gameDialog.close();
    }

    /**
     * the join button allows to send a JOIN message with the selected game to server
     * @param event fires if the join
     *              button is activated
     */
    @FXML
    void joinButtonOnAction(ActionEvent event) {
        if (selectedGameID != null) {
            new Alert(AlertType.INFORMATION, "you joined " + selectedGameID).showAndWait();
            Client.getInstance().sendMessageToServer("JOIN " + selectedGameID);
            //TODO send back from server a confirmation that the user joined
            // if this message arrives, open new scene SeparateLobby
        }
    }

    /**
     * send message by clicking button
     * @param event fires if button gets pressed
     */
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
        displayTextArea.appendText("\b" + message + "\b" + "\n");
    }

    /**
     * message displayed a public chat message
     * @param message from server for public chat
     */
    public void displayPCHTmsg(String message) {
        displayTextArea.appendText(message + "\n");
    }

    /**
     * display info message
     * @param message to be displayed
     */
    public void displayInfomsg(String message) {
        displayTextArea.appendText(message + "\n");
    }

    /**
     * display open pendent game
     * @param text game from server
     */
    public void addOpenGame(String text) {
        OpenGame newGame = GuiParser.getOpenGame(text);

        //look for game by name
        int index = -1;
        for (int i = 0; i < openGamesList.size(); i++) {
            if (openGamesList.get(i).getName().equals(newGame.getName())) {
                index = i;
                break;
            }
        }
        // suppose: if name exists, game exists;
        // delete old version of openGame and add new version
        if (index > -1) {
            openGamesList.remove(index);
        }
        // add game
        openGamesList.add(newGame);
    }

    /**
     * remove pendent game in lobby
     * @param text the open game which should be replaced
     */
    public void removeOpenGame(String text) {
        OpenGame openGame = GuiParser.getOpenGame(text);
        int row = -1;
        for (int i = 0; i < openGamesList.size(); i++) {
            if (openGamesList.get(i).getName().equals(openGame.getName())) {
                //System.out.println("remove me");
                row = i;
                break;
            }
        }
        if (row > -1) {openGamesList.remove(row);}
        else {
            System.out.println("couldn t remove " + text);
        }
    }

    /**
     * display a player's name
     * @param name name of player
     */
    public void addUser(String name) {
        System.out.println("entered add user");
        for (int i = 0; i < playersInPubList.size(); i++) {
            if (playersInPubList.get(i).getPlayer().equals(name)) {
                return;
            }
        }
        System.out.println("ADD " + name);
        playersInPubList.add(new Participant(name));
    }
    /**
     * removes a player from the playersInLobby list
     * @param name displayed name
     */
    public void removeUser(String name) {
        try {
            Participant participant = new Participant(name);
            int row = -1;
            for (int i = 0; i < playersInPubList.size(); i++) {
                if (playersInPubList.get(i).getPlayer().equals(participant.getPlayer())) {
                    row = i;
                }
            }
            playersInPubList.remove(row);

        } catch (Exception e) {
            System.err.println(e.getCause().toString() + "tried to remove non existing player " + name);
        }
    }

    /**
     * display the nickname
     * @param name new nickname
     */
    public void displayNickname(String name) {
        labelName.setText(name);
        new BounceIn(labelName).setCycleCount(3).play();
    }

    /**
     * make this blink
     */
    public void blink() {
        t.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn name = new TableColumn("id");
        TableColumn responsible = new TableColumn("responsible");
        TableColumn enlist = new TableColumn("enlist");
        TableColumn TeamMode = new TableColumn("Team");
        tableViewGames.getColumns().addAll(name,responsible,enlist,TeamMode);

        openGamesList = FXCollections.observableArrayList();

        name.setCellValueFactory(new
                PropertyValueFactory<OpenGame, String>("name"));
        responsible.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("responsible"));
        enlist.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("enlist"));
        TeamMode.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("TeamMode")
        );

        tableViewGames.setItems((ObservableList)openGamesList);

        tableViewGames.setOnMousePressed ((MouseEvent event) -> {
            if (event.getClickCount() == 1 &&
                    ((tableViewGames.getSelectionModel().getSelectedItem() != null))) {
                Object object = tableViewGames.getSelectionModel().getSelectedItem();
                selectedGame = (OpenGame) object;
                selectedGameID = selectedGame.getName();
            };});

        /**
         * tableViewPlayersInLobby: displays all players in Lobby
         */
        TableColumn player = new TableColumn("player");
        tableViewPlayers.getColumns().addAll(player);

        playersInPubList = FXCollections.observableArrayList();

        player.setCellValueFactory(new PropertyValueFactory<Participant, String>("player"));

        tableViewPlayers.setItems((ObservableList) playersInPubList);

        //add name
        labelName.setText(Client.getInstance().getNickname());



        // blinking pug

        Image image1 = new Image(getClass().getResource("/newBlink1.png").toExternalForm(),1000,1000,false,false);
        Image image2 = new Image(getClass().getResource("/newBlink2.png").toExternalForm(),1000,1000,false,false);
        Image image3 = new Image(getClass().getResource("/newBlink3.png").toExternalForm(),1000,1000,false,false);
        Image image4 = new Image(getClass().getResource("/newBlink4.png").toExternalForm(),1000,1000,false,false);
        Image image5 = new Image(getClass().getResource("/newBlink5.png").toExternalForm(),1000,1000,false,false);
        Image image6 = new Image(getClass().getResource("/newBlink6.png").toExternalForm(),1000,1000,false,false);

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
        scheduledExecutorService.scheduleAtFixedRate(new Blinker("public"), 3000, 5000, TimeUnit.MILLISECONDS);
    }
}

