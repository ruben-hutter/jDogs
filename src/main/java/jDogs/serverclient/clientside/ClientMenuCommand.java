package jDogs.serverclient.clientside;


import jDogs.gui.GUIManager;
import jDogs.serverclient.helpers.Queuejd;
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
                /*
                Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayLPUB(text.substring(5)));

                 */
                System.out.println("LPUB: " + text.substring(5));
                break;


            case "LSEP":

                /*Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayLSEP(text.substring(5)));

                 */
                System.out.println("LSEP: " + text.substring(5));
                break;

            case "OGAM":
                System.out.println("OGAM: " + text.substring(5));
                /*
                Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displaynewGame(text.substring(5)));

                 */



            case "INFO":
                /*Platform.runLater(()->
                        GUIManager.getInstance().lobbyController.displayPCHTmsg("SRVRINFO: " + text.substring(5)));

                 */
                System.out.println("SRVRINFO: " + text.substring(5));
                break;


            default:
                System.out.println("received from server " + text + ". This command " + command
                        + " is not implemented");
        }

    }

}
