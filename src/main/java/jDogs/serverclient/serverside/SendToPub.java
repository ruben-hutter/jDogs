package jDogs.serverclient.serverside;

public class SendToPub implements Runnable {
    boolean running;

    public SendToPub() {
        this.running = true;
    }

        @Override
        public void run() {

            while (running) {

                sender.sendToPub(sendQueue.take());

            }

            System.out.println(this.toString() + " stopps now");
        }

        public void kill() {
            this.running = false;
        }
    }

}
