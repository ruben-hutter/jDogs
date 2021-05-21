package jDogs.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 * this class represents open game objects and makes it possible
 * to display them in the lobby window
 */

public class OpenGame {
    private final SimpleStringProperty name;
    private final SimpleStringProperty responsible;
    private SimpleStringProperty enlist;
    private final SimpleStringProperty total;
    private SimpleStringProperty teamMode;

    /**
     * constructor of open game
     * @param name of game
     * @param responsible host
     * @param enlist how many are enlisted 0 - 4
     * @param total 4
     * @param teamMode 0, 1
     */
    OpenGame(String name, String responsible, String enlist, String total, String teamMode) {
        this.name = new SimpleStringProperty(name);
        this.responsible = new SimpleStringProperty(responsible);
        this.enlist = new SimpleStringProperty(enlist);
        this.total = new SimpleStringProperty(total);
        this.teamMode = new SimpleStringProperty(teamMode);
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

    public void setTeamMode(String teamMode) {
        this.teamMode.set(teamMode);
    }

    public String getTeamMode() {
        return teamMode.get();
    }
}
