package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendFromServer implements Runnable {

    private boolean running;
    private DataOutputStream dout;
    private Socket socket;
    private Server server;
    private Queue sendToAll;
    private Queue sendToThisClient;
    private ServerConnection serverConnection;

    public SendFromServer(Socket socket, Server server, Queue sendToAll, Queue sendToThisClient,ServerConnection serverConnection) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = sendToAll;
        this.sendToThisClient = sendToThisClient;
        this.running = true;
        this.serverConnection = serverConnection;

    }

    @Override
    public void run() {


        try {
            dout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text;


        while (running) {


                if (running && !sendToThisClient.isEmpty()) {

                    sendStringToClient(sendToThisClient.dequeue());
                }

                if (running && !sendToAll.isEmpty()) {

                    sendStringToAllClients(sendToAll.dequeue());
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread sleep fehler");
                }



        }
        System.out.println(this.toString() + "  stops now...");
        try {
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    synchronized public void sendStringToClient(String text) {
        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ServerConnection error 1: send String to Client error....");
            //kill this serverConnection:
            server.connections.remove(this);
            running = false;
            serverConnection.kill();

            //here: error handling needed, when Server can t be reached, program gets stuck here

        }

    }


    synchronized public void sendStringToAllClients(String text) {
        for (int index = 0; index < server.connections.size(); index++) {
            System.out.println();
            server.connections.get(index).sendStringToClient(text);
            //sfS.sendStringToClient(text);
        }
    }

    public void kill() {
        System.out.println(this.toString() + "  should kill itself");
        running = false;
    }
}
