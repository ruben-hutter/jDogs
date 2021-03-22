package utils.JDogs.ServerClientChatPingPongWithThreads;

public class ConnectionToClientMonitor implements Runnable {

    private long oldTime = -1;
    private boolean running = true;
    private final Queue sendToThisClient;

    public ConnectionToClientMonitor(Queue sendToThisClient, ServerConnection serverConnection) {
        this.sendToThisClient = sendToThisClient;
    }

    @Override
    public void run() {

        while (running) {
            if (System.currentTimeMillis() - oldTime >= 10000) {
                sendSignal();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("connectionMonitor" + this.toString() + ": message sent");

            }
        }

        System.out.println(this.toString() + "  stops now...");
    }

    synchronized public void message(long timeInMilliSec) {
       oldTime = timeInMilliSec;
    }

    public void sendSignal() {
        String ping = "ping";
        sendToThisClient.enqueue(ping);
    }

    public void kill() {
        running = false;
    }

}
