package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiveFromServer implements Runnable {

    private final Queue sendQueue;
    private final boolean running;
    private DataInputStream din;

    public ReceiveFromServer(Socket socket, Queue sendQueue) {
        this.sendQueue = sendQueue;
        this.running = true;
        try {
            this.din = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        //monitoring thread

        ConnectionToServerMonitor connectionMonitor = new ConnectionToServerMonitor(sendQueue);
        Thread monitorThread = new Thread(connectionMonitor);

        try {
            while(running) {
                if ((message = din.readUTF()) != null) {
                    connectionMonitor.message(System.currentTimeMillis());
                    if(message.equalsIgnoreCase("ping")) {
                        connectionMonitor.sendSignal();
                    } else {

                        System.out.println("from server  " + message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
