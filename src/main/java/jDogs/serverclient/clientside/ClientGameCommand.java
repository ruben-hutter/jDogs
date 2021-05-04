package jDogs.serverclient.clientside;

import jDogs.Alliance_4;
import jDogs.ClientGame;
import jDogs.gui.GUIManager;
import jDogs.gui.GuiParser;
import jDogs.serverclient.helpers.Queuejd;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ClientGameCommand contains the game
 * commands which are sent from the
 * server to communicate with the
 * client.
 *
 */
public class ClientGameCommand {

    private ClientGame clientGame;
    private Client client;
    private SendFromClient sendFromClient;
    private Queuejd sendQueue;
    private Queuejd keyBoardInQueue;
    private static final Logger logger = LogManager.getLogger(ClientGameCommand.class);


    ClientGameCommand(Client client,SendFromClient sendFromClient, Queuejd sendQueue, Queuejd keyBoardInQueue) {
        this.client = client;
        this.sendQueue = sendQueue;
        this.sendFromClient = sendFromClient;
        this.sendQueue = sendQueue;
        this.keyBoardInQueue = keyBoardInQueue;
    }

    public void execute(String text) {

        String command = text.substring(0,4);

        switch(command) {

            case "TURN":
                if (text.length() == command.length()) {
                    System.out.println("TURN: your turn");
                    Platform.runLater(() -> GUIManager.getInstance().
                            gameWindowController.setYourTurn(true));
                } else {
                    Platform.runLater(() -> GUIManager.getInstance().
                            gameWindowController.displayInfoFromClient(text));
                }
                break;

            case "ROUN":
                //TODO received hand of cards display in Game GUI
                clientGame.setCards(text.substring(5));
                break;

            case "CARD":
                clientGame.remove(text.substring(5));
                break;
            //TODO delete this
            case "HAND":
                clientGame.printCards();
                break;

            case "GAME":
                //TODO receive game details when game starts and display in Game GUI

                clientGame = new ClientGame(GuiParser.getArray(text.substring(5)));
                clientGame.printGameState();

                //move to game-scene in gui manager
                Platform.runLater(() -> GUIManager.getInstance().startGame());
                break;

            case "MOVE": // MOVE YELO-1 B20
                System.out.println("cg command " + text);
                String piece = text.substring(5, 9);
                switch(piece) {
                    case "YELO":
                        clientGame.changePiecePosition(clientGame.getPlayer(Alliance_4.YELLOW),
                                Integer.parseInt(text.substring(10, 11)), text.substring(12));
                        break;
                    case "BLUE":
                        clientGame.changePiecePosition(clientGame.getPlayer(Alliance_4.BLUE),
                                Integer.parseInt(text.substring(10, 11)), text.substring(12));
                        break;
                    case "REDD":
                        clientGame.changePiecePosition(clientGame.getPlayer(Alliance_4.RED),
                                Integer.parseInt(text.substring(10, 11)), text.substring(12));
                        break;
                    case "GREN":
                        clientGame.changePiecePosition(clientGame.getPlayer(Alliance_4.GREEN),
                                Integer.parseInt(text.substring(10, 11)), text.substring(12));
                        break;
                }
                break;

            case "JACK":
                Platform.runLater(() -> GUIManager.getInstance().gameWindowController.makeJackMove(text.substring(5)));
                break;

            //TODO delete this
            case "BORD":
                clientGame.printGameState();
                break;

            case "VICT":
                // TODO send message of a victory with the winner's name/s
                // TODO if they click ok, terminate game and return lobby
                Platform.runLater(() -> GUIManager.getInstance().gameWindowController.declareVictory(text.substring(5)));                break;
        }
    }
}
