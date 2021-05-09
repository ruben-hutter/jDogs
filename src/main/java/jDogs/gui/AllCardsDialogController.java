package jDogs.gui;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
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

    private int count;

    @FXML
    private Button chooseButton;

    @FXML
    private ImageView cardImage;

    @FXML
    void chooseButtonOnAction(ActionEvent event) {
        String card = CardUrl.getCardNameByNumber(count % 13);
        GUIManager.getInstance().gameWindowController.setCardFromJoker(card);
    }


    @FXML
    void setCardImage(MouseEvent event) {
        count++;
        String card = CardUrl.getCardNameByNumber(count % 13);
        String url = CardUrl.getURL(card);
        Image image = null;
        try {
            image = new Image(getClass().getResource(url).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        cardImage.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        count = 0;
        String card = CardUrl.getCardNameByNumber(count);
        String url = CardUrl.getURL(card);
        Image image = null;
        try {
            image = new Image(getClass().getResource(url).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        cardImage.setImage(image);
    }
}
