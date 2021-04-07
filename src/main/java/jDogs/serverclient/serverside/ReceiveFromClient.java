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
    private Queuejd sendToAllClients;
    private final Queuejd receivedFromThisClient;
    private ServerConnection serverConnection;

    public ReceiveFromClient(Socket socket, Queuejd sendToThisClient, Queuejd receivedFromThisClient,
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


        String textIn;
        try {


            while (running) {


                if (din.available() != 0) {
                    textIn = din.readUTF();

                    //informs that any message arrived
                    //connectionToClientMonitor.message(System.currentTimeMillis());

                    //heartbeat-signal
                    if (textIn.equals("pong")) {
                        serverConnection.monitorMsg(System.currentTimeMillis());
                        //connectionToClientMonitor.sendSignal();
                    } else {
                        //write to receiver-queue
                        System.out.println("received:  " + textIn);
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
    public void kill() {
        running = false;
    }
}
