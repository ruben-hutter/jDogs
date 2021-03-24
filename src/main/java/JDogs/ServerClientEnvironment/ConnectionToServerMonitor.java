package JDogs.ServerClientEnvironment;

public class ConnectionToServerMonitor implements Runnable {

    private long oldTime = -1;
    private boolean running = true;
    private final Queue sendQueue;
    private int tryToReachServer;
    private final Client client;

    public ConnectionToServerMonitor(Client client, Queue sendQueue) {
        this.sendQueue = sendQueue;
        this.client = client;
        this.tryToReachServer = 0;
    }

    @Override
    public void run() {

        while (running) {

            if (System.currentTimeMillis() - oldTime >= 10000 && tryToReachServer >= 5) {
                System.out.println("problem handling...exit" + tryToReachServer);
                client.newSetUp();
            }

            sendSignal();
            tryToReach();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(this.toString() + " stops now...");
    }

    //updates time of last incoming ping message from server
    public void message(long timeInMilliSec) {
        oldTime = timeInMilliSec;
    }

    public void sendSignal() {
        String pong = "pong";
        sendQueue.enqueue(pong);
    }

    public void tryToReach() {
        tryToReachServer++;
    }

    public void kill() {
        running = false;
    }
}
