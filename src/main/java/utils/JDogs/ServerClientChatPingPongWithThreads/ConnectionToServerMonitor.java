package ServerClientExtended;

    public class ConnectionToServerMonitor implements Runnable {

        private long oldTime = -1;
        private boolean running = true;
        private Queue sendQueue;
        private int tryToReachServer;

        public ConnectionToServerMonitor(Queue sendQueue) {
            this.sendQueue = sendQueue;
            this.tryToReachServer = 0;

        }

        @Override
        public void run() {

            while (running) {

                if (System.currentTimeMillis() - oldTime >= 10000) {

                    if (tryToReachServer >= 5) {
                        System.out.println("problem handling...exit");
                        this.kill();
                    }
                    sendSignal();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tryToReach();
                } else {
                    tryToReachServer = 0;
                }


            }

            System.out.println(this.toString() + " stops now...");

        }
        //update time of last incoming message
       synchronized public void message(long timeInMilliSec) {
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
