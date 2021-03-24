package JDogs.ServerClientEnvironment;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
            e.printStackTrace();
            System.out.println("problem sending...");
            HandlingConnectionLoss();
        }
    }

    public void HandlingConnectionLoss() {

        while (true) {
            try {
                dout.close();
                dout = new DataOutputStream(socket.getOutputStream());
                break;
            } catch (IOException e) {
                //e.printStackTrace();
                //if connection fails:
                System.out.println(this.toString() + " cannot reconnect OutputStream to Server");
                client.newSetUp();
            }
        }

    }

    public void kill() {
        running = false;
    }
}
