package JDogs.ServerClientEnvironment;

public class ConnectionToClientMonitor implements Runnable {

    Queue sendToClient;
    Monitor monitor;
    ServerConnection serverConnection;

    ConnectionToClientMonitor(ServerConnection serverConnection,Queue sendToClient, Monitor monitor) {
        this.sendToClient = sendToClient;
        this.monitor = monitor;
        this.serverConnection = serverConnection;
    }

    @Override
    public void run() {
        if (monitor.connectionCheck()) {
            sendToClient.enqueue("ping");
        } else {
            System.out.println(this.toString() + " no ping message from server for over 10sec."
                    + "shutdown connection to server");
            serverConnection.kill();
        }
    }
}


