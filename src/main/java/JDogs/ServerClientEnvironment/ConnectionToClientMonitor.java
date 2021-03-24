package JDogs.ServerClientEnvironment;

public class ConnectionToClientMonitor implements Runnable {

    private long oldTime = -1;
    private boolean running = true;
    private int tryToReachClient = 0;
    private final Queue sendToThisClient;
    private final ServerConnection serverConnection;


    /*
    This thread will check if a connection to the client still exists.
    If no messages are sent between client and server for approx. 10000mS.
    If no respond arrives after 10 attempts, the serverConnection will end itself.
     */

    public ConnectionToClientMonitor(Queue sendToThisClient, ServerConnection serverConnection) {
        this.sendToThisClient = sendToThisClient;
        this.serverConnection = serverConnection;

    }


    @Override
    public void run() {

        while (running) {

            if (System.currentTimeMillis() - oldTime >= 10000) {
                //kill connection
                if (tryToReachClient > 20) {
                    serverConnection.kill();
                } else {
                    //send signal

                    sendSignal();
                    tryToReach();
                    System.out.println(this.toString() + ": message sent");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //set to zero
                tryToReachClient = 0;

            }



        }

        System.out.println(this.toString() + "  stops now...");

    }

     public void message(long timeInMilliSec) {
       oldTime = timeInMilliSec;

    }

    public void sendSignal() {
        String ping = "ping";
        sendToThisClient.enqueue(ping);
    }

    public void tryToReach() {
        tryToReachClient++;
    }

    public void kill() {
        running = false;
    }

}
