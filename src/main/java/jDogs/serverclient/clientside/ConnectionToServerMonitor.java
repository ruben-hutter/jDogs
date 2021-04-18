package jDogs.serverclient.clientside;


import jDogs.serverclient.helpers.Monitorcs;
import jDogs.serverclient.helpers.Queuejd;

public class ConnectionToServerMonitor implements Runnable {
    Queuejd sendToServer;
    Monitorcs monitorCS;
    Client client;

    ConnectionToServerMonitor(Client client, Queuejd sendToServer, Monitorcs monitorCS) {
        this.sendToServer = sendToServer;
        this.monitorCS = monitorCS;
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("connection to ServerMon start");

        if (monitorCS.connectionCheck()) {
            sendToServer.enqueue("pong");
        } else {
            System.out.println(this.toString() + " no ping message from server for over 10sec."
                    + "shutdown connection to server");
            client.kill();
        }
    }
}
