package jDogs.gui;

import jDogs.serverclient.clientside.Client;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class LobbyController implements Initializable {
    @FXML
    ObservableList<OpenGame> openGames;
    @FXML
    ObservableList<Participant> playersInLobby;

    private OpenGame selectedGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /**
         * TableViewActiveGames displays all active games
         */
        TableColumn name = new TableColumn("id");
        TableColumn responsible = new TableColumn("responsible");
        TableColumn enlist = new TableColumn("enlist");
        TableColumn total = new TableColumn("total");
        TableColumn signIn = new TableColumn("signIn");
        tableViewActiveGames.getColumns().addAll(name,responsible,enlist,total,signIn);

        openGames = FXCollections.observableArrayList();

        name.setCellValueFactory(new
                PropertyValueFactory<OpenGame, String>("name"));
        responsible.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("responsible"));
        enlist.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("enlist"));
        total.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("total"));
        signIn.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("button")
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
        TableColumn pM = new TableColumn("pM");
        tableViewActPlayers.getColumns().addAll(player,pM);

        playersInLobby = FXCollections.observableArrayList();

        player.setCellValueFactory(new PropertyValueFactory<Participant, String>("player"));
        pM.setCellValueFactory(new PropertyValueFactory<Participant, Button>("button"));

        tableViewActPlayers.setItems((ObservableList) playersInLobby);

    }

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
    void setJButtonOnAction(ActionEvent event) {
        if (selectedGame != null && tableViewActiveGames.getSelectionModel().getSelectedItem()!= null) {
            System.out.println("you joined " + selectedGame.getName());
        } else {
            System.out.println("no game selected");
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
        Client.getInstance().sendMessageToServer("PCHT " + message);
    }



    public void displayPCHTmsg(String message) {
        //System.out.println(message);
        publicChatMessagesLobby.appendText(message + "\n");
    }


    public void displayWCHTmsg(String message) {
        System.out.println(message + " wcht not implemented");
    }

    public void displayInfomsg(String info) {

    }

    public void displayPlayersInLobby(String player) {
        playersInLobby.add(new Participant(player));

    }

    public void displayPendentGameInLobby(String game) {
        OpenGame newGame = GuiParser.getOpenGame(game);
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
            openGames.get(index).setEnlist(newGame.getEnlist());
        } else {
            // name doesn`t exist, game doesn`t exist;
            // add game
            openGames.add(newGame);}

    }

    public void displayOnGoingGamesInLobby(String games) {

    }

    public void displayPlayersInJDogs(String players) {

    }


}