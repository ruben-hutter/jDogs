package jDogs.gui;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jDogs.board.Board;
import org.checkerframework.checker.units.qual.A;

/**
 * this class adapts the fields how they are kept to the gui and
 * translates their value to a position on the board
 */
public class AdaptToGui {
    private int boardSize;
    private FieldOnBoard[] fieldsOnTrack;
    private FieldOnBoard[][] fieldsOnHeaven;
    private FieldOnBoard[][] fieldsOnHome;
    private final int NUM_PIECES = 4;
    private final int BOARD_SIZE = 4;
    private static AdaptToGui instance;

    public AdaptToGui() {

        this.boardSize = BOARD_SIZE;
       createFieldsOnTrack(boardSize);
       createFieldsOnHeaven(boardSize);
       createFieldsOnHome(boardSize);
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
     * creates a translation table to translate them from gamestate to gui
     * @param boardSize is the size of the board(4 or 6)
     */
    private void createFieldsOnTrack(int boardSize) {
        // board size is 4 or 6
        fieldsOnTrack = new FieldOnBoard[this.boardSize * 16];

        // first position is a new starting position

        int trackCounter = 0;
        int leftestPoint = 4;
        int rightestPoint = 20;
        int highestPoint = 1;
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
     * creates all the fields on heaven
     * @param boardSize size of the board(4/6)
     */
    //TODO update with new values
    public void createFieldsOnHeaven (int boardSize) {

       switch(boardSize) {

           case 4:
               fieldsOnHeaven = new FieldOnBoard[boardSize][NUM_PIECES];
               int count = 0;
               while (count < boardSize) {
                   /**
                    * order: 2/2 is startingPos 0
                    *       2/17 is startingPos 16
                    *       etc..
                    */

                   // from 4/1 -> +1/+1
                   int startX = 4;
                   int startY = 1;
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

                   // from 17/2 -> -1/+1
                   startX = 20;
                   startY = 1;

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

    public void createFieldsOnHome(int boardSize) {
        switch (boardSize) {
            case 4:

                //starting positions are 2/2 2/17 17/17 17/2
                // home positions are next to starting positions so
                // 2/2: 0/0 0/1 1/0 1/1
                // 2/17: 0/17 0/18 1/17 1/18
                // 17/17: 19/17 19/18 18/17 18/18
                // 17/2: 19/0 19/1 18/0 18/1

                fieldsOnHome = new FieldOnBoard[boardSize][NUM_PIECES];
                int count = 0;
                while ( count < boardSize) {

                    int x = 1;
                    int y = 1;
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
                    y = 1;
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
    //TODO delete psvm
    public static void main(String[] args) {
        System.out.println(7%4);
        System.out.println(3/4);

    }


    public FieldOnBoard[] getHomeFieldArray() {

        FieldOnBoard[] homeFieldArr = new FieldOnBoard[boardSize * Board.NUM_HOME_TILES];
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
     * @param destiny fieldOnBoard, x-Pos & y-Pos
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
            if (field.equals(destiny)) {
               return "B0" + pos;
            }
            pos++;
        }
        return "-1";
    }

}
