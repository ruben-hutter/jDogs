package JDogs.ServerClientEnvironment;

/**
 * This thread will check if a connection to the server still exists.
 * If no messages are sent between client and server for approx. 10000mS.
 * If no respond arrives after 2 attempts, the client will end itself.
 */
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
            if (System.currentTimeMillis() - oldTime >= 10000) {
                if (tryToReachServer > 2) {
                    client.kill();
                }
            } else {
                // server was reached, set to zero
                tryToReachServer = 0;
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

    /**
     * Sets the oldTime to the new given from ReceiveFromServer.
     * @param timeInMilliSec
     * This parameter is given from ReceiveFromServer thread
     * and is used if a message with "ping" is received.
     */
    public void message(long timeInMilliSec) {
        oldTime = timeInMilliSec;
    }

    /**
     * Sends a pong-message to server.
     */
    public void sendSignal() {
        String pong = "pong";
        sendQueue.enqueue(pong);
    }

    /**
     * Counts the attempts to reach client by sending ping-messages.
     */
    public void tryToReach() {
        tryToReachServer++;
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }
}
