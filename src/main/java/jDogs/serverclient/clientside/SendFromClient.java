package jDogs.serverclient.clientside;

import jDogs.serverclient.helpers.Queuejd;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This thread is sending messages to server
 * and ends client if connection problems are detected
 */
public class SendFromClient implements Runnable {

    private final Socket socket;
    private final Queuejd sendQueue;
    private final Queuejd keyBoardInQueue;
    private DataOutputStream dout;
    private boolean running;
    public boolean keyBoardInBlocked;
    private final Client client;

    /**
     * constructor of sendFromClient
     * @param socket connection to server
     * @param client client instance
     * @param sendQueue Queuejd to send messages to server
     * @param keyBoardInQueue Queuejd to send messages from user to server
     */
    public SendFromClient(Socket socket,Client client, Queuejd sendQueue, Queuejd keyBoardInQueue) {
        this.socket = socket;
        this.sendQueue = sendQueue;
        this.running = true;
        this.keyBoardInBlocked = true;
        this.client = client;
        this.keyBoardInQueue = keyBoardInQueue;
    }

    @Override
    public void run() {
        try {
            this.dout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String reply;
        while(running) {
            if (!sendQueue.isEmpty()) {
                reply = sendQueue.dequeue();
                sendStringToServer(reply);
            }
            if (!keyBoardInBlocked && !keyBoardInQueue.isEmpty()) {
                reply = keyBoardInQueue.dequeue();
                System.out.println("keyboard sending: " + reply);
                sendStringToServer(reply);
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
        System.out.println(this.toString() + " stops now");
    }

    /**
     * Sends a message to the server
     * @param text a String message
     */
    public void sendStringToServer(String text) {
        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("problem sending...");
            HandlingConnectionLoss();
        }
    }

    /**
     * First attempt to handle connection losses;
     * it should reSetUp connection to server, but is at the moment
     * disabled due to problems with threads not finishing
     * before restarting.
     * the method just kills the client at the moment.
     */
    public void HandlingConnectionLoss() {
        try {
            dout.close();
            // dout = new DataOutputStream(socket.getOutputStream());
            System.err.println(this.toString() + " cannot reconnect OutputStream to Server");
            client.kill();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }

}
