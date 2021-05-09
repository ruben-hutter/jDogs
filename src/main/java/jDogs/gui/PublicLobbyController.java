package jDogs.gui;

import jDogs.serverclient.clientside.Client;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.checkerframework.checker.units.qual.A;

/**
 * this class represents the lobby window
 */
public class PublicLobbyController implements Initializable {
    @FXML
    ObservableList<OpenGame> openGames;
    @FXML
    ObservableList<Participant> playersInLobby;

    private OpenGame selectedGame;
    private String gameId;
    private Stage gameDialog;
    boolean startGamePossible;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.startGamePossible = false;
        this.gameId = null;

        /**
         * TableViewActiveGames displays all active games
         */
        TableColumn name = new TableColumn("id");
        TableColumn responsible = new TableColumn("responsible");
        TableColumn enlist = new TableColumn("enlist");
        TableColumn total = new TableColumn("total");
        TableColumn TeamMode = new TableColumn("Team");
        tableViewActiveGames.getColumns().addAll(name,responsible,enlist,total,TeamMode);

        openGames = FXCollections.observableArrayList();

        name.setCellValueFactory(new
                PropertyValueFactory<OpenGame, String>("name"));
        responsible.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("responsible"));
        enlist.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("enlist"));
        total.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("total"));
        TeamMode.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("TeamMode")
        );

        tableViewActiveGames.setItems((ObservableList)openGames);

        tableViewActiveGames.setOnMousePressed ((MouseEvent event) -> {
            if (event.getClickCount() == 1 &&
                    ((tableViewActiveGames.getSelectionModel().getSelectedItem() != null))) {
                    Object object = tableViewActiveGames.getSelectionModel().getSelectedItem();
                    selectedGame = (OpenGame) object;
            };});

        /**
         * tableViewPlayersInLobby: displays all players in Lobby
         */

        TableColumn player = new TableColumn("player");
        tableViewActPlayers.getColumns().addAll(player);

        playersInLobby = FXCollections.observableArrayList();

        player.setCellValueFactory(new PropertyValueFactory<Participant, String>("player"));


        tableViewActPlayers.setItems((ObservableList) playersInLobby);

    }

    @FXML
    private Button startButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button joinButton;

    @FXML
    private TableView<String> tableViewActPlayers;


    @FXML
    private TableView<String> tableViewActiveGames;

    @FXML
    private TextArea publicChatMessagesLobby;

    @FXML
    private TextField messageFieldLobby;

    @FXML
    private Button sendButtonLobby;

    @FXML
    private MenuItem exitGameLobby;

    @FXML
    private TextField textField;

    /**
     * start game button
     * @param event fires if the start game button is pressed
     */
    @FXML
    void startButtonOnAction(ActionEvent event) {
        if (startGamePossible) {
            Client.getInstance().sendMessageToServer("STAR");
            //set everything up to game mode
            startButton.setText("started");
            startGamePossible = false;
        }
    }

    /**
     * sets up a new open game
     * @param event fires if button is pressed
     *              and a new window to create
     *              an open game window gets created
     */
    @FXML
    void newGameButtonOnAction(ActionEvent event) {

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
     * leave separate lobby mode(leave open game)
     * @param event fires if client wants
     *             to leave separate lobby command
     */
    @FXML
    void setQButtonOnAction(ActionEvent event) {
        if(gameId != null) {
            gameId = null;
            new Alert(AlertType.INFORMATION, "you quit game and are public" ).showAndWait();
            Client.getInstance().sendMessageToServer("QUIT");
            playersInLobby.removeAll();
        } else {
            new Alert(AlertType.INFORMATION, "Error.you were already public" ).showAndWait();
        }
    }

    /**
     * the join button allows to join an
     * open game/separate lobby
     * @param event fires if the join
     *              button is activated
     */
    @FXML
    void setJButtonOnAction(ActionEvent event) {
        if (selectedGame != null && tableViewActiveGames.getSelectionModel().getSelectedItem()!= null) {
            new Alert(AlertType.INFORMATION, "you joined " + selectedGame.getName()).showAndWait();
            gameId = selectedGame.getName();
            Client.getInstance().sendMessageToServer("JOIN " + gameId);

        } else {
            new Alert(AlertType.INFORMATION, "you did not select a game");
        }
    }

    /**
     * shutdown client by closing window
     * @param event fires if client
     *              closes the window
     */
    @FXML
    void exitJDogs(ActionEvent event) {
        System.out.println("exit..");
        System.exit(-1);
    }

    /**
     * send message by clicking button
     * @param event fires if button gets pressed
     */
    @FXML
    void sendButtonOnAction(ActionEvent event) {
        System.out.println("send..");
        String message = messageFieldLobby.getText();
        messageFieldLobby.clear();
        //System.out.println(message);

        if (message.isEmpty() || message.isBlank()) {
            //not allowed to send
        } else {
            System.out.println(message + " : " + message.charAt(0));
            if (message.charAt(0) == '@') {
                String parsedMsg = null;
                // if users are in a separate lobby they can send messages to public with "@PUB"
                if (message.length() > 5 && message.substring(1,4).equals("PUB")) {
                    Client.getInstance().sendMessageToServer("PCHT " + message.substring(5));
                } else {
                    if ((parsedMsg = GuiParser.sendWcht(message.substring(1))) == null) {
                        new Alert(AlertType.ERROR,
                                "wrong Wcht format entered. E.g. '@nickname message' ");
                    } else {
                        displayWCHTmsg(message);
                        Client.getInstance().sendMessageToServer("WCHT " + parsedMsg);
                    }
                }

            } else {
                if (gameId != null) {
                    // you send to separate lobby
                    Client.getInstance().sendMessageToServer("LCHT " + message);
                } else {
                    // you are in public lobby
                    Client.getInstance().sendMessageToServer("PCHT " + message);
                }

            }
        }
    }

    /**
     * all display methods enable Input via GUI-Manager from Server or local
     */

    /**
     *
     * @param message from server for public chat
     */

    public void displayPCHTmsg(String message) {
        //System.out.println(message);
        publicChatMessagesLobby.appendText("@PUB " + message + "\n");
    }

    /**
     * message displayed as lobby chat message
     * @param message lobby chat message
     */
    public void displayLCHTmsg(String message) {
        publicChatMessagesLobby.appendText("@LOBBY " + message + "\n");
    }

    /**
     * message displayed as whisper chat message
     * @param message whisper chat message
     */
    public void displayWCHTmsg(String message) {
        publicChatMessagesLobby.appendText(message + "\n");
    }

    /**
     * display info message
     * @param info info message
     */
    public void displayInfomsg(String info) {
        publicChatMessagesLobby.appendText(info + "\n");
    }

    /**
     * display all player in lobby
     * @param player in lobby
     */
    public void displayPlayersInLobby(String player) {
        playersInLobby.add(new Participant(player));

    }

    /**
     * display open pendent game
     * @param game from server
     */
    public void displayPendentGameInLobby(String game) {
        OpenGame newGame = GuiParser.getOpenGame(game);

        if (newGame.getResponsible().equals(Client.getInstance().getNickname())) {
            gameId = newGame.getName();
        }

        //look for game by name
        int index = -1;
        for (int i = 0; i < openGames.size(); i++) {
            if (openGames.get(i).getName().equals(newGame.getName())) {
                index = i;
                break;
            }
        }
        // suppose: if name exists, game exists;
        // only refresh enlist number
        if (index > -1) {
            openGames.remove(index);
        }

            // name doesn`t exist, game doesn`t exist;
            // add game
            openGames.add(newGame);
    }

    /**
     * remove pendent game in lobby
     * @param substring the open game which should be replaced
     */
    public void removePendentGameInLobby(String substring) {
        OpenGame openGame = GuiParser.getOpenGame(substring);
        int row = -1;

        for (int i = 0; i < openGames.size(); i++) {
            if (openGames.get(i).getName().equals(openGame.getName())) {
                //System.out.println("remove me");
                row = i;
                break;
            }
        }
        /*tableViewActiveGames.getSelectionModel().select(row);
        Object object = tableViewActiveGames.getSelectionModel().getSelectedItem();
        OpenGame openGame1 = (OpenGame) object;
        tableViewActiveGames.getItems().remove(openGame1);
         */

        openGames.remove(row);


    }

    /**
     * display ongoing game in lobby
     * @param game to parse first
     */
    public void displayOnGoingGameInLobby(String game) {

    }

    public void displayPlayer(String player) {
        for (int i = 0; i < playersInLobby.size(); i++) {
            if (playersInLobby.get(i).getPlayer().equals(player)) {
                return;
            }
        }
        playersInLobby.add(new Participant(player));
    }

    /**
     * removes a player from the playersInLobby list
     * @param player displayed name
     */
    public void removePlayer(String player) {
        try {
            Participant participant = new Participant(player);

            int row = -1;
            for (int i = 0; i < playersInLobby.size(); i++) {
                if (playersInLobby.get(i).getPlayer().equals(participant.getPlayer())) {
                        //playersInLobby.remove(i);
                        //Participant player1 = playersInLobby.get(i);
                        //tableViewActPlayers.getItems().remove(player1);
                        //System.out.println(tableViewActPlayers.getItems().remove(playersInLobby.get(i)));
                    row = i;
                }
            }
            playersInLobby.remove(row);
            //tableViewActPlayers.getItems().clear();
            //playersInLobby.remove(new Participant(player));


        } catch (Exception e) {
            System.err.println("tried to remove non existing player " + player);
        }
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
     * closes the window to create an open game
     */
    public void closeGameDialog() {
        System.out.println("close game dialog window");
        gameDialog.close();
    }

    /**
     * method to set inform gui that it joined separate lobby
     * @param game string from server
     */
    public void goToSeparateLobby(String game) {
        gameId = game;
        displayInfomsg("INFO you joined game " + gameId);
        //show only players in separate lobby
        for (int i = 0; i < playersInLobby.size(); i++) {
            playersInLobby.remove(i);
        }
        Client.getInstance().sendMessageToServer("LPUB");
    }



    /**
     * method to allow to start the game
     */
    public void startGameConfirmation() {
        startGamePossible = true;
        /**
         * this code represents a rotating rectangle, which is rotating when the game is ready to start
         */
        final Rectangle rotatingRect = new Rectangle(5,5,10,6);
        rotatingRect.setFill(Color.GREEN);

        final Pane rectHolder = new Pane();
        rectHolder.setMinSize(20, 16);
        rectHolder.setPrefSize(20, 16);
        rectHolder.setMaxSize(20, 16);
        rectHolder.getChildren().add(rotatingRect);

        final RotateTransition rotate = new RotateTransition(Duration.seconds(4), rotatingRect);
        rotate.setByAngle(360);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        Platform.runLater(
                () -> {
                    startButton.setGraphic(rectHolder);
                });

        rotate.play();
    }
}
