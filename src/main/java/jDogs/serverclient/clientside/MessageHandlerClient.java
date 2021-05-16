package jDogs.serverclient.clientside;


import jDogs.serverclient.helpers.Queuejd;


/**
 * This thread processes messages received meaningfully.
 * - it receives commands from server and reacts accordingly
 * e.g. if the server wants a default nickname, the messageHandler
 * sends the local hostname back to the server.
 * - it prints messages meant for the client into the cmd
 */
public class MessageHandlerClient implements Runnable{

    private boolean running;
    private final Queuejd receiveQueue;
    private final Queuejd sendQueue;
    private final Queuejd keyBoardInQueue;
    private final Client client;
    private final SendFromClient sendFromClient;
    private ClientMenuCommand clientMenuCommand;
    private ClientGameCommand clientGameCommand;

    /**
     * constructor of an object of MessageHandlerClient
     * @param client client instance
     * @param sendFromClient send messages to server-thread
     * @param receiveQueue receive messages from server-thread
     * @param sendQueue Queuejd store messages to send to server
     * @param keyBoardInQueue Queuejd store messages from user to send to server
     */
    public MessageHandlerClient(Client client, SendFromClient sendFromClient, Queuejd receiveQueue,
            Queuejd sendQueue, Queuejd keyBoardInQueue) {
        this.running = true;
        this.receiveQueue = receiveQueue;
        this.sendQueue = sendQueue;
        this.keyBoardInQueue = keyBoardInQueue;
        this.sendFromClient = sendFromClient;
        this.client = client;
        this.clientMenuCommand = new ClientMenuCommand(client, sendFromClient,sendQueue,keyBoardInQueue);
        this.clientGameCommand = new ClientGameCommand(client, sendFromClient,sendQueue,keyBoardInQueue);

    }
    @Override
    public void run() {
        String reply;
        while (running) {
            if (!receiveQueue.isEmpty()) {
                reply = receiveQueue.dequeue();
                if (reply.length() >= 4 && ClientMenuProtocol.isACommand(reply.substring(0, 4))) {
                        clientMenuCommand.execute(reply);
                        System.out.println("received from server: " + reply);
                } else {
                    if (reply.length() >= 4 && ClientGameProtocol
                            .isACommand(reply.substring(0, 4))) {
                        clientGameCommand.execute(reply);
                    } else {
                        System.out.println("message doesn't match Menu or GameProtocol: " + reply);
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
        System.out.println(this + " stops now");
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }

    //specify particular lobbyGroup

}
