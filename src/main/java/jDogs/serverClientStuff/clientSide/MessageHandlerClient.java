package jDogs.serverClientStuff.clientSide;

import jDogs.serverClientStuff.helpers.QueueJD;

/**
 * This thread processes messages received meaningfully.
 * <li>it receives commands from server and reacts accordingly</li>
 * e.g. if the server wants a default nickname, the messageHandler
 * sends the local hostname back to the server.
 * <li>it prints messages meant for the client into the cmd</li>
 */
public class MessageHandlerClient implements Runnable{

    private boolean running;
    private final QueueJD receiveQueue;
    private final QueueJD sendQueue;
    private final QueueJD keyBoardInQueue;
    private final Client client;
    private final SendFromClient sendFromClient;
    private ClientMenuCommand clientMenuCommand;
    private ClientGameCommand clientGameCommand;

    public MessageHandlerClient(Client client, SendFromClient sendFromClient, QueueJD receiveQueue,
            QueueJD sendQueue, QueueJD keyBoardInQueue) {
        this.running = true;
        this.receiveQueue = receiveQueue;
        this.sendQueue = sendQueue;
        this.keyBoardInQueue = keyBoardInQueue;
        this.sendFromClient = sendFromClient;
        this.client = client;
        this.clientMenuCommand = new ClientMenuCommand(client, sendFromClient,sendQueue,keyBoardInQueue);
        this.clientGameCommand = new ClientGameCommand();

    }

    @Override
    public void run() {
        String reply;
        while (running) {
            if (!receiveQueue.isEmpty()) {
                reply = receiveQueue.dequeue();
                System.out.println("before: " + reply.substring(0,4));
                if (reply.length() >= 4 && ClientMenuProtocol.isACommand(reply.substring(0,4))) {
                    System.out.println("MENU COMMAND " + reply.substring(0,4));
                    clientMenuCommand.execute(reply);
                } else {
                    if (reply.length() >= 4 && ClientGameProtocol
                            .isACommand(reply.substring(0, 4))) {
                        clientMenuCommand.execute(reply);
                    } else {
                        System.out.println("from server " + reply);
                    }
                }

            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(this.toString() + " stops now");
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }
}
