package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendFromClient implements Runnable {

    private Socket socket;
   private Queue sendQueue;
   private DataOutputStream dout;
   private boolean running;

    public SendFromClient(Socket socket, Queue sendQueue) {
        this.socket = socket;
        this.sendQueue = sendQueue;
        this.running = true;

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
    }

    public void sendStringToServer(String text) {

        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("problem sending...exiting");
            running = false;
        }
    }
}
