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
    private Thread sendClientThread;
    private Thread sendAllThread;
    private Thread sendPubThread;

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

        sendClientThread = new Thread(sendClient);
        sendClientThread.setName("sendClient-thrd" + socket.getLocalAddress().getHostName());
        sendClientThread.start();

        sendAllThread = new Thread(sendAll);
        sendAllThread.setName("sendAll-thrd" + socket.getLocalAddress().getHostName());
        sendAllThread.start();

        sendPubThread = new Thread(sendPub);
        sendPubThread.setName("sendPub-thrd" + socket.getLocalAddress().getHostName());
        sendPubThread.start();
    }

    public void sendToClient(String message) {
        try {
            dout.writeUTF(message);
            dout.flush();
        } catch (IOException e) {
            // error handling due to strange behaving threads
            e.printStackTrace();
            System.out.println("SenderContainer error: send String to Client error...." + serverConnection.getNickname());
            System.out.println("sendClientThreadState: " + sendClientThread.getState());
            System.out.println("sendAllThreadState: " + sendAllThread.getState());
            System.out.println("sendPubThreadState: " + sendPubThread.getState());

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
