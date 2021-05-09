package jDogs.serverclient.clientside;


import jDogs.gui.GUIManager;
import jDogs.serverclient.helpers.Monitorcs;
import jDogs.serverclient.helpers.Queuejd;
import javafx.application.Platform;

/**
 * the object of this class checks the connection to server
 */
public class ConnectionToServerMonitor implements Runnable {
    Queuejd sendToServer;
    Monitorcs monitorCS;
    Client client;

    /**
     * constructor of ConnectionToServerMonitor
     * @param client client instance
     * @param sendToServer QueueJd stores messages to send to server
     * @param monitorCS object to observe connection to server
     */
    ConnectionToServerMonitor(Client client, Queuejd sendToServer, Monitorcs monitorCS) {
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

            Platform.runLater(() -> GUIManager.getInstance().
                    gameWindowController.displayInfoFromClient("no ping message from server for over 10sec. "
                    + "shutdown now"));

            client.kill();
        }
    }
}
