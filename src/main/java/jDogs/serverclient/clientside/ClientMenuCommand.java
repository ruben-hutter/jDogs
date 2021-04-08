package jDogs.serverclient.clientside;


import jDogs.gui.GUIManager;
import jDogs.serverclient.helpers.Queuejd;
import java.sql.SQLOutput;
import javafx.application.Platform;

/**
 * ClientMenuCommand contains the menu/lobby
 * commands which are sent from the server to
 * communicate with the client.
 *
 */

public class ClientMenuCommand {

    private Client client;
    private SendFromClient sendFromClient;
    private Queuejd sendQueue;
    private Queuejd keyBoardInQueue;

    ClientMenuCommand(Client client,SendFromClient sendFromClient, Queuejd sendQueue, Queuejd keyBoardInQueue) {

        this.client = client;
        this.sendQueue = sendQueue;
        this.sendFromClient = sendFromClient;
        this.sendQueue = sendQueue;
        this.keyBoardInQueue = keyBoardInQueue;

    }

    public void execute (String text) {
        System.out.println("text " + text);
        String command = text.substring(0,4);

        switch (command) {
            case "USER":
                String name;

                //case 1: Server wants a nickname, it sends "USER" only
                if (text.length() == command.length()) {
                    name = client.getNickname();
                    sendQueue.enqueue("USER " + name);
                } else {
                    //case 2: Server confirms nickname, it sends "USER " + new nick
                    name = text.substring(5);
                    client.setNickname(name);
                    sendFromClient.keyBoardInBlocked = false;
                    /*
                    Platform.runLater(() -> GUIManager.getInstance().lobbyController.
                            displayInfomsg("info from server. Your new nick is " + name));

                     */
                }
                break;

            case "PCHT":
                System.out.println("PCHT: " + text.substring(4));

                Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayPCHTmsg(text.substring(5)));
                break;

            case "WCHT":
                System.out.println("WCHT: " + text.substring(5));

                Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayWCHTmsg(text.substring(5)));

                break;

            case "LPUB":

                //TODO Information update of active users in Public Lobby
                //just compare to existing String/Array and replace if necessary

                /*
                Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayLPUB(text.substring(5)));

                 */
                System.out.println("LPUB: " + text.substring(5));
                break;


            case "JOIN":

                //TODO display Information about a user or users who joined a pendent game

                /*Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayLSEP(text.substring(5)));

                 */
                System.out.println("JOIN: " + text.substring(5));
                break;

            case "OGAM":

                //TODO display a new pendent
                System.out.println("OGAM: " + text.substring(5));
                /*
                Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displaynewGame(text.substring(5)));

                 */
                break;

            case "DOGA":
                //TODO remove openGame from GUI-Lobby-Display
                System.out.println("DOGA: " + text.substring(5));
                break;

            case "DPER":
                //TODO remove person from open game in lobby
                System.out.println("DPER: " + text.substring(5));
                break;

            case "INFO":
                /*Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayPCHTmsg("SRVRINFO: " + text.substring(5)));

                 */
                System.out.println("SRVRINFO: " + text.substring(5));
                break;

            case "GAME":
                //TODO receive game details when game starts and display in Game GUI
                //details: who makes the first move, who 'sits' where, how many cards do you get in the first round(or do you always get 6?)
                break;


            default:
                System.out.println("received from server " + text + ". This command " + command
                        + " is not implemented");

        }

    }

}
