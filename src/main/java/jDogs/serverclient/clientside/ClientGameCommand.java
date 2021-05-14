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

    /**
     * constructor of a ClientGameCommand
     * @param client client instance
     * @param sendFromClient sendFromClient thread: sends messages to server
     * @param sendQueue QueueJD to send messages to client
     * @param keyBoardInQueue QueueJD to send messages to client from keyboard or user
     */
    ClientGameCommand(Client client,SendFromClient sendFromClient, Queuejd sendQueue, Queuejd keyBoardInQueue) {
        this.client = client;
        this.sendQueue = sendQueue;
        this.sendFromClient = sendFromClient;
        this.sendQueue = sendQueue;
        this.keyBoardInQueue = keyBoardInQueue;
    }

    /**
     * executes commands that fitted the formal criteria of ReceivedFromServer
     * @param text command and information
     */
    public void execute(String text) {
        String command = text.substring(0,4);
        switch(command) {
            case "TURN":
                if (text.length() == command.length()) {
                    Platform.runLater(() -> GUIManager.getInstance().
                            gameWindowController.setYourTurn(true));
                } else {
                    if (text.substring(5).equals(Client.getInstance().getNickname())) {
                        Platform.runLater(() -> GUIManager.getInstance().
                                gameWindowController.setYourTurn(true));
                    } else {
                        Platform.runLater(() -> GUIManager.getInstance().
                                gameWindowController.displayInfoFromServer(text));
                    }
                }
                break;

            case "ROUN":
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
                System.out.println(text);
                clientGame = new ClientGame(GuiParser.getArray(text.substring(7)));
                clientGame.printGameState();

                //move to game-scene in gui manager and give 0 or 1 for teamMode or no
                Platform.runLater(() -> GUIManager.getInstance().startGame(text.charAt(5) - 48));
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

            // TODO delete this
            case "BORD":
                clientGame.printGameState();
                break;

            case "VICT":
                Platform.runLater(() -> GUIManager.getInstance().gameWindowController.declareVictory(text.substring(5)));
                break;

            case "STOP":
                Platform.runLater(() -> GUIManager.getInstance().gameWindowController.returnToLobby());
                break;

            case "FAIL":
                // TODO change with something usefull in GUI
                // TODO change with protocol command
                break;

            default:
                System.err.println("Received unknow message from server: " + text);
        }
    }
}
