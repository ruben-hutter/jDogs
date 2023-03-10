package jDogs.serverclient.clientside;

import jDogs.serverclient.helpers.Queuejd;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This thread is receiving messages from server
 * received ping - messages are sent to Monitor-Thread
 * all others: are given to receiveQueue(handled by MessageHandlerThread)
 */
public class ReceiveFromServer implements Runnable {

    private final Client client;
    private final Queuejd receiveQueue;
    private boolean running;
    private DataInputStream din;

    /**
     * constructor of ReceiveFromServer
     * @param socket socket for connection to server
     * @param client client instance
     * @param receiveQueue QueueJd to store messages received from server
     */
    public ReceiveFromServer(Socket socket, Client client, Queuejd receiveQueue) {
        this.receiveQueue = receiveQueue;
        this.client = client;
        this.running = true;
        try {
            this.din = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while(running) {
                if (din.available() != 0) {

                    client.monitorMsg(System.currentTimeMillis());

                    message = din.readUTF();

                    if (message.equalsIgnoreCase("ping")) {
                        //Do nothing
                    } else {
                        receiveQueue.enqueue(message);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("connection to server seems to be interrupted..will shut down");
            client.kill();
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
