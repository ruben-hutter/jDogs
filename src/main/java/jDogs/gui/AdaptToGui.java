package jDogs.gui;

public class AdaptToGui {
    private int boardSize;
    private FieldOnBoard[] fieldsOnTrack;
    private static AdaptToGui instance;

    public AdaptToGui(int boardSize) {
        this.boardSize = boardSize;
       fieldsOnTrack = new FieldOnBoard[this.boardSize * 16];
       createFieldsOnTrack(boardSize);
       instance = this;
    }

    public static AdaptToGui getInstance() {
        return instance;
    }

    private void createFieldsOnTrack(int boardSize) {
        //side one above (2/3,2/4.....2/18)
        // last position is a new startingPosition
        int trackCounter = 0;
        int leftestPoint = 2;
        int rightestPoint = 18;
        int highestPoint = 2;
        int lowestPoint = 18;

        //side up (2/3,2/4...2/18)

        for (int i = 1; i < 17; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(highestPoint, leftestPoint + i);
            System.out.println(fieldsOnTrack[trackCounter].getX() + " " + fieldsOnTrack[trackCounter].getY());
            trackCounter++;

        }

        //right side(3/18, 4/18.....18/18)

        for (int i = 1; i < 17; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(highestPoint + i, rightestPoint);
            System.out.println(fieldsOnTrack[trackCounter].getX() + " " + fieldsOnTrack[trackCounter].getY());
            trackCounter++;
        }

        //side down (18/17, 18/16,...18/2)

        for (int i = 1; i < 17; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(lowestPoint, rightestPoint - i);
            System.out.println(fieldsOnTrack[trackCounter].getX() + " " + fieldsOnTrack[trackCounter].getY());
            trackCounter++;
        }

        // left side (17/2, 16/2...2/2)

        for (int i = 1; i < 17; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(lowestPoint - i, leftestPoint);
            System.out.println(fieldsOnTrack[trackCounter].getX() + " " + fieldsOnTrack[trackCounter].getY());
            trackCounter++;
        }
    }

    public FieldOnBoard getTrack(int number) {
       if (number > fieldsOnTrack.length) {
           return null;
       }

       return fieldsOnTrack[number];
    }
}
