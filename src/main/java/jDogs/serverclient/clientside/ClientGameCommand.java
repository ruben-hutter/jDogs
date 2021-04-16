package jDogs.serverclient.clientside;

import jDogs.ClientGame;
import jDogs.gui.GuiParser;
import jDogs.serverclient.helpers.Queuejd;
import jDogs.serverclient.serverside.SeparateLobbyCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ClientGameCommand contains the game
 * commands which are sent from the
 * server to communicate with the
 * client.
 *
 */
//

public class ClientGameCommand {
    private Client client;
    private SendFromClient sendFromClient;
    private Queuejd sendQueue;
    private Queuejd keyBoardInQueue;
    private ClientGame clientGame;
    private static final Logger logger = LogManager.getLogger(ClientGameCommand.class);


    ClientGameCommand(Client client,SendFromClient sendFromClient, Queuejd sendQueue, Queuejd keyBoardInQueue) {
        this.client = client;
        this.sendQueue = sendQueue;
        this.sendFromClient = sendFromClient;
        this.sendQueue = sendQueue;
        this.keyBoardInQueue = keyBoardInQueue;


    }

    public void execute(String text) {
        logger.debug("Entered ClientGameCommand with: " + text);

        String command = text.substring(0,4);

        switch(command) {

            case "TURN":
                if (text.length() == command.length()) {
                    System.out.println("TURN: your turn");
                } else {
                    System.out.println("it is " + text.substring(5) + "`s turn");
                }
                System.out.println("Which card do you want to play?");
                break;
            case "GAME":
                //TODO receive game details when game starts and display in Game GUI
                clientGame = new ClientGame(GuiParser.getArray(text.substring(5)));
                clientGame.printGameState();
                //details: who makes the first move, who 'sits' where, how many cards do you get in the first round(or do you always get 6?)
                break;

            case "ROUN":
                //TODO received hand of cards display in Game GUI
                logger.debug("In command ROUND");

                System.out.println("ROUN: " + text.substring(5));
                //System.out.println("Which card do you want to play?");

            default:
                System.out.println("the command " + command + " is not implemented. Can`t execute " + text);

        }

    }

}
