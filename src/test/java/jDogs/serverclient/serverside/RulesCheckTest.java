package jDogs.serverclient.serverside;

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

    private Player player1; //YELLOW
    private Player player2; //GREEN
    private Player player3; //BLUE
    private Player player4; //RED

    private MainGame mainGame;
    private GameState gameState;

    private RulesCheck rulesCheck;

    private RulesCheckHelper rulesCheckHelper;

    private boolean teamMode;

    /**
     * set up a test game with 4 players
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
        gameState = null;
    }

    /**
     * the player has KING on his hand
     * the tests checks the result if the player plays KING
     */
    @Test
    @DisplayName("Checks if the hand contains a given card")
    void testIfHandContainsCardTrue() {
        String text = "MOVE KING YELO-1 B00";
        String result = "KING YELO-1 B00";
        assertEquals(result, rulesCheck.checkCard(text,"1"));
    }

    /**
     * the player hasn't the card TWOO on his hand
     * but he plays the card TWOO
     */
    @Test
    @DisplayName("Should return null, because card isn't in hand")
    void testIfHandContainsCardFalse() {
        String text = "MOVE TWOO YELO-1 B00";
        String result = null;
        assertEquals(result, rulesCheck.checkCard(text,"1"));
    }

    /**
     * test sending garbage, e.g. no cardname
     */
    @Test
    @DisplayName("Should return null, because command MOVE isn't correct")
    void testIfHandContainsCardNoCard() {
        String text = "MOVE BLAB YELO-1 B00";
        String result = null;
        assertEquals(result, rulesCheck.checkCard(text,"1"));
    }

    /**
     * the player has the card JOKE on his hand
     * checks the correct handling of card JOKE
     */
    @Test
    @DisplayName("Should return the desired card without JOKE")
    void testSpecialCardJoker() {
        String text = "MOVE JOKE KING YELO-1 B00";
        String result = "KING YELO-1 B00";
        assertEquals(result, rulesCheck.checkCard(text,"1"));

    }

    // TODO add javadoc
    @Test
    @DisplayName("Move from home tile to track tile")
    void checkMoveNewPositionA() {
        String completeMove = "TWOO YELO-1 A00";
        int result = 1;
        assertEquals(result, rulesCheck.checkMove(completeMove,"1").getReturnValue());
    }

    /**
     * send a too short command to checkMove()
     * should return 8
     */
    @Test
    @DisplayName("send a too short command to checkMove()")
    void checkMoveTooShort(){
        String completeMove = "KING A00";
        int result = 8;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1").getReturnValue());
    }

    /**
     * send a too long command to checkMove()
     * should return 8
     */
    @Test
    @DisplayName("send a too long command to checkMove()")
    void checkMoveTooLong(){
        String completeMove = "KING A00 JOKE B00 B01";
        int result = 8;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1").getReturnValue());
    }

    /**
     * send garbage to checkMove()
     * should return 2, 2 = exception
     */
    @Test
    @DisplayName("send garbage to checkMove() with appropriate length")
    void checkMoveGarbage(){
        String completeMove = "QWEE MNVS-1 A00";
        int result = 2;
        assertEquals(result, rulesCheck.checkMove(completeMove, "1").getReturnValue());
    }

    /**
     * Player is on A00
     * Moves with card KING to startingPosition B00
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * player tries to go to startingPosition with invalid starting card
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on position B00 and moves twoo steps to B02
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on B00 and wants to move 3 steps with card TWOO
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on B03 and wants to move 3 steps backwards
     * should return false
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on B04 and wants to go 4 steps backwards with card FOUR to B00
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on B04 and wants to move 4 steps forward with FOUR to B08
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on B62 and wants to move 2 steps forward to B00
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on B02 and wants to move 4 steps backwards to B62
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on B63, has startingPosition B00
     * and wants to go into heaven C01 with card THRE
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on his startingPosition B00 and hasn't moved yet
     * he wants to go into heave with THRE to C02
     * should return false
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player is on B17 and wants to go into heaven C02 with FOUR backwards
     * startingPosition is B16
     */
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
                newPosition2, startingPosition, hasMoved, ownplayer));
    }

    /**
     * Player1  wants to change pieces from player1 and player2
     * The pieces are on track and hasMovec = true
     */
    @Test
    @DisplayName("change two pieces on track (BB)")
    void checkMoveJackBB() {
        String twoPieces = "JACK YELO-1 GREN-1";
        //set players to positions on track
        player1.changePositionServer(1, "B", 00);
        player2.changePositionServer(1, "B", 05);
        player1.setAllowedToPlay(true);
        player2.setAllowedToPlay(false);

        //pieces are initially not moved
        //change hasMoved to moved = true
        player1.getPiece(1).changeHasMoved();
        player2.getPiece(1).changeHasMoved();

        //0 = everything ok
        int result = 0;
        assertEquals(result, rulesCheck.checkMoveJack(twoPieces, "1").getReturnValue());
    }

    /**
     *player1 wants to change a piece which is in heaven
     * should return 3, 3 = one piece is at home or in heaven
     */
    @Test
    @DisplayName("change two pieces from heaven to track (AB)")
    void checkMoveJackAB() {
        String twoPieces = "JACK YELO-1 GREN-1";
        //set players to positions on track
        player1.changePositionServer(1, "B", 05);
        player2.changePositionServer(1, "A", 00);
        player1.setAllowedToPlay(true);
        player2.setAllowedToPlay(false);

        //pieces are initially not moved
        //change hasMoved to moved = true
        player1.getPiece(1).changeHasMoved();

        int result = 3;
        assertEquals(result, rulesCheck.checkMoveJack(twoPieces, "1").getReturnValue());
    }

    /**
     * player1 is YELLOW, player2 is GREN
     * player1 sends command in wrong order, green piece first
     * ownPlayer is the player which alliance is first in the command
     * should return 2, 2 = ownPlayer != nowPlaying
     */
    @Test
    @DisplayName("pieces in command in wrong order")
    void checkMoveJackWrongOrder() {
        String twoPieces = "JACK GREN-1 YELO-1";
        //set players to positions on track
        player1.changePositionServer(1, "B", 05);
        player2.changePositionServer(1, "B", 00);
        player1.setAllowedToPlay(true);
        player2.setAllowedToPlay(false);

        player1.getPiece(1).changeHasMoved();
        player2.getPiece(1).changeHasMoved();
        int result = 2;
        assertEquals(result, rulesCheck.checkMoveJack(twoPieces, "1").getReturnValue());
    }

    /**
     * send wrong JACK-command
     * should result in an exception (5)
     */
    @Test
    @DisplayName("incorrect input throws exception")
    void checkMoveSevenIncorrectInput() {
        String completeMove = "SEVE BLUE-1 B05 GREN-3";
        int result = 5;
        assertEquals(result, rulesCheck.checkMoveSeven(completeMove,"1").getReturnValue());
    }

    /**
     * player1 is YELLOW and wants to move two of his own pieces with card SEVE
     */
    @Test
    @DisplayName("player YELO moves two own pieces with SEVE and a total of 7 steps")
    void checkMoveSevenCorrectInput() {
        String completeMove = "SEVE 2 YELO-1 B05 YELO-2 B18";
        player1.changePositionServer(1,"B",00);
        player1.changePositionServer(2, "B", 16);
        player1.setAllowedToPlay(true);

        int result = 0;
        assertEquals(result, rulesCheck.checkMoveSeven(completeMove,"1").getReturnValue());
    }

    /**
     * There is no TeamMode, so player1 cannot move pieces from player3
     */
    @Test
    @DisplayName("Move with  pieces from other players without TeamMode")
    void checkMoveSevenNoTeamMode(){
        String completeMove = "SEVE 2 YELO-1 B13 BLUE-1 B34";
        player1.changePositionServer(1,"B",10);
        player3.changePositionServer(1, "B", 30);
        player1.setAllowedToPlay(true);

        player1.getPiece(1).changeHasMoved();
        player3.getPiece(1).changeHasMoved();

        int result = 1;
        assertEquals(result, rulesCheck.checkMoveSeven(completeMove,"1").getReturnValue());
    }

    /**
     * player1 wants to move more than seven steps with card SEVE and two pieces
     */
    @Test
    @DisplayName("try to move more than seven steps")
    void checkMoveSevenMoreThanSeven() {
        String completeMove = "SEVE 2 YELO-1 B05 YELO-2 B19";
        player1.changePositionServer(1,"B",00);
        player1.changePositionServer(2, "B", 16);
        player1.setAllowedToPlay(true);

        //2 = more than seven steps
        int result = 2;
        assertEquals(result, rulesCheck.checkMoveSeven(completeMove,"1").getReturnValue());
    }

    @Test
    @DisplayName("eliminate piece by overtaking with SEVE")
    void checkMoveSevenEliminate(){
        String completeMove = "SEVE 1 YELO-1 B07";
        player1.changePositionServer(1,"B",00);
        player2.changePositionServer(2, "B", 03);
        player1.setAllowedToPlay(true);
        player2.getPiece(1).changeHasMoved();

        int result = 3;
        assertEquals(result, rulesCheck.checkMoveSeven(completeMove,"1").getReturnValue());

    }

    /**
     *player1 wants to move over a blocked field
     */
    @Test
    @DisplayName("try to move over blocked field")
    void checkBlock() {
        String completeMove = "THRE YELO-1 B17";
        String nickname = "1";

        player1.changePositionServer(1,"B", 14);
        player2.changePositionServer(1,"B", 16);
        gameState.updatePiecesOnTrack(player2.getPiece(1), "B");
        gameState.updatePiecesOnTrack(player1.getPiece(1), "B");

        int result = 0;

        assertEquals(result, rulesCheck.checkMove(completeMove, nickname).getReturnValue());
    }

    /**
     *player1 wants to move on a blocked field
     */
    @Test
    @DisplayName("try to move on a blocked field")
    void checkBlockDestination() {
        String completeMove = "TWOO YELO-1 B16";
        String nickname = "1";

        player1.changePositionServer(1,"B", 14);
        player2.changePositionServer(1,"B", 16);
        gameState.updatePiecesOnTrack(player2.getPiece(1), "B");
        gameState.updatePiecesOnTrack(player1.getPiece(1), "B");

        int result = 0;

        assertEquals(result, rulesCheck.checkMove(completeMove, nickname).getReturnValue());
    }


    /**
     *player1 wants to move on an occupied (not blocked) field
     */
    @Test
    @DisplayName("try to move on an occupied (not blocked) field")
    void checkDestinationOccupied(){
        String completeMove = "THRE YELO-1 B17";
        String nickname = "1";

        player1.changePositionServer(1,"B", 14);
        player2.changePositionServer(1,"B", 17);
        player1.getPiece(1).changeHasMoved();
        player2.getPiece(1).changeHasMoved();

        int result = 0;
        assertEquals(result, rulesCheck.checkMove(completeMove, nickname).getReturnValue());
    }


}