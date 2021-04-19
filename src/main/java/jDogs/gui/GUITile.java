package jDogs.gui;

/**
 * this class is important to hold the relation between
 * gui-2D fields and representation of game fields
 */
public class GUITile {

    private FieldOnBoard field;
    private char type;
    //private Token token;
    private FieldOnBoard[] fieldTracks;

    GUITile() {
        //this.token = token;
    }


    public void setOnHome() {
        this.type = 'A';

    }

    public void setOnTrack(String position) {
        this.type = 'B';

    }

    public void setOnHeaven(String position) {
        this.type = 'C';
        //FieldOnBoard startPos = getTrack(token.getAlliance().getStartingPosition());
    }

    private FieldOnBoard getTrack(int number) {
        return AdaptToGui.getInstance().getTrack(number);
    }

    public char getType() {
        return type;
    }
}
