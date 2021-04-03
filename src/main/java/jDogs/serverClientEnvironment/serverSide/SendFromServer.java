package jDogs.serverClientEnvironment.serverSide;

import jDogs.serverClientEnvironment.helpers.QueueJD;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * purpose of this thread is sending messages to client
 * and ends serverConnection if connection problems are detected
 */
public class SendFromServer implements Runnable {

    private boolean running;
    private DataOutputStream dout;
    private final Socket socket;
    private final Server server;
    private final QueueJD sendToAll;
    private final QueueJD sendToThisClient;
    private final ServerConnection serverConnection;

    public SendFromServer(Socket socket, Server server, QueueJD sendToAll, QueueJD sendToThisClient,
            ServerConnection serverConnection) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = sendToAll;
        this.sendToThisClient = sendToThisClient;
        this.running = true;
        this.serverConnection = serverConnection;
    }

    @Override
    public void run() {
        try {
            dout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (running) {
            if (!sendToThisClient.isEmpty()) {
                sendStringToClient(sendToThisClient.dequeue());
            }
            if (!sendToAll.isEmpty()) {
                sendStringToAllClients(sendToAll.dequeue());
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.toString() + "  stops now...");
        try {
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to a specific client
     * @param text a String message
     */
    synchronized public void sendStringToClient(String text) {
        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println("ServerConnection error 1: send String to Client error....");
            // kill this serverConnection:
            server.connections.remove(this);
            running = false;
            serverConnection.kill();
            // here: error handling needed, when Server can t be reached, program gets stuck here
        }
    }

    /**
     * Sends a message to all clients
     * @param text a String message
     */
    synchronized public void sendStringToAllClients(String text) {
        for (int index = 0; index < server.connections.size(); index++) {
            System.out.println();
            server.connections.get(index).sendStringToClient(text);
            //sfS.sendStringToClient(text);
        }
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }
}
