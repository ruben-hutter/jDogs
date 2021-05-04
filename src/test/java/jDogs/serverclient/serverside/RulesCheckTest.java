package jDogs.serverclient.serverside;

import java.util.ArrayList;

import jDogs.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RulesCheckTest {

    @BeforeEach
    void setUp() {
        Player player1 = new Player("1");
        Player player2 = new Player("2");
        Player player3 = new Player("3");
        Player player4 = new Player("4");
        String nameID = "test";
        Player[] playerArray = {player1, player2, player3, player4};

        MainGame mainGame = new MainGame(playerArray, nameID, false);
        //mainGame.startTest();
        RulesCheck rulesCheck = new RulesCheck(mainGame);

    }

    @AfterEach
    void tearDown() {
        //mainGame = null;
    }

    @Test
    void testIfHandContainsCard() {

    }

    @Test
    void testSpecialCardAcee() {

    }

    @Test
    void testSpecialCardJoker() {

    }

    @Test
    void checkMoveNewPositionA() {
    }

    @Test
    void checkCardWithNewPositionABOk() {
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