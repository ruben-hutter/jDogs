package jDogs.serverclient.clientside;

import jDogs.Alliance_4;
import jDogs.ClientGame;
import jDogs.gui.GuiParser;
import jDogs.serverclient.helpers.Queuejd;
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
                } else {
                    System.out.println("it is " + text.substring(5) + "`s turn");
                }
                break;

            case "ROUN":
                //TODO received hand of cards display in Game GUI
                clientGame.setCards(text.substring(5));
                //TODO client game setCards()
                //clientGame.setCards(text.substring(5));
                break;

            case "CARD":
                clientGame.remove(text.substring(5));
                break;

            case "HAND":
                clientGame.printCards();
                break;

            case "GAME":
                //TODO receive game details when game starts and display in Game GUI
                clientGame = new ClientGame(GuiParser.getArray(text.substring(5)));
                clientGame.printGameState();

              /*  Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayWCHTmsg(text.substring(5)));

               */

                //details: who makes the first move, who 'sits' where, how many cards do you get in the first round(or do you always get 6?)
                break;

            case "MOVE": // MOVE YELO-1 B20
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

            case "BORD":
                clientGame.printGameState();
        }
    }
}
