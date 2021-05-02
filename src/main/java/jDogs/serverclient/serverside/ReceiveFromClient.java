package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Queuejd;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This thread is receiving messages from client
 * received pong - messages are sent to Monitor-Thread
 * all others: are given to receiveQueue(handled by MessageHandlerThread)
 */
public class ReceiveFromClient implements Runnable {

    private final Socket socket;
    private boolean running;
    private DataInputStream din;
    private final Queuejd receivedFromThisClient;
    private ServerConnection serverConnection;

    public ReceiveFromClient(Socket socket, Queuejd receivedFromThisClient,
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
                        receivedFromThisClient.enqueue(textIn);
                    }
                }
            }
        } catch (IOException e) {
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
