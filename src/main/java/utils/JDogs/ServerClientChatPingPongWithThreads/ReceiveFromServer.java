package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiveFromServer implements Runnable {
    private Socket socket;
    private Client client;
    private Queue sendQueue;
    private Queue receiveQueue;
    private boolean running;
    private DataInputStream din;
    private ConnectionToServerMonitor connectionToServerMonitor;

    public ReceiveFromServer(Socket socket,Client client, Queue sendQueue,Queue receiveQueue, ConnectionToServerMonitor connectionToServerMonitor) {
        this.socket = socket;
        this.sendQueue = sendQueue;
        this.receiveQueue = receiveQueue;
        this.client = client;
        this.running = true;
        this.connectionToServerMonitor = connectionToServerMonitor;
        try {
            this.din = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
   public void run() {
        String message;


        try {
            while(running) {

                message = din.readUTF();
                    connectionToServerMonitor.message(System.currentTimeMillis());

                    if(message.equalsIgnoreCase("ping")) {
                        connectionToServerMonitor.sendSignal();
                    } else {
                        receiveQueue.enqueue(message);
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

            }
        } catch (IOException e) {
            client.newSetUp();
        }

        System.out.println(this.toString() + " stops now");

    }

    public void kill() {
        running = false;
    }
}
