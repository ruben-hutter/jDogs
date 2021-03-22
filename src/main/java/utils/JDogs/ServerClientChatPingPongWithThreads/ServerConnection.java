package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private Server server;
    private Socket socket;
    //Queues: used to store messages, which should be sent
   private Queue sendToAll;
   private Queue sendToThisClient;
   private SendFromServer sender;
   private ListeningToClients listeningToClient;
   private ConnectionToClientMonitor connectionToClientMonitor;
    private DataInputStream din;
    private DataOutputStream dout;

    private int stopNumber;

    private boolean running;

    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = new Queue();
        this.sendToThisClient = new Queue();
        this.running = true;
    }

    @Override
    public void run() {
        System.out.println("serverConnection");
        //sending thread: uses the two queues
        SendFromServer sender = new SendFromServer(socket,server,sendToAll,sendToThisClient,this);
        stopNumber = server.connections.size();

        server.connections.add(sender);
        Thread senderThread = new Thread(sender);
        senderThread.start();
        System.out.println("thread sender name: " + senderThread.toString());

        //start ConnectionCheck

        //start thread to detect connection problems
        this.connectionToClientMonitor = new ConnectionToClientMonitor(sendToThisClient, this);
        Thread conMoThread = new Thread(connectionToClientMonitor);
        conMoThread.start();

        //start receivefromClient
        listeningToClient = new ListeningToClients(socket, sendToThisClient,sendToAll,this, connectionToClientMonitor);
        Thread listener = new Thread(listeningToClient);
        listener.start();
        System.out.println("thread listener name: " + listener.toString());





        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        System.out.println("ending");

    this.listeningToClient.kill();
    this.connectionToClientMonitor.kill();



    }

    public SendFromServer getSender () {
        return sender;
    }

    synchronized public void kill() {
        System.out.println("stop ServerConnection...");

        running = false;
    }

    public void print(long print) {
        System.out.println(print);
    }
}
