package JDogs.ServerClientEnvironment;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * this thread is the main thread of the connection to the client
 * here are lie the three Queues(sendtoClient, sendToAll, receivedFromClient)
 * used by the threads.
 * the threads are started here
 * the connection to the client can be ended here by other threads by kill()
 *
 * it is doubtful if serverConnection needs to be a thread since
 * it does not execute any commands or listens to client etc.
 *
 */

public class ServerConnection implements Runnable {

    private final Server server;
    private final Socket socket;
    private final Queue sendToAll;
    private final Queue sendToThisClient;
    private final Queue receivedFromClient;
    private SendFromServer sender;
    private ReceiveFromClient listeningToClient;
    private ConnectionToClientMonitor connectionToClientMonitor;
    private MessageHandlerServer messageHandlerServer;
    public boolean loggedIn;

    /** stopNumber is the number of the sender-object saved
     * in the ArrayList of server.connections.
     * this enables to delete this sender-object from the list
     * to prevent errors in the other sender threads after disconnection of this client
     */


    private boolean running;

    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = new Queue();
        this.sendToThisClient = new Queue();
        this.receivedFromClient = new Queue();
        this.running = true;
        this.loggedIn = false;
    }

    @Override
    public void run() {
        System.out.println("serverConnection");

        //sender thread
        sender = new SendFromServer(socket, server, sendToAll, sendToThisClient,
                this);

        Thread senderThread = new Thread(sender);
        senderThread.start();
        System.out.println("thread sender name: " + senderThread.toString());

        //detect connection problems thread
        connectionToClientMonitor = new ConnectionToClientMonitor(sendToThisClient,
                this);
        Thread conMoThread = new Thread(connectionToClientMonitor);
        conMoThread.start();
        System.out.println("conMo thread: " + conMoThread.toString());


        //receivefromClient thread
        listeningToClient = new ReceiveFromClient(socket, sendToThisClient,receivedFromClient,
                this, connectionToClientMonitor);
        Thread listener = new Thread(listeningToClient);
        listener.start();
        System.out.println("thread listener name: " + listener.toString());

        //messageHandlerServer Thread
        messageHandlerServer = new MessageHandlerServer(server,
                this, sendToThisClient, sendToAll, receivedFromClient);
        Thread messenger = new Thread(messageHandlerServer);
        messenger.start();

    }

    public void loggedIn() {
        server.connections.add(sender);
        System.out.println(this.toString() + " logged in ");

        loggedIn = true;
    }

    public SendFromServer getSender () {
        return sender;
    }

     synchronized public void kill() {
        try {
             System.out.println("stop ServerConnection..." + InetAddress.getLocalHost().getHostName() );
        } catch (UnknownHostException e) {
             e.printStackTrace();
        }
        server.connections.remove(sender);
        server.allNickNames.remove(messageHandlerServer.getNickName());
        this.listeningToClient.kill();
        this.connectionToClientMonitor.kill();
        this.sender.kill();
        this.messageHandlerServer.kill();

        running = false;

    }
}
