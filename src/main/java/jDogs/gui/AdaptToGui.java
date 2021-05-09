package jDogs.gui;

import jDogs.board.Board;

/**
 * this class adapts the fields how they are kept to the gui and
 * translates their value to a position on the board
 */
public class AdaptToGui {
    private FieldOnBoard[] fieldsOnTrack;
    private FieldOnBoard[][] fieldsOnHeaven;
    private FieldOnBoard[][] fieldsOnHome;
    private final int NUM_PIECES = 4;
    private final int BOARD_SIZE = 4;
    private static AdaptToGui instance;

    /**
     * constructor of AdaptToGui
     */
    public AdaptToGui() {

       createFieldsOnTrack();
       createFieldsOnHeaven();
       createFieldsOnHome();
       instance = this;
    }

    /**
     * Since only one gui exists, we only need one GUI-Adapter
     * @return the signleton of this class
     */
    public static AdaptToGui getInstance() {
        return instance;
    }

    /**
     * creates a translation table to translate from game state tracks to gui
     */
    private void createFieldsOnTrack() {
        // board size is 4 or 6
        fieldsOnTrack = new FieldOnBoard[BOARD_SIZE * 16];

        // first position is a new starting position

        int trackCounter = 0;
        int leftestPoint = 4;
        int rightestPoint = 20;
        int highestPoint = 2;
        int lowestPoint = 18;

        //left side from yellow to green (4/2,4/3,4/5....4/17)


        for (int i = 0; i < 16; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(leftestPoint, highestPoint + i);
            trackCounter++;

        }

        //down side(4/18, 5/18.....20/18)

        for (int i = 0; i < 16; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(leftestPoint + i, lowestPoint);
            trackCounter++;
        }

        //right side (20/18, 20/17,...20/1)

        for (int i = 0; i < 16; i++) {

            fieldsOnTrack[trackCounter] = new FieldOnBoard(rightestPoint, lowestPoint - i);
            trackCounter++;

        }

        // upper side (18/2, 17/2...3/2)

        for (int i = 0; i < 16; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(rightestPoint - i, highestPoint);
            trackCounter++;
        }
    }

    /**
     * creates a translation table to translate from game state heaven to gui
     */
    public void createFieldsOnHeaven () {
       switch(BOARD_SIZE) {
           case 4:
               fieldsOnHeaven = new FieldOnBoard[BOARD_SIZE][NUM_PIECES];
               int count = 0;
               while (count < BOARD_SIZE) {
                   /**
                    * order: 4/2 is startingPos 0
                    *       4/18 is startingPos 16
                    *       etc..
                    */

                   // from 4/2 -> +1/+1
                   int startX = 4;
                   int startY = 2;
                   for (int j = 0; j < NUM_PIECES; j++) {
                       fieldsOnHeaven[count][j] = new FieldOnBoard(++startX,++startY);
                   }
                   count++;

                   // from 4/18 -> +1/-1
                   startX = 4;
                   startY = 18;

                   for (int j = 0; j < NUM_PIECES; j++) {
                       fieldsOnHeaven[count][j] = new FieldOnBoard(++startX,--startY);
                   }
                   count++;
                   // from 20/18 -> -1/-1
                   startX = 20;
                   startY = 18;

                   for (int j = 0; j < NUM_PIECES; j++) {
                       fieldsOnHeaven[count][j] = new FieldOnBoard(--startX,--startY);
                   }
                   count++;

                   // from 20/2 -> -1/+1
                   startX = 20;
                   startY = 2;

                   for (int j = 0; j < NUM_PIECES; j++) {
                       fieldsOnHeaven[count][j] = new FieldOnBoard(--startX,++startY);
                   }
                   count++;
               }
               break;

           case 6:
               System.err.println("board not implemented for 6 at the moment");
               //fieldsOnHeaven = new FieldOnBoard[boardSize][NUM_PIECES];
               // same as before: but the board is longer, so there are two heaven tiles on the longer sides which go straight and not crosswise
               break;
       }
    }

    /**
     * creates a translation table to translate from game state home to gui
     */
    public void createFieldsOnHome() {
        switch (BOARD_SIZE) {
            case 4:

                fieldsOnHome = new FieldOnBoard[BOARD_SIZE][NUM_PIECES];
                int count = 0;
                while ( count < BOARD_SIZE) {

                    int x = 1;
                    int y = 2;
                    int j = 0;

                    // YELLOW
                    fieldsOnHome[count][j] = new FieldOnBoard(x, y);
                    fieldsOnHome[count][j + 1] = new FieldOnBoard(x, y + 1);
                    fieldsOnHome[count][j + 2] = new FieldOnBoard(x + 1, y);
                    fieldsOnHome[count][j + 3] = new FieldOnBoard(x + 1, y + 1);

                    count++;
                    x = 1;
                    y = 17;
                    j = 0;
                    // GREEN
                    fieldsOnHome[count][j] = new FieldOnBoard(x, y);
                    fieldsOnHome[count][j + 1] = new FieldOnBoard(x, y + 1);
                    fieldsOnHome[count][j + 2] = new FieldOnBoard(x + 1, y);
                    fieldsOnHome[count][j + 3] = new FieldOnBoard(x + 1, y + 1);

                    count++;
                    x = 23;
                    y = 17;
                    j = 0;
                    // BLUE
                    fieldsOnHome[count][j] = new FieldOnBoard(x, y);
                    fieldsOnHome[count][j + 1] = new FieldOnBoard(x, y + 1);
                    fieldsOnHome[count][j + 2] = new FieldOnBoard(x - 1, y);
                    fieldsOnHome[count][j + 3] = new FieldOnBoard(x - 1, y + 1);

                    count++;
                    x = 23;
                    y = 2;
                    j = 0;
                    // RED
                    fieldsOnHome[count][j] = new FieldOnBoard(x, y);
                    fieldsOnHome[count][j + 1] = new FieldOnBoard(x, y + 1);
                    fieldsOnHome[count][j + 2] = new FieldOnBoard(x - 1, y);
                    fieldsOnHome[count][j + 3] = new FieldOnBoard(x - 1, y + 1);

                    count++;
                }
                break;
        }
    }

    /**
     * translates number on server to heaven field in gui
     * @param playerNumber 0-3(for 4 players)
     * @param pos 0-3 (4 heaven fields)
     * @return position in gui
     */
    public FieldOnBoard getHeavenField(int playerNumber, int pos) {
        FieldOnBoard fieldOnBoard = fieldsOnHeaven[playerNumber][pos];
        return fieldOnBoard;
    }

    /**
     * returns the track on the gui
     * @param number the number of the field on the game state
     * @return the place in the gui(saved as FieldOnBoard)
     */
    public FieldOnBoard getTrack(int number) {
       if (number > fieldsOnTrack.length) {
           return null;
       }
       return fieldsOnTrack[number];
    }

    /**
     * translates a home tile in the game to a field in the gui
     * @param startPos the starting position of this alliance
     * @param pieceID the id of this token in the array of home tiles
     * @return a 2D Field which represents this field in the gui
     */
    public FieldOnBoard getHomeField(int startPos, int pieceID) {
        FieldOnBoard home = null;
        switch (startPos) {
            case 0:
                home = fieldsOnHome[0][pieceID];
                break;
            case 16:
                home = fieldsOnHome[1][pieceID];
                break;
            case 32:
                home = fieldsOnHome[2][pieceID];
                break;
            case 48:
                home = fieldsOnHome[3][pieceID];
                break;
        }
        return home;
    }

    /**
     * get the array with all home fields in an array from 0 to 15
     * Yelo: 0-3,Gren: 4-7,Blue: 8-11,Redd: 12-15
     * @return positions 0 - 15
     */
    public FieldOnBoard[] getHomeFieldArray() {
        FieldOnBoard[] homeFieldArr = new FieldOnBoard[BOARD_SIZE * Board.NUM_HOME_TILES];
        int count = 0;
        for (FieldOnBoard[] fieldOnBoardArray : fieldsOnHome) {
            for (int i = 0; i < Board.NUM_HOME_TILES; i++) {
                homeFieldArr[count] = fieldOnBoardArray[i];
                count++;
            }
        }
        return homeFieldArr;
    }

    /**
     * transform track or heaven field back to position number on server
     * @param destiny fieldOnBoard, x-Pos and y-Pos
     * @param playerNr int between 0-3
     * @return position number on server(heaven tracks numbers are from 64 upwards)
     */
    public String getPosNumber(FieldOnBoard destiny, int playerNr) {
        int pos = 0;
        for (FieldOnBoard field : fieldsOnTrack) {
            if (field.getX() == destiny.getX() && field.getY() == destiny.getY()) {
                if (pos < 10) {
                    return "B0" + pos;
                }
                return "B" + pos;
            }
            pos++;
        }
        pos = 0;
        FieldOnBoard[] heavenArr = fieldsOnHeaven[playerNr];
        for (FieldOnBoard field : heavenArr) {
            if (field.getX() == destiny.getX() && field.getY() == destiny.getY()) {
               return "C0" + pos;
            }
            pos++;
        }
        return "-1";
    }
}
