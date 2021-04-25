package jDogs.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * this controller controlls a window in the game gui which pops up to choose a card for joker
 */
public class AllCardsDialogController implements Initializable {

    int count;

    @FXML
    private Button chooseButton;

    @FXML
    private ImageView cardImage;

    @FXML
    void chooseButtonOnAction(ActionEvent event) {
        String card = CardUrl.getCardNameByNumber(count % 13);
        GUIManager.getInstance().gameWindow2Controller.setCardFromJoker(card);
    }


    @FXML
    void setCardImage(MouseEvent event) {
        cardImage = new ImageView(new Image(CardUrl.getURLArray()[count]));
        count++;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        count = 0;
    }
}
