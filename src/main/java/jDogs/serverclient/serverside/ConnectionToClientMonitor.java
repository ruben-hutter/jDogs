package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.MonitorCS;
import jDogs.serverclient.helpers.QueueJD;

public class ConnectionToClientMonitor implements Runnable {

    QueueJD sendToClient;
    MonitorCS monitorCS;
    ServerConnection serverConnection;

    ConnectionToClientMonitor(ServerConnection serverConnection,QueueJD sendToClient, MonitorCS monitorCS) {
        this.sendToClient = sendToClient;
        this.monitorCS = monitorCS;
        this.serverConnection = serverConnection;
    }

    @Override
    public void run() {
        if (monitorCS.connectionCheck()) {
            sendToClient.enqueue("ping");
        } else {
            System.out.println(this.toString() + " no ping message from server for over 10sec."
                    + "shutdown connection to server");
            serverConnection.kill();
        }
    }
}


