package jDogs.serverclient.serverside;

import jDogs.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the class {@link RulesCheck}
 * The test-game is played in TeamMode
 */
public class RulesCheckTeamTest {

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
     * set up a test game, where teamMode is active
     */
    @BeforeEach
    void setUp() {
        player1 = new Player("1");
        player2 = new Player("2");
        player3 = new Player("3");
        player4 = new Player("4");
        String nameID = "test";
        Player[] playerArray = {player1, player2, player3, player4};
        teamMode = true;

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
     * Player1 (YELO) and player3 (BLUE) are in a team
     * player1 moves with a  yellow and a blue piece with card SEVE
     */
    @Test
    @DisplayName("move seven with own piece and piece from teampartner")
    void checkMoveSevenTeam(){
        String completeMove = "SEVE 2 YELO-1 B13 BLUE-1 B34";
        player1.changePositionServer(1,"B",10);
        player3.changePositionServer(1, "B", 30);
        player1.setAllowedToPlay(true);

       player1.getPiece(1).changeHasMoved();
       player3.getPiece(1).changeHasMoved();

       int result = 0;
       assertEquals(result, rulesCheck.checkMoveSeven(completeMove,"1").getReturnValue());
    }

    /**
     * player1 wants to move a piece from his teampartner player3
     */
    @Test
    @DisplayName("Move with teampartner's pieces")
    void moveWithTeampartnerPiece(){
        String completeMove = "TWOO BLUE-1 B15";
        player1.changePositionServer(1,"B", 00);
        player3.changePositionServer(1, "B", 13);

        player3.getPiece(1).changeHasMoved();

        int result = 3;
        assertEquals(result, rulesCheck.checkMove(completeMove,"1").getReturnValue());

    }


}
