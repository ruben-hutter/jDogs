package jDogs.gui;

public class AdaptToGui {
    private int boardSize;
    private FieldOnBoard[] fieldsOnTrack;
    private FieldOnBoard[][] fieldsOnHeaven;
    private FieldOnBoard[][] fieldsOnHome;
    private final int NUM_PIECES = 4;

    private static AdaptToGui instance;

    public AdaptToGui(int boardSize) {
        this.boardSize = boardSize;
       createFieldsOnTrack(boardSize);
       instance = this;
    }

    public static AdaptToGui getInstance() {
        return instance;
    }

    private void createFieldsOnTrack(int boardSize) {
        // board size is 4 or 6
        fieldsOnTrack = new FieldOnBoard[this.boardSize * 16];

        // first position is a new starting position

        int trackCounter = 0;
        int leftestPoint = 2;
        int rightestPoint = 18;
        int highestPoint = 2;
        int lowestPoint = 18;

        //side up (2/2,2/3...2/17)

        for (int i = 0; i < 16; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(highestPoint, leftestPoint + i);
            trackCounter++;

        }

        //right side(2/18, 3/18.....18/17)

        for (int i = 0; i < 16; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(highestPoint + i, rightestPoint);
            trackCounter++;
        }

        //side down (18/18, 18/16,...18/3)

        for (int i = 0; i < 16; i++) {

            fieldsOnTrack[trackCounter] = new FieldOnBoard(lowestPoint, rightestPoint - i);
            trackCounter++;

        }

        // left side (18/2, 17/2...3/2)

        for (int i = 0; i < 16; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(lowestPoint - i, leftestPoint);
            trackCounter++;
        }
    }

   public void createFieldsOnHeaven (int boardSize) {

       switch(boardSize) {

           case 4:
               fieldsOnHeaven = new FieldOnBoard[boardSize][NUM_PIECES];



               for (int i = 0; i < boardSize * 16; i++) {
                   /**
                    * order: 2/2 is startingPos 0
                    *       2/17 is startingPos 16
                    *       etc..
                    */

                   // from 2/2 -> +1/+1
                   int startX = 2;
                   int startY = 2;

                   for (int j = 0; j < NUM_PIECES; j++) {
                       fieldsOnHeaven[i][j] = new FieldOnBoard(startX + 1,startY + 1);
                   }

                   // from 2/17 -> +1/-1
                   startX = 2;
                   startY = 17;

                   for (int j = 0; j < NUM_PIECES; j++) {
                       fieldsOnHeaven[i][j] = new FieldOnBoard(startX + 1,startY - 1);
                   }

                   // from 17/17 -> -1/-1
                   startX = 17;
                   startY = 17;

                   for (int j = 0; j < NUM_PIECES; j++) {
                       fieldsOnHeaven[i][j] = new FieldOnBoard(startX - 1,startY - 1);
                   }

                   // from 17/2 -> -1/+1
                   startX = 17;
                   startY = 2;

                   for (int j = 0; j < NUM_PIECES; j++) {
                       fieldsOnHeaven[i][j] = new FieldOnBoard(startX - 1,startY + 1);
                   }
               }
               break;

           case 6:
               System.err.println("board not implemented for 6 at the moment");
               //fieldsOnHeaven = new FieldOnBoard[boardSize][NUM_PIECES];
               // same as before: but the board is longer, so there are two heaven tiles on the longer sides which go straight and not crosswise
               break;
       }
    }

    public FieldOnBoard getTrack(int number) {
       if (number > fieldsOnTrack.length) {
           return null;
       }
       return fieldsOnTrack[number];
    }
}
