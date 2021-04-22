package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Queuejd;
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
    private final Queuejd sendToAll;
    private final Queuejd sendToThisClient;
    private final Queuejd sendToPub;
    private final ServerConnection serverConnection;

    public SendFromServer(Socket socket, Server server, Queuejd sendToAll, Queuejd sendToThisClient,Queuejd sendToPub,
            ServerConnection serverConnection) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = sendToAll;
        this.sendToPub = sendToPub;
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
                String message = sendToThisClient.dequeue();
                    sendStringToClient(message);
            }
            if (!sendToAll.isEmpty()) {
                String message = sendToAll.dequeue();
                sendStringToAllClients(message);
            }

            if (!sendToPub.isEmpty()) {
                String message = sendToPub.dequeue();
                sendStringToPublicLobbyGuests(message);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
            System.out.println("ServerConnection error 1: send String to Client error...." + serverConnection.getNickname());
            // kill this serverConnection:
            running = false;
            serverConnection.kill();
            // here: error handling needed, when Server can t be reached, program gets stuck here
        }
    }

    /**
     * Sends a message to all clients active
     * @param text a String message
     */
    synchronized public void sendStringToAllClients(String text) {
       for (ServerConnection serverConnection : server.getBasicConnections()) {
           serverConnection.getSender().sendStringToClient(text);
       }
    }

    synchronized public void sendStringToPublicLobbyGuests(String text) {
        for (ServerConnection serverConnection : server.getPublicLobbyConnections()) {
            serverConnection.getSender().sendStringToClient(text);
        }
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }
}
