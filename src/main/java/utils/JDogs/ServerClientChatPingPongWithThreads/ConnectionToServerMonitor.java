package utils.JDogs.ServerClientChatPingPongWithThreads;

public class ConnectionToServerMonitor implements Runnable {

    private long newTime = 0;
    private long oldTime = -1;
    private final Queue sendQueue;

    public ConnectionToServerMonitor(Queue sendQueue) {
        this.sendQueue = sendQueue;
    }

    @Override
    public void run() {
        // while running
        while (true) {
            if (newTime - oldTime > 100000) {
                sendSignal();
                System.out.println("connectionMonitor: message sent");
            }
        }
    }

    public void message(long timeInMilliSec) {
        if (oldTime < 0) {
            oldTime = timeInMilliSec;
        } else {
            oldTime = newTime;
            newTime = timeInMilliSec;
        }
    }

    public void sendSignal() {
        String pong = "pong";
        sendQueue.enqueue(pong);
    }
}
