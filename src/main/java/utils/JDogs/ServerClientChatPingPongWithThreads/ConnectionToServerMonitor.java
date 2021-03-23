package utils.JDogs.ServerClientChatPingPongWithThreads;

public class ConnectionToServerMonitor implements Runnable {

        private long oldTime = -1;
        private boolean running = true;
        private Queue sendQueue;
        private int tryToReachServer;
        private Client client;

        public ConnectionToServerMonitor(Client client, Queue sendQueue) {
            this.sendQueue = sendQueue;
            this.client = client;
            this.tryToReachServer = 0;

        }

        @Override
        public void run() {

            while (running) {

                if (System.currentTimeMillis() - oldTime >= 10000) {

                    if (tryToReachServer >= 20) {
                        System.out.println("problem handling...exit" + tryToReachServer);
                        client.newSetUp();
                    } else {
                        sendSignal();
                        tryToReach();

                    }
                } else {
                    tryToReachServer = 0;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            System.out.println(this.toString() + " stops now...");

        }
        //update time of last incoming message
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
