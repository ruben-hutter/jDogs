package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Monitorcs;

/**
 * the object of this class checks the connection from server to client
 */
public class ConnectionToClientMonitor implements Runnable {

    Monitorcs monitorCS;
    ServerConnection serverConnection;

    /**
     * constructs an object,
     * serverConnection to make calls,
     * in MonitorCS information is saved
     * @param serverConnection sc Object
     * @param monitorCS monitor object
     */
    ConnectionToClientMonitor(ServerConnection serverConnection, Monitorcs monitorCS) {
        this.monitorCS = monitorCS;
        this.serverConnection = serverConnection;
    }

    @Override
    public void run() {
        if (monitorCS.connectionCheck()) {
            //sendToClient.enqueue("ping");
            serverConnection.sendToClient("ping");
        } else {
            System.out.println(this + " no ping message from client for over 10sec."
                    + "shutdown connection to server");
            serverConnection.kill();
        }
    }
}


