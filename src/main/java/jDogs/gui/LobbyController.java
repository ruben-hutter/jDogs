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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.checkerframework.checker.units.qual.A;

public class LobbyController implements Initializable {
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

    @FXML
    void startButtonOnAction(ActionEvent event) {
        if (startGamePossible) {
            Client.getInstance().sendMessageToServer("STAR " + gameId);
            //set everything up to game mode
            startButton.setText("joined");
            startGamePossible = false;
        }

    }

    @FXML
    void newGameButtonOnAction(ActionEvent event) {
        System.out.println("started new game action");
        String dialogPath = "src/main/resources/createGameWindow.fxml";
        URL url = null;
        try {
            url = Paths.get(dialogPath).toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FXMLLoader dialogPaneGameLoader = new FXMLLoader(url);

        CreateGameWindowController createGameWindowController = dialogPaneGameLoader.getController();
        DialogPane dialogPane = null;
        try {
            dialogPane = dialogPaneGameLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameDialog = new Stage();
        Scene gameScene = new Scene(dialogPane);
        gameDialog.setScene(gameScene);
        gameDialog.show();

    }


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

    @FXML
    void exitJDogs(ActionEvent event) {
        System.out.println("exit..");
        System.exit(-1);
    }

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

    public void displayLCHTmsg(String message) {
        publicChatMessagesLobby.appendText("@LOBBY " + message + "\n");
    }


    public void displayWCHTmsg(String message) {
        publicChatMessagesLobby.appendText(message + "\n");
    }

    public void displayInfomsg(String info) {
        publicChatMessagesLobby.appendText(info + "\n");
    }

    public void displayPlayersInLobby(String player) {
        playersInLobby.add(new Participant(player));

    }

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


    public void removePendentGameInLobby(String substring) {
        OpenGame openGame = GuiParser.getOpenGame(substring);
        System.out.println("remove game " + substring);
        displayInfomsg("INFO removed game " + openGame.getName());

        tableViewActiveGames.getSelectionModel().select(0);
        Object object = tableViewActiveGames.getSelectionModel().getSelectedItem();
        OpenGame openGame1 = (OpenGame) object;
        tableViewActiveGames.getItems().remove(openGame1);

    }

    public void displayOnGoingGamesInLobby(String games) {

    }

    public void displayPlayerinPublic(String player) {
        for (int i = 0; i < playersInLobby.size(); i++) {
            if (playersInLobby.get(i).getPlayer().equals(player)) {
                return;
            }
        }
        playersInLobby.add(new Participant(player));
    }

    public void removePlayerinPublic(String player) {
        try {


                for (int i = 0; i < playersInLobby.size(); i++) {
                    if (playersInLobby.get(i).getPlayer().equals(player)) {
                        //playersInLobby.remove(i);
                        //Participant player1 = playersInLobby.get(i);
                        //tableViewActPlayers.getItems().remove(player1);
                        //System.out.println(tableViewActPlayers.getItems().remove(playersInLobby.get(i)));
                    }
                }
                //tableViewActPlayers.getItems().clear();
                //playersInLobby.remove(new Participant(player));
                System.out.println("after ");
                for (int i = 0; i < playersInLobby.size(); i++) {
                    System.out.println(playersInLobby.get(i));

                }

        } catch (Exception e) {
            System.err.println("tried to remove non existing player " + player);
        }
    }



    /**
     *  this method receives from the createGameWindowController
     * @param gameId this is the game name
     * @param total the total number of participants till starting the game
     * @param teamMode 0 for singleMode and 1 for teamMode
     */
    public void sendNewGame(String gameId, String total, String teamMode) {
        System.out.println("Send new game");
        Client.getInstance().sendMessageToServer("OGAM " + gameId + " " + total + " " + teamMode);
        gameDialog.close();
    }

    public void closeGameDialog() {
        System.out.println("close game dialog window");
        gameDialog.close();
    }

    public void goToSeparateLobbyGame(String game) {
        gameId = game;
        displayInfomsg("INFO you joined game " + gameId);
        //show only players in separate lobby
        playersInLobby.removeAll();
    }

    public void startGameConfirmation() {
        startGamePossible = true;
        /**
         * this code represents a rotating rectangle, that is rotating when the game is ready to start
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

        startButton.setGraphic(rectHolder);

        rotate.play();

    }


    public void startGame(String gameInfo) {
        GUIManager.getInstance().startGame();
    }
}
