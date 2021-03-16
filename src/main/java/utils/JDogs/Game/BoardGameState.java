package utils.JDogs.Game;
//this class will save the state of the board with the players

import utils.JDogs.Board.*;

public class BoardGameState {

    /*should import the tiles from the board
    (since it could be big or small).
    In order to know where the tiles are the players can use
    this step is important
     */
    Board newBoard;
    Tile[] gameArray;


    BoardGameState(Board newBoard) {
        this.newBoard = newBoard;
        this.gameArray = newBoard.getGameArray();
    }


}
