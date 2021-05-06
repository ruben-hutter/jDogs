package jDogs.serverclient.serverside;

import java.util.ArrayList;

import jDogs.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the class {@link RulesCheck}
 */
class RulesCheckTest {

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private MainGame mainGame;
    private GameState gameState;

    private RulesCheck rulesCheck;

    private RulesCheckHelper rulesCheckHelper;

    private boolean teamMode;

    /**
     * set up a test game
     */
    @BeforeEach
    void setUp() {
        player1 = new Player("1");
        player2 = new Player("2");
        player3 = new Player("3");
        player4 = new Player("4");
        String nameID = "test";
        Player[] playerArray = {player1, player2, player3, player4};
        teamMode = false;

        mainGame = new MainGame(playerArray, nameID, teamMode);
        mainGame.startTest();
        rulesCheck = new RulesCheck(mainGame);
        gameState = new GameState(mainGame);
        rulesCheckHelper = new RulesCheckHelper(mainGame);

    }

    @AfterEach
    void tearDown() {
        mainGame = null;
    }

    @Test
    @DisplayName("Checks if the hand contains a given card")
    void testIfHandContainsCardTrue() {
        String text = "MOVE KING YELO-1 B00";
        String result = "KING YELO-1 B00";
        assertEquals(result, rulesCheck.checkCard(text,"1"));
    }

    @Test
    @DisplayName("Should return null, because card isn't in hand")
    void testIfHandContainsCardFalse() {
        String text = "MOVE TWOO YELO-1 B00";
        String result = null;
        assertEquals(result, rulesCheck.checkCard(text,"1"));
    }

    @Test
    @DisplayName("Should return null, because command MOVE isn't correct")
    void testIfHandContainsCardNoCard() {
        String text = "MOVE BLAB YELO-1 B00";
        String result = null;
        assertEquals(result, rulesCheck.checkCard(text,"1"));
    }

    @Test
    @DisplayName("Should return the desired card without JOKE")
    void testSpecialCardJoker() {
        String text = "MOVE JOKE KING YELO-1 B00";
        String result = "KING YELO-1 B00";
        assertEquals(result, rulesCheck.checkCard(text,"1"));

    }

    @Test
    @DisplayName("Move from home tile to track tile")
    void checkMoveNewPositionA() {
        String completeMove = "TWOO YELO-1 A00";
        int result = 1;
        assertEquals(result, rulesCheck.checkMove(completeMove,"1").getReturnValue());
    }

    @Test
    @DisplayName("send a too short command to checkMove()")
    void checkMoveTooShort(){
        String completeMove = "KING A00";
        int result = 8;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1").getReturnValue());
    }

    @Test
    @DisplayName("send a too long command to checkMove()")
    void checkMoveTooLong(){
        String completeMove = "KING A00 JOKE B00 B01";
        int result = 8;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1").getReturnValue());
    }

    @Test
    @DisplayName("send garbage to checkMove() with appropriate length")
    void checkMoveGarbage(){
        String completeMove = "QWEE MNVS-1 A00";
        //2 = command throws exception
        int result = 2;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1").getReturnValue());
    }

    @Test
    @DisplayName("Go to startingPosition with valid starting card KING")
    void checkCardWithNewPositionABKING() {
        String card = "KING";
        String actualPosition1 = "A";
        int actualPosition2 = 00;
        String newPosition1 = "B";
        int newPosition2 =  00;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = true;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go to startingPosition with invalid starting card TWOO")
    void checkCardWithNewPositionABNotKING() {
        String card = "TWOO";
        String actualPosition1 = "A";
        int actualPosition2 = 00;
        String newPosition1 = "B";
        int newPosition2 =  00;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = false;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go two steps further on the track with card TWOO")
    void checkCardWithNewPositionBB() {
        String card = "TWOO";
        String actualPosition1 = "B";
        int actualPosition2 = 00;
        String newPosition1 = "B";
        int newPosition2 =  02;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = true;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go three steps further on the track with card TWOO")
    void checkCardWithNewPositionBBInvalid() {
        String card = "TWOO";
        String actualPosition1 = "B";
        int actualPosition2 = 00;
        String newPosition1 = "B";
        int newPosition2 =  03;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = false;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go three steps backwards on the track with card THRE")
    void checkCardWithNewPositionBBBackwards() {
        String card = "THRE";
        String actualPosition1 = "B";
        int actualPosition2 = 03;
        String newPosition1 = "B";
        int newPosition2 =  00;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = false;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go four steps backwards on the track with card FOUR")
    void checkCardWithNewPositionBBFour() {
        String card = "FOUR";
        String actualPosition1 = "B";
        int actualPosition2 = 04;
        String newPosition1 = "B";
        int newPosition2 =  00;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = true;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go four steps forwards on the track with card FOUR")
    void checkCardWithNewPositionBBFourPos() {
        String card = "FOUR";
        String actualPosition1 = "B";
        int actualPosition2 = 04;
        String newPosition1 = "B";
        int newPosition2 =  8;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = true;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go twoo steps forwards on the track over the end")
    void checkCardWithNewPositionBBEnd() {
        String card = "TWOO";
        String actualPosition1 = "B";
        int actualPosition2 = 62;
        String newPosition1 = "B";
        int newPosition2 =  0;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = true;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go four steps backwards on the track over the end")
    void checkCardWithNewPositionBBFourEnd() {
        String card = "FOUR";
        String actualPosition1 = "B";
        int actualPosition2 = 2;
        String newPosition1 = "B";
        int newPosition2 =  62;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = true;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go into heaven with THRE (heaven starts at C00)")
    void checkCardWithNewPositionBC() {
        String card = "THRE";
        String actualPosition1 = "B";
        int actualPosition2 = 63;
        String newPosition1 = "C";
        int newPosition2 =  01;
        int startingPosition = 00;
        boolean hasMoved = true;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = true;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go into heaven directly from start (hasMoved = false)")
    void checkCardWithNewPositionBCNotMoved() {
        String card = "THRE";
        String actualPosition1 = "B";
        int actualPosition2 = 63;
        String newPosition1 = "C";
        int newPosition2 =  01;
        int startingPosition = 00;
        boolean hasMoved = false;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = false;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    @DisplayName("Go into heaven with FOUR negative")
    void checkCardWithNewPositionBCFour() {
        String card = "FOUR";
        String actualPosition1 = "B";
        int actualPosition2 = 17;
        String newPosition1 = "C";
        int newPosition2 =  02;
        int startingPosition = 16;
        boolean hasMoved = true;
        Player ownplayer = player1;
        int pieceID = 1;

        boolean result = true;
        assertEquals(result, rulesCheck.checkCardWithNewPosition(card, actualPosition1, actualPosition2, newPosition1,
                newPosition2, startingPosition, hasMoved, ownplayer, pieceID, rulesCheckHelper));
    }

    @Test
    void checkCardWithNewPositionABNotOk() {
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