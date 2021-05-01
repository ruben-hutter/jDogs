package jDogs.serverclient.serverside;


import jDogs.serverclient.helpers.Monitorcs;
import jDogs.serverclient.helpers.Queuejd;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This thread is the main thread of the connection to the client
 * here are lie the three Queues(sendtoClient, sendToAll, receivedFromClient)
 * used by the threads.
 * the threads are started here
 * the connection to the client can be ended here by other threads by delete()
 *
 * it is doubtful if serverConnection needs to be a thread since
 * it does not execute any commands or listens to client etc.
 *
 * stopNumber is the number of the sender-object saved
 * in the ArrayList of server.senderlist.
 * this enables to delete this sender-object from the list
 * to prevent errors in the other sender threads after disconnection of this client
 */
public class ServerConnection {

    private final Server server;
    private final Socket socket;
    private final Queuejd sendToAll;
    private final Queuejd sendToThisClient;
    private final Queuejd receivedFromClient;
    private final Queuejd sendToPub;
    private SendFromServer sender;
    private ReceiveFromClient listeningToClient;
    private MessageHandlerServer messageHandlerServer;
    public boolean loggedIn;
    private boolean running;
    private Monitorcs monitorCS;
    private String nickname;
    private ServerParser serverParser;
    ScheduledExecutorService scheduledExecutorService = null;
    private String gameID;
    private int stateNumber;


    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.sendToAll = new Queuejd();
        this.sendToPub = new Queuejd();
        this.sendToThisClient = new Queuejd();
        this.receivedFromClient = new Queuejd();
        this.running = true;
        this.loggedIn = false;
        this.monitorCS = new Monitorcs();
        this.nickname = null;
    }


    public void createConnection() {
        System.out.println("serverConnection");

        // sender thread
        sender = new SendFromServer(socket, server, sendToAll, sendToThisClient,
                sendToPub,this);
        Thread senderThread = new Thread(sender);
        senderThread.start();


        // detect connection problems thread
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ConnectionToClientMonitor
                        (this, sendToThisClient, monitorCS), 5000,5000, TimeUnit.MILLISECONDS);




        // receiveFromClient thread
        listeningToClient = new ReceiveFromClient(socket, sendToThisClient,receivedFromClient,
                this);
        Thread listener = new Thread(listeningToClient);
        listener.start();

        // messageHandlerServer Thread
        messageHandlerServer = new MessageHandlerServer(server,
                this, sendToThisClient, sendToAll, receivedFromClient, sendToPub);
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
        if (gameID != null) {
            if (stateNumber == 0) {
                server.errorRemoveOpenGame(gameID, nickname);
            } else {
                server.errorRemoveMainGame(gameID, nickname);
            }
            gameID = null;
        }

        this.listeningToClient.kill();
        this.sender.kill();
        this.scheduledExecutorService.shutdown();
        this.messageHandlerServer.kill();


        System.out.println("stop ServerConnection..." + nickname);

        server.removeServerConnection(this);
    }

    public void updateNickname(String nickName) {
        loggedIn = true;
        this.nickname = nickName;

    }

    public String getDefaultName() {
        loggedIn = true;
        nickname = socket.getLocalAddress().getHostName();
        return nickname;
    }

    public String getNickname() {
        return nickname;
    }


    public MessageHandlerServer getMessageHandlerServer() {
        return messageHandlerServer;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setState(int number) {
        stateNumber = number;
    }
}

