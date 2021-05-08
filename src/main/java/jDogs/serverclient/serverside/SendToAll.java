package jDogs.serverclient.serverside;

import java.util.concurrent.BlockingQueue;
/**
 * the object of this class is
 * a thread that sends to all users
 */
public class SendToAll implements Runnable {

    private final BlockingQueue<String> sendAll;
    boolean running;

    /**
     * Contstruct SendToAll Thread
     * @param sendAll blockingQueue to send to all
     */
    public SendToAll(BlockingQueue<String> sendAll) {
        this.sendAll = sendAll;
        this.running = true;
        }

    @Override
    public void run() {
        while (running) {
            try {
                Server.getInstance().sendMessageToAll(sendAll.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("set running false");
                kill();
            }
        }
        System.out.println(this.toString() + " stopps now");
        }

    /**
     * kills this thread
     */
    public void kill() {
        running = false;
    }
}


