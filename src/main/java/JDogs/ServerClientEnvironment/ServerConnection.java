package JDogs.ServerClientEnvironment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private final Server server;
    private final Socket socket;
    //Queues: used to store messages, which should be sent
   private final Queue sendToAll;
   private final Queue sendToThisClient;
   private final Queue receivedFromClient;
   private SendFromServer sender;
   private ListeningToClients listeningToClient;
   private ConnectionToClientMonitor connectionToClientMonitor;
   private MessageHandlerServer messageHandlerServer;


    private int stopNumber;

    private boolean running;

    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = new Queue();
        this.sendToThisClient = new Queue();
        this.receivedFromClient = new Queue();
        this.running = true;
    }

    @Override
    public void run() {
        System.out.println("serverConnection");

        //sender thread
        this.sender = new SendFromServer(socket,server,sendToAll,sendToThisClient,this);
        stopNumber = server.connections.size();

        server.connections.add(sender);

        Thread senderThread = new Thread(sender);
        senderThread.start();
        System.out.println("thread sender name: " + senderThread.toString());

        //detect connection problems thread
        this.connectionToClientMonitor = new ConnectionToClientMonitor(sendToThisClient, this);
        Thread conMoThread = new Thread(connectionToClientMonitor);
        conMoThread.start();
        System.out.println("conMo thread: " + conMoThread.toString());


        //receivefromClient thread
        listeningToClient = new ListeningToClients(socket, sendToThisClient,receivedFromClient,this, connectionToClientMonitor);
        Thread listener = new Thread(listeningToClient);
        listener.start();
        System.out.println("thread listener name: " + listener.toString());

        //messageHandlerServer Thread
        messageHandlerServer = new MessageHandlerServer(server, this, sendToThisClient, sendToAll, receivedFromClient);
        Thread messenger = new Thread(messageHandlerServer);
        messenger.start();





        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public SendFromServer getSender () {
        return sender;
    }

     synchronized public void kill() {
        System.out.println("stop ServerConnection...");
        server.connections.remove(stopNumber);
        this.listeningToClient.kill();
        this.connectionToClientMonitor.kill();
        this.sender.kill();
        this.messageHandlerServer.kill();

        running = false;

    }


}
