package JDogs.ServerClientEnvironment;

public class Monitor {

    long newTime;

    Monitor() {
        this.newTime = System.currentTimeMillis();
    }

    //update time of last received ping/pong
    public void receivedMsg(long newTime) {
        this.newTime = newTime;

    }
    //return if last ping/pong received under 10 s;
    boolean connectionCheck() {

        if (System.currentTimeMillis() - this.newTime >= 10000) {
            return false;
        }
        return true;
    }

}
