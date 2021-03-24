package JDogs.ServerClientEnvironment;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ListeningToClients implements Runnable {
    private final Socket socket;
    private boolean running;
    private DataInputStream din;
    private Queue sendToAllClients;
    private final Queue receivedFromThisClient;
    private ServerConnection serverConnection;
    private final ConnectionToClientMonitor connectionToClientMonitor;

    public ListeningToClients(Socket socket, Queue sendToThisClient, Queue receivedFromThisClient, ServerConnection serverConnection, ConnectionToClientMonitor connectionToClientMonitor) {
        this.socket = socket;
        this.serverConnection = serverConnection;
        this.connectionToClientMonitor = connectionToClientMonitor;
        this.running = true;
        this.receivedFromThisClient = receivedFromThisClient;

    }


    @Override
     public void run() {

        try {
            din = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        String textIn;


        try {


            while (running) {


                if (din.available() != 0) {
                    textIn = din.readUTF();
                    connectionToClientMonitor.message(System.currentTimeMillis());
                    //heartbeat-signal
                    if (textIn.equals("pong")) {
                        connectionToClientMonitor.sendSignal();
                    } else {
                        //write to receiver-queue
                        receivedFromThisClient.enqueue(textIn);
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            din.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.toString() + "  stops now...");
    }


    public void kill() {
        this.connectionToClientMonitor.kill();
        running = false;
    }
}
