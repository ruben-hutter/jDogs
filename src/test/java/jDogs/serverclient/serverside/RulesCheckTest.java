package jDogs.serverclient.serverside;

import java.util.ArrayList;

import jDogs.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RulesCheckTest {

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private MainGame mainGame;
    private GameState gameState;

    private RulesCheck rulesCheck;

    @BeforeEach
    void setUp() {
        player1 = new Player("1");
        player2 = new Player("2");
        player3 = new Player("3");
        player4 = new Player("4");
        String nameID = "test";
        Player[] playerArray = {player1, player2, player3, player4};

        mainGame = new MainGame(playerArray, nameID, false);
        mainGame.startTest();
        rulesCheck = new RulesCheck(mainGame);
        gameState = new GameState(mainGame);

    }

    @AfterEach
    void tearDown() {
        mainGame = null;
    }

    @Test
    void testIfHandContainsCard() {
        String text = "MOVE KING YELO-1 B00";
        String result = "KING YELO-1 B00";
        assertEquals(result, rulesCheck.checkCard(text,"1"));



    }

    @Test
    void testSpecialCardJoker() {
        String text = "MOVE JOKE KING YELO-1 B00";
        String result = "KING YELO-1 B00";
        assertEquals(result, rulesCheck.checkCard(text,"1"));

    }

    @Test
    void checkMoveNewPositionA() {
        String completeMove = "TWOO YELO-1 A00";
        int result = 1;
        assertEquals(result, rulesCheck.checkMove(completeMove,"1"));
    }

    @Test
    void checkMoveTooShort(){
        String completeMove = "KING A00";
        int result = 8;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1"));
    }

    @Test
    void checkMoveTooLong(){
        String completeMove = "KING A00 JOKE B00 B01";
        int result = 8;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1"));
    }

    @Test
    void checkCardWithNewPositionABOk() {
        /*player1.changePositionServer(1,"A", 00);
        String completeMove = "KING YELO-1 B00";
        int result = 0;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1"));

         */



    }

    @Test
    void checkCardWithNewPositionABNotOk() {
    }

    @Test
    void checkCardWithNewPositionBB() {
    }

    @Test
    void checkCardWithNewPositionBC() {
    }

    @Test
    void checkCardWithNewPositionN4() {
    }

    @Test
    void checkMoveJack() {
    }

    @Test
    void checkMoveSeven() {
    }

    @Test
    void testPiecesOnPathBB(){

    }

    @Test
    void testPiecesOnPathBC(){

    }

    @Test
    void testPiecesOnPathCC(){

    }
    @Test
    void testPiecesOnPathOwnPiece(){

    }

    @Test
    void testPiecesOnPathPieceToEliminate(){

    }

    @Test
    void testPiecesOnPathSeveralPiecesToEliminate(){

    }

    @Test
    void testPiecesOnPathBlocked(){

    }



}