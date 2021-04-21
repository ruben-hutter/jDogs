package jDogs.serverclient.helpers;

public class Monitorcs {
    /**
     * this class is used to store the last
     * received ping/pong message and the method connectionCheck is
     * given to the ConnectionToClientMonitor/
     * ConnectionToServerMonitor to check the
     * connection and to eventually
     * shutdown the connection
     */

    long newTime;

    public Monitorcs() {
        this.newTime = System.currentTimeMillis();
    }

    //update time of last received ping/pong
    public void receivedMsg(long newTime) {
        this.newTime = newTime;

    }
    //return if last ping/pong received under 10 s;
    public boolean connectionCheck() {
        if (System.currentTimeMillis() - this.newTime >= 10000) {
            System.out.println("connCheck false: " + (System.currentTimeMillis() - this.newTime));
            return false;
        }
        return true;
    }

}
