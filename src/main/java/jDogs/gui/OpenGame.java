package jDogs.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class OpenGame {
    //name must be unique!
    private final SimpleStringProperty name;

    private final SimpleStringProperty responsible;
    private SimpleStringProperty enlist;
    private final SimpleStringProperty total;
    private Button button;

    OpenGame(String name, String responsible, String enlist, String total) {
        this.name = new SimpleStringProperty(name);
        this.responsible = new SimpleStringProperty(responsible);
        this.enlist = new SimpleStringProperty(enlist);
        this.total = new SimpleStringProperty(total);
        this.button = new Button("signIn");
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getResponsible() {
        return responsible.get();
    }

    public void setResponsible(String responsible) {
        this.responsible.set(responsible);
    }

    public String getEnlist() {
        return enlist.get();
    }

    public void setEnlist(String sumPlayers) {
        this.enlist.set(sumPlayers);
    }

    public void setTotal(String total) {
        this.total.set(total);
    }

    public String getTotal() {
        return total.get();
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }
}
