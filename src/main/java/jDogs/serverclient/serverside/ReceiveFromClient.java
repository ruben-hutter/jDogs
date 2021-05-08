package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Queuejd;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * This thread is receiving messages from client
 * received pong - messages are sent to Monitor-Thread
 * all others: are given to receiveQueue(handled by MessageHandlerThread)
 */
public class ReceiveFromClient implements Runnable {

    private final Socket socket;
    private boolean running;
    private DataInputStream din;
    private final BlockingQueue<String> receivedFromThisClient;
    private final ServerConnection serverConnection;

    /**
     * construct an object of receiveFromClient(a listener)
     * @param socket socket to client
     * @param receivedFromThisClient received from this client-thread-object
     * @param serverConnection sC object
     */
    public ReceiveFromClient(Socket socket, BlockingQueue<String> receivedFromThisClient,
            ServerConnection serverConnection) {
        this.socket = socket;
        this.serverConnection = serverConnection;
        this.running = true;
        this.receivedFromThisClient = receivedFromThisClient;
    }

    @Override
    public void run() {
        try {
            din = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String textIn = null;
        try {
            while (running) {
                if (din.available() != 0) {
                    textIn = din.readUTF();
                    serverConnection.monitorMsg(System.currentTimeMillis());
                    //write to receiver-queue
                    if (!textIn.equals("pong")) {
                        receivedFromThisClient.put(textIn);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            din.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.toString() + "  stops now...");
    }

    /**
     * Kills thread
     */
    public synchronized void kill() {
        running = false;
    }
}
