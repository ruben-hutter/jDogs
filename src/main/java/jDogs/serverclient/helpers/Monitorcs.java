package jDogs.serverclient.helpers;
/**
 * this class is used to store the last
 * received ping/pong message and the method connectionCheck is
 * given to the ConnectionToClientMonitor/
 * ConnectionToServerMonitor to check the
 * connection and to eventually
 * shutdown the connection
 */
public class Monitorcs {

    long newTime;

    /**
     * constructor of new Monitorcs object
     */
    public Monitorcs() {
        this.newTime = System.currentTimeMillis();
    }

    /**
     * updates the time of last received ping/pong message
     * @param newTime long
     */
    public void receivedMsg(long newTime) {
        this.newTime = newTime;

    }

    /**
     * check if connection is alive
     * @return true if last ping/pong received in less than 10s
     */
    public boolean connectionCheck() {
        return System.currentTimeMillis() - this.newTime < 10000;
    }
}
