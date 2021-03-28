package JDogs.ServerClientEnvironment;

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
    private final Queue receiveQueue;
    private boolean running;
    private DataInputStream din;
    private final ConnectionToServerMonitor connectionToServerMonitor;

    public ReceiveFromServer(Socket socket, Client client, Queue receiveQueue,
                             ConnectionToServerMonitor connectionToServerMonitor) {
        this.receiveQueue = receiveQueue;
        this.client = client;
        this.running = true;
        this.connectionToServerMonitor = connectionToServerMonitor;
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
                    message = din.readUTF();
                    if (message.equalsIgnoreCase("ping")) {
                        connectionToServerMonitor.message(System.currentTimeMillis());
                        //connectionToServerMonitor.sendSignal();
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
