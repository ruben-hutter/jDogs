package JDogs.ServerClientEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/***
 * this thread processes messages received meaningfully.
 *
 *- it receives commands from server and reacts accordingly
 * e.g. if the server wants a default nickname, the messageHandler
 * sends the local hostname back to the server.
 * - it prints messages meant for the client into the cmd.
 *
 */

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
                    Thread.sleep(50);
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
