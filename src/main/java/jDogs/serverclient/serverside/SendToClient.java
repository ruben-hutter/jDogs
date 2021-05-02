package jDogs.serverclient.serverside;

import java.util.concurrent.BlockingQueue;

public class SendToClient implements Runnable {

    private final BlockingQueue<String> sendClient;
    private final SenderContainer senderContainer;
    private boolean running;

    public SendToClient(SenderContainer senderContainer, BlockingQueue<String> sendClient) {
        this.sendClient = sendClient;
        this.senderContainer = senderContainer;
        this.running = true;
    }

    @Override
    public void run() {

    while (running) {

        try {
            senderContainer.sendToClient(sendClient.take());
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
