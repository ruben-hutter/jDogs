package jDogs.gui;

import jDogs.serverclient.clientside.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class PublicLobbyController implements Initializable {
    @FXML
    ObservableList<OpenGame> openGamesList;
    @FXML
    ObservableList<Participant> playersInPubList;
    @FXML
    private TableView<?> tableViewPlayers;

    @FXML
    private TableView<?> tableViewGames;


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
    private boolean isStartPossible;
    private OpenGame selectedGame;
    private String selectedGameID;

    @FXML
    void createButtonOnAction(ActionEvent event) {

    }

    @FXML
    void joinButtonOnAction(ActionEvent event) {

    }


    @FXML
    void sendButtonOnAction(ActionEvent event) {
        String message = sendTextField.getText();
        sendTextField.clear();
        if (message.isBlank() || message.isEmpty()) {
            //Do nothing
        } else {
            if (message.charAt(0) == '@') {

            } else {
                Client.getInstance().sendMessageToServer("PCHT " + message);
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.isStartPossible = false;

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
    }
}

