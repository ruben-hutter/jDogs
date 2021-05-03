package jDogs.serverclient.serverside;

import java.util.concurrent.BlockingQueue;

public class SendToPub implements Runnable {

    private final BlockingQueue<String> sendPub;
    private boolean running;

    public SendToPub(BlockingQueue<String> sendPub) {
        this.sendPub = sendPub;
        this.running = true;
    }

    @Override
    public void run() {

        while (running) {
            try {
                Server.getInstance().sendMessageToAll(sendPub.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("set running false");
                kill();
            }
        }
        System.out.println(this.toString() + " stopps now");
    }

    public void kill() {
        this.running = false;
    }
}

