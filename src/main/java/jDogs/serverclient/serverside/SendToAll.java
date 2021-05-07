package jDogs.serverclient.serverside;

import java.util.concurrent.BlockingQueue;

public class SendToAll implements Runnable {

    private final BlockingQueue<String> sendAll;
    boolean running;

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

    public void kill() {
        running = false;
    }
}


