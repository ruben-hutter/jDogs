package jDogs.gui;

import animatefx.animation.BounceIn;
import jDogs.serverclient.clientside.Client;
import jDogs.serverclient.serverside.SavedUser;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class OptionsController implements Initializable {


    @FXML
    private TableView<?> tableViewHighScore;

    @FXML
    private Pane pane;

    @FXML
    private TextField textFieldName;

    @FXML
    private Text textDisplay;

    @FXML
    private ObservableList<SavedUserGui> highScoreList;

    @FXML
    void changeNameButtonOnAction(ActionEvent event) {
        if (GUIManager.getInstance().getState().equals("publicLobby")) {
            String name = textFieldName.getText();
            if (!name.isEmpty() && !name.isBlank()) {
                Client.getInstance().sendMessageToServer("USER " + name);
            }
            textFieldName.clear();
        }
    }

    /**
     * update highScoreList
     * @param list whole HighScoreList from server
     */
    public void updateHighScoreList(String list) {
        highScoreList.removeAll();
        int pos = 0;
        for (int i = 0; i < list.length(); i++) {
            if (Character.isWhitespace(list.charAt(i))) {
                SavedUserGui savedUserGui = GuiParser.getSavedUserGui(list.substring(pos, i));
                pos = i + 1;
                highScoreList.add(savedUserGui);
            }
        }
        SavedUserGui savedUserGui = GuiParser.getSavedUserGui(list.substring(pos));
        highScoreList.add(savedUserGui);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textDisplay.setText("Your name: " + Client.getInstance().getNickname());

        TableColumn name = new TableColumn("name");
        TableColumn playedGames = new TableColumn("playedGames");
        TableColumn victories = new TableColumn("victories");
        tableViewHighScore.getColumns().addAll(name,playedGames,victories);

        highScoreList = FXCollections.observableArrayList();

        name.setCellValueFactory(new
                PropertyValueFactory<OpenGame, String>("name"));
        playedGames.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("playedGames"));
        victories.setCellValueFactory(
                new PropertyValueFactory<OpenGame, String>("victories"));

        tableViewHighScore.setItems((ObservableList)highScoreList);
    }

    /**
     * update nickname and display
     * @param name name
     */
    public void updateNickname(String name) {
        textDisplay.setText("Your name: " + name);
        new BounceIn(textDisplay).setCycleCount(2).play();
    }
}
