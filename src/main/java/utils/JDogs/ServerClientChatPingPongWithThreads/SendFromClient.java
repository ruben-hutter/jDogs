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
                try {
                    Thread.sleep(10);
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
            e.printStackTrace();
            System.out.println("problem sending...");
            running = false;
        }
    }

    public void kill() {
        running = false;
    }
}
