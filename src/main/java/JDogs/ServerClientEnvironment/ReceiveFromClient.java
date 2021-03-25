package JDogs.ServerClientEnvironment;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * purpose of this thread is receiving messages from client
 * received pong - messages are sent to Monitor-Thread
 * all others: are given to receiveQueue(handled by MessageHandlerThread)
 */

public class ReceiveFromClient implements Runnable {
    private final Socket socket;
    private boolean running;
    private DataInputStream din;
    private Queue sendToAllClients;
    private final Queue receivedFromThisClient;
    private ServerConnection serverConnection;
    private final ConnectionToClientMonitor connectionToClientMonitor;

    public ReceiveFromClient(Socket socket, Queue sendToThisClient, Queue receivedFromThisClient, ServerConnection serverConnection, ConnectionToClientMonitor connectionToClientMonitor) {
        this.socket = socket;
        this.serverConnection = serverConnection;
        this.connectionToClientMonitor = connectionToClientMonitor;
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


        String textIn;


        try {


            while (running) {


                if (din.available() != 0) {
                    textIn = din.readUTF();

                    //informs that any message arrived
                    //connectionToClientMonitor.message(System.currentTimeMillis());

                    //heartbeat-signal
                    if (textIn.equals("pong")) {
                        connectionToClientMonitor.message(System.currentTimeMillis());
                        connectionToClientMonitor.sendSignal();
                    } else {
                        //write to receiver-queue
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


    public void kill() {
        this.connectionToClientMonitor.kill();
        running = false;
    }
}
