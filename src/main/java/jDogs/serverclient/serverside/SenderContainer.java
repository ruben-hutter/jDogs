package jDogs.serverclient.serverside;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * from the object of this class all three sender threads
 * will be started and from send messages to
 * this client will be sent
 */
public class SenderContainer {

    private final ServerConnection serverConnection;
    private SendToAll sendAll;
    private SendToPub sendPub;
    private SendToClient sendClient;
    private DataOutputStream dout;
    private Socket socket;

    /**
     * construct container
     * @param serverConnection
     * @param socket
     * @param sendToAllQueue
     * @param sendToPubQueue
     * @param sendToClientQueue
     */
    public SenderContainer(ServerConnection serverConnection, Socket socket, BlockingQueue<String> sendToAllQueue,
             BlockingQueue<String> sendToPubQueue, BlockingQueue<String> sendToClientQueue) {
    this.socket = socket;
    this.serverConnection = serverConnection;

    setUp(sendToAllQueue,sendToPubQueue,sendToClientQueue);
    }

    /**
     * sets up all three threads with their respective blocking queue
     * @param sendToAllQueue
     * @param sendToPubQueue
     * @param sendToClientQueue
     */
    private void setUp(BlockingQueue<String> sendToAllQueue, BlockingQueue<String> sendToPubQueue, BlockingQueue<String> sendToClientQueue) {
        try {
            dout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendAll = new SendToAll(sendToAllQueue);

        sendPub = new SendToPub(sendToPubQueue);

        sendClient = new SendToClient(this, sendToClientQueue);

        Thread sendClientThread = new Thread(sendClient);
        sendClientThread.start();

        Thread sendAllThread = new Thread(sendAll);
        sendAllThread.start();

        Thread sendPubThread = new Thread(sendPub);
        sendPubThread.start();
    }

    /**
     * sends messages to client
     * @param message
     */
    public void sendToClient(String message) {
        try {
            dout.writeUTF(message);
            dout.flush();
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("SenderContainer error: send String to Client error...." + serverConnection.getNickname());

            // delete this serverConnection:
            sendClient.kill();
            sendPub.kill();
            sendAll.kill();
            serverConnection.kill();
            // here: error handling needed, when Server can t be reached, program gets stuck here
        }
    }

    /**
     * stop all three sender threads
     */
    public void kill() {
        sendClient.kill();
        sendPub.kill();
        sendAll.kill();
    }
}
