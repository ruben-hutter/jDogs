package JDogs.ServerClientEnvironment;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

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
                    //informs that any message arrived
                    //connectionToServerMonitor.message(System.currentTimeMillis());

                    if (message.equalsIgnoreCase("ping")) {
                        connectionToServerMonitor.message(System.currentTimeMillis());
                        connectionToServerMonitor.sendSignal();
                    } else {
                        receiveQueue.enqueue(message);
                    }
                }
            }
        } catch (IOException e) {
            client.newSetUp();
        }

        System.out.println(this.toString() + " stops now");

    }

    public void kill() {
        running = false;
    }
}
