package jDogs.serverClientEnvironment.clientSide;

import jDogs.serverClientEnvironment.helpers.MonitorCS;
import jDogs.serverClientEnvironment.helpers.QueueJD;

public class ConnectionToServerMonitor implements Runnable {
    QueueJD sendToServer;
    MonitorCS monitorCS;
    Client client;

    ConnectionToServerMonitor(Client client,QueueJD sendToServer, MonitorCS monitorCS) {
        this.sendToServer = sendToServer;
        this.monitorCS = monitorCS;
        this.client = client;
    }

    @Override
    public void run() {
        if (monitorCS.connectionCheck()) {
            sendToServer.enqueue("pong");
        } else {
            System.out.println(this.toString() + " no ping message from server for over 10sec."
                    + "shutdown connection to server");
            client.kill();
        }
    }
}
