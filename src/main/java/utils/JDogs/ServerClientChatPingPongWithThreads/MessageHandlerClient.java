package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MessageHandlerClient implements Runnable{

    private boolean running;
    private final Queue receiveQueue;
    private final Queue sendQueue;

    public MessageHandlerClient(Queue receiveQueue, Queue sendQueue) {
        this.running = true;
        this.receiveQueue = receiveQueue;
        this.sendQueue = sendQueue;
    }

    @Override
    public void run() {
        String reply;
        while(running) {

            if (receiveQueue.isEmpty()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                reply = receiveQueue.dequeue();
                messageHandling(reply);
                }
            }

        System.out.println(this.toString() + " stops now");

    }

    public void messageHandling(String reply) {

        if(reply.substring(0,3).equals("jd ")) {

            if ("nickna".equals(reply.substring(3, 9))) {
                String defaultNickname = null;
                try {
                    defaultNickname = InetAddress.getLocalHost().getHostName();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                sendQueue.enqueue("jd nickna " + defaultNickname);
            } else {
                System.out.println("from server:  " + reply);
            }


        } else {
            System.out.println("from server:  " + reply);
        }

    }



    public void kill() {
        running = false;
    }
}
