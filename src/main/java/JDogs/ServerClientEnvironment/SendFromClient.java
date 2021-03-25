package JDogs.ServerClientEnvironment;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/***
 * purpose of this thread is sending messages to server
 * and ends client if connection problems are detected
 */

public class SendFromClient implements Runnable {

    private final Socket socket;
    private final Queue sendQueue;
    private DataOutputStream dout;
    private boolean running;
    private final Client client;

    public SendFromClient(Socket socket,Client client, Queue sendQueue) {
        this.socket = socket;
        this.sendQueue = sendQueue;
        this.running = true;
        this.client = client;
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
            if(!sendQueue.isEmpty()) {
                reply = sendQueue.dequeue();
                sendStringToServer(reply);
            }

            try {
                Thread.sleep(50);
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

    /***
     * this method is a first attempt to handle connection losses;
     * it should reSetUp connection to server, but is at the moment
     * disabled due to problems with threads not finishing
     * before restarting.
     * the method just kills the client at the moment.
     */

    public void HandlingConnectionLoss() {


        try {
            dout.close();
            //dout = new DataOutputStream(socket.getOutputStream());
            System.err.println(this.toString() + " cannot reconnect OutputStream to Server");
            client.kill();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kill() {
        running = false;
    }
}
