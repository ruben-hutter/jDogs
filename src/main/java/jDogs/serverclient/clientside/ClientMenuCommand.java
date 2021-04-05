package jDogs.serverclient.clientside;


import jDogs.serverclient.helpers.Queuejd;

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
                System.out.println("server wants a nickname from you. Your default nickname "
                        + client.getHostName()
                        + " will be sent; accept by 'Y' or enter other name now");
                while (keyBoardInQueue.isEmpty()) {
                    // do nothing
                    // for later: wake up, if keyboardInput is not empty anymore
                    // or handle with commands
                }
                name = keyBoardInQueue.dequeue();
                if (name.equalsIgnoreCase("y")) {
                    sendQueue.enqueue("USER " + client.getHostName());
                } else {
                    sendQueue.enqueue("USER " + name);
                }
                // allow sending messages from keyboard to server
                sendFromClient.keyBoardInBlocked = false;
                break;

            case "PCHT":
                //chatGui.displayMessage(text.substring(4));
                client.displayPCHTmsg(text.substring(4));
                System.out.println("public chat message: " + text.substring(4));
                break;

            case "WCHT":
                client.displayWCHTmsg(text.substring(4));
                System.out.println("whisper chat message: " + text.substring(4));



            default:
                System.out.println("received from server " + text + ". This command " + command
                        + " is not implemented");
        }

    }

}
