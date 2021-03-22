package ServerClientExtended;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiveFromServer implements Runnable {
    private Socket socket;
    private Queue sendQueue;
    private boolean running;
    private DataInputStream din;
    private ConnectionToServerMonitor connectionToServerMonitor;

    public ReceiveFromServer(Socket socket, Queue sendQueue, ConnectionToServerMonitor connectionToServerMonitor) {
        this.socket = socket;
        this.sendQueue = sendQueue;
        this.running = true;
        this.connectionToServerMonitor = connectionToServerMonitor;
        try {
            this.din = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
   synchronized public void run() {
        String message;


        try {
            while(running) {
                if ((message = din.readUTF()) != null) {
                    connectionToServerMonitor.message(System.currentTimeMillis());
                    if(message.equalsIgnoreCase("ping")) {
                        connectionToServerMonitor.sendSignal();
                    } else {

                        System.out.println("from server  " + message);
                    }

                } else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(this.toString() + " stops now");

    }

    public void kill() {
        running = false;
    }
}
