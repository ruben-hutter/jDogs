package JDogs.ServerClientEnvironment;

public class MonitorCS {

    long newTime;

    public MonitorCS() {
        this.newTime = System.currentTimeMillis();
    }

    //update time of last received ping/pong
    public void receivedMsg(long newTime) {
        this.newTime = newTime;

    }
    //return if last ping/pong received under 10 s;
    public boolean connectionCheck() {

        if (System.currentTimeMillis() - this.newTime >= 10000) {
            return false;
        }
        return true;
    }

}
