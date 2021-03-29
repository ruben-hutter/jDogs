package JDogs.ServerClientEnvironment;

public class ConnectionToServerMonitor implements Runnable {
    Queue sendToServer;
    Monitor monitor;
    Client client;

    ConnectionToServerMonitor(Client client,Queue sendToServer, Monitor monitor) {
        this.sendToServer = sendToServer;
        this.monitor = monitor;
        this.client = client;
    }

    @Override
    public void run() {
        if (monitor.connectionCheck()) {
            sendToServer.enqueue("pong");
        } else {
            System.out.println(this.toString() + " no ping message from server for over 10sec."
                    + "shutdown connection to server");
            client.kill();
        }
    }
}
