package jDogs.serverclient.serverside;

import java.util.concurrent.BlockingQueue;

/**
 * the object of this class is
 * a thread that sends to the public
 */
public class SendToPub implements Runnable {

    private final BlockingQueue<String> sendPub;
    private boolean running;

    /**
     * construct SendToPub
     * @param sendPub blocking queue to send to public lobby
     */
    public SendToPub(BlockingQueue<String> sendPub) {
        this.sendPub = sendPub;
        this.running = true;
    }

    @Override
    public void run() {
        try {
            while (running) {
                try {
                    Server.getInstance().sendMessageToAll(sendPub.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("set running false");
                    kill();
                }
            }
        } catch (Throwable t) {
        System.out.println(this.toString() + " throwed throwable");
        t.printStackTrace();
        }
        System.out.println(this.toString() + " stopps now");
    }

    /**
     * kills this thread
     */
    public void kill() {
        this.running = false;
    }
}


