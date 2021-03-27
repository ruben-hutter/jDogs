package JDogs.ServerClientEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/***
 * this thread processes messages received meaningfully.
 *
 *- it receives commands from server and reacts accordingly
 * e.g. if the server wants a default nickname, the messageHandler
 * sends the local hostname back to the server.
 * - it prints messages meant for the client into the cmd.
 *
 */

public class MessageHandlerClient implements Runnable{

    private boolean running;
    private final Queue receiveQueue;
    private final Queue sendQueue;
    private final Queue keyBoardInQueue;
    private Client client;
    private SendFromClient sendFromClient;

    public MessageHandlerClient(Client client, SendFromClient sendFromClient, Queue receiveQueue, Queue sendQueue, Queue keyBoardInQueue) {
        this.running = true;
        this.receiveQueue = receiveQueue;
        this.sendQueue = sendQueue;
        this.keyBoardInQueue = keyBoardInQueue;
        this.sendFromClient = sendFromClient;
        this.client = client;
    }

    @Override
    public void run() {
        String reply;
        while (running) {

            if (!receiveQueue.isEmpty()) {
                reply = receiveQueue.dequeue();

                if (reply.length() >= 4 && Protocol.isACommand(reply)) {
                    messageHandling(reply);
                } else {
                System.out.println("from server " + reply);
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

    public void messageHandling(String reply) {

        String command = reply.substring(0,4);

        switch (command) {

            case "USER":
                String name;
                System.out.println("server wants a nickname from you. Your default nickname "
                        + client.getHostName()
                        + " will be sent; accept by 'Y' or enter other name now");
                while (keyBoardInQueue.isEmpty()) {
                    //do nothing
                    //for later: wake up, if keyboardInput is not empty anymore or handle with commands
                }
                name = keyBoardInQueue.dequeue();

                if (name.equalsIgnoreCase("y")) {
                    sendQueue.enqueue("USER " + client.getHostName());
                } else {
                    sendQueue.enqueue("USER " + name);
                }
                //allow sending messages from keyboard to server
                sendFromClient.keyBoardInBlocked = false;
                break;
            default:
                System.out.println("received from server " + reply + ". But this command " + command + " is not implemented");
        }
    }

    public void kill() {
        running = false;
    }
}
