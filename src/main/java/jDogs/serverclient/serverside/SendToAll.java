package jDogs.serverclient.serverside;

public class SendToAll implements Runnable {
        boolean running;
        public SendToAll() {
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

}
