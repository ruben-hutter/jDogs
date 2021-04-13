package jDogs.gui;

public class GUITile {

    private FieldOnBoard field;
    private char type;
    private Token token;
    private FieldOnBoard[] fieldTracks;

    GUITile(Token token) {
        this.token = token;
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
