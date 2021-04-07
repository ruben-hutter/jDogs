package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.Monitorcs;
import jDogs.serverclient.helpers.Queuejd;
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
    private final Queuejd sendToAll;
    private final Queuejd sendToThisClient;
    private final Queuejd receivedFromClient;
    private SendFromServer sender;
    private ReceiveFromClient listeningToClient;
    private MessageHandlerServer messageHandlerServer;
    public boolean loggedIn;
    private boolean running;
    private Monitorcs monitorCS;
    private String nickName;
    ScheduledExecutorService scheduledExecutorService = null;

    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = new Queuejd();
        this.sendToThisClient = new Queuejd();
        this.receivedFromClient = new Queuejd();
        this.running = true;
        this.loggedIn = false;
        this.monitorCS = new Monitorcs();
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
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ConnectionToClientMonitor
                        (this, sendToThisClient, monitorCS), 5000,5000, TimeUnit.MILLISECONDS);


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


    public SendFromServer getSender () {
        return sender;
    }

    public void monitorMsg(long time) {
        this.monitorCS.receivedMsg(time);
    }


    synchronized public void kill() {
        try {
             System.out.println("stop ServerConnection..." + InetAddress.getLocalHost().getHostName() );
        } catch (UnknownHostException e) {
             e.printStackTrace();
        }
        scheduledExecutorService.shutdown();
        System.out.println(scheduledExecutorService.toString() + " stops now");

        server.removeSender(sender);
        server.removeNickname(messageHandlerServer.getNickName());
        this.listeningToClient.kill();
        this.sender.kill();
        this.messageHandlerServer.kill();

        running = false;
    }

    public void updateNickname(String nickName) {
        loggedIn = true;
        this.nickName = nickName;

    }
}