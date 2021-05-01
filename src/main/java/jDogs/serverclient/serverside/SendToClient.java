package jDogs.serverclient.serverside;

public class SendToClient implements Runnable {

    boolean running;

    public SendToClient() {
        this.running = true;
    }

    @Override
    public void run() {

        while (running) {

            sender.sendToClient(sendQueue.take());

        }

        System.out.println(this.toString() + " stopps now");
    }

    public void kill() {
        running = false;
    }
}
