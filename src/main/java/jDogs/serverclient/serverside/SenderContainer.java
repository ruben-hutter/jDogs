package jDogs.serverclient.serverside;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SenderContainer {

    private final ServerConnection serverConnection;
    private SendToAll sendAll;
    private SendToPub sendPub;
    private SendToClient sendClient;
    private DataOutputStream dout;
    private Socket socket;

    public SenderContainer(ServerConnection serverConnection, Socket socket, BlockingQueue<String> sendToAllQueue,
             BlockingQueue<String> sendToPubQueue, BlockingQueue<String> sendToClientQueue) {
    this.socket = socket;
    this.serverConnection = serverConnection;

    setUp(sendToAllQueue,sendToPubQueue,sendToClientQueue);
    }

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
}
