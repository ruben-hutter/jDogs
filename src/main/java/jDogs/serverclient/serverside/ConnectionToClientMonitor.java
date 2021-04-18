package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.Monitorcs;
import jDogs.serverclient.helpers.Queuejd;

public class ConnectionToClientMonitor implements Runnable {

    Queuejd sendToClient;
    Monitorcs monitorCS;
    ServerConnection serverConnection;

    ConnectionToClientMonitor(ServerConnection serverConnection, Queuejd sendToClient, Monitorcs monitorCS) {
        this.sendToClient = sendToClient;
        this.monitorCS = monitorCS;
        this.serverConnection = serverConnection;
    }

    @Override
    public void run() {

        if (monitorCS.connectionCheck()) {
            //sendToClient.enqueue("ping");
            serverConnection.getSender().sendStringToClient("ping");
        } else {
            System.out.println(this.toString() + " no ping message from client for over 10sec."
                    + "shutdown connection to server");
            serverConnection.kill();
        }
    }
}


