package JDogs.ServerClientEnvironment;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This thread is the main thread of the connection to the client
 * here are lie the three Queues(sendtoClient, sendToAll, receivedFromClient)
 * used by the threads.
 * the threads are started here
 * the connection to the client can be ended here by other threads by kill()
 *
 * it is doubtful if serverConnection needs to be a thread since
 * it does not execute any commands or listens to client etc.
 *
 * stopNumber is the number of the sender-object saved
 * in the ArrayList of server.connections.
 * this enables to delete this sender-object from the list
 * to prevent errors in the other sender threads after disconnection of this client
 */
public class ServerConnection {

    private final Server server;
    private final Socket socket;
    private final Queue sendToAll;
    private final Queue sendToThisClient;
    private final Queue receivedFromClient;
    private SendFromServer sender;
    private ReceiveFromClient listeningToClient;
    private MessageHandlerServer messageHandlerServer;
    public boolean loggedIn;
    private boolean running;
    private Monitor monitor;
    ScheduledExecutorService scheduledExecutorService = null;

    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = new Queue();
        this.sendToThisClient = new Queue();
        this.receivedFromClient = new Queue();
        this.running = true;
        this.loggedIn = false;
        this.monitor = new Monitor();
    }


    public void createConnection() {
        System.out.println("serverConnection");

        // sender thread
        sender = new SendFromServer(socket, server, sendToAll, sendToThisClient,
                this);
        Thread senderThread = new Thread(sender);
        senderThread.start();
        System.out.println("thread sender name: " + senderThread.toString());

        // detect connection problems thread
        /*connectionToClientMonitor = new OldConnectionToClientMonitor(sendToThisClient,
                this);
        Thread conMoThread = new Thread(connectionToClientMonitor);
        conMoThread.start();
        System.out.println("conMo thread: " + conMoThread.toString());

         */

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ConnectionToClientMonitor(
                this, sendToThisClient, monitor),
                5000,5000, TimeUnit.MILLISECONDS);

                // receiveFromClient thread
        listeningToClient = new ReceiveFromClient(socket, sendToThisClient,receivedFromClient,
                this);
        Thread listener = new Thread(listeningToClient);
        listener.start();
        System.out.println("thread listener name: " + listener.toString());

        // messageHandlerServer Thread
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

    public void monitorMsg(long time) {
        this.monitor.receivedMsg(time);
    }


    synchronized public void kill() {
        try {
             System.out.println("stop ServerConnection..." + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
             e.printStackTrace();
        }
        scheduledExecutorService.shutdown();
        System.out.println(scheduledExecutorService.toString() + " stops now");

        server.connections.remove(sender);
        server.allNickNames.remove(messageHandlerServer.getNickName());
        this.listeningToClient.kill();
        this.sender.kill();
        this.messageHandlerServer.kill();

        running = false;
    }
}
