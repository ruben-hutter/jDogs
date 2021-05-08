package jDogs.serverclient.serverside;

import jDogs.serverclient.helpers.Monitorcs;
import jDogs.serverclient.helpers.Queuejd;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This thread is the main thread of the connection to the client
 * here are lie the three Queues(sendtoClient, sendAll, receivedFromClient)
 * used by the threads.
 * the threads are started here
 * the connection to the client can be ended here by other threads by delete()

 * stopNumber is the number of the sender-object saved
 * in the ArrayList of server.senderlist.
 * this enables to delete this sender-object from the list
 * to prevent errors in the other sender threads after disconnection of this client
 */
public class ServerConnection {

    private final Server server;
    private final Socket socket;
    private final BlockingQueue<String> sendAll;
    private final BlockingQueue<String> sendThisClient;
    private final BlockingQueue<String> receivedFromClient;
    private final BlockingQueue<String> sendPub;
    private ReceiveFromClient listeningToClient;
    private MessageHandlerServer messageHandlerServer;
    private Monitorcs monitorCS;
    private String nickname;
    ScheduledExecutorService scheduledExecutorService = null;
    private String gameID;
    private int stateNumber;
    private SenderContainer senderContainer;

    /**
     * constructor of an object of ServerConnection
     * @param socket socket to client
     * @param server server instance
     */
    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.sendAll = new ArrayBlockingQueue<>(10);
        this.sendPub = new ArrayBlockingQueue<>(10);
        this.sendThisClient = new ArrayBlockingQueue<>(10);
        this.receivedFromClient = new ArrayBlockingQueue<>(10);
        this.monitorCS = new Monitorcs();
        this.nickname = null;
    }

    /**
     * creates all needed threads
     */
    public void createConnection() {
        // create all three threads to send messages
        senderContainer = new SenderContainer(this, socket, sendAll, sendPub,
               sendThisClient);

        // detect connection problems thread
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ConnectionToClientMonitor
                        (this, monitorCS), 5000,5000, TimeUnit.MILLISECONDS);

        // receiveFromClient thread
        listeningToClient = new ReceiveFromClient(socket, receivedFromClient,
                this);
        Thread listener = new Thread(listeningToClient);
        listener.start();

        // messageHandlerServer Thread
        messageHandlerServer = new MessageHandlerServer(server,
                this, sendThisClient, sendAll, receivedFromClient, sendPub);
        Thread messenger = new Thread(messageHandlerServer);
        messenger.start();
    }

    /**
     * send message to this client
     * @param message
     */
    public void sendToClient(String message) {
        try {
            sendThisClient.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * send message to all clients that are connected to server
     * whether they are playing, in separate or public lobby
     * @param message
     */
    public void sendToAll(String message) {
        try {
            sendAll.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * send message to public lobby guests
     * @param message
     */
    public void sendToPub(String message) {
        try {
            sendPub.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * send time of last received ping to monitorCS
     * @param time
     */
    public void monitorMsg(long time) {
        this.monitorCS.receivedMsg(time);
    }

    /**
     * remove the serverConnection from server
     * (and if playing or being host of an open game) the game
     * and kill all threads affiliated
     */
    synchronized public void kill() {
        if (gameID != null) {
            if (stateNumber == 0) {
                server.errorRemoveOpenGame(gameID, nickname);
            } else {
                server.errorRemoveMainGame(gameID, nickname);
            }
            gameID = null;
        }
        this.senderContainer.kill();
        this.listeningToClient.kill();
        this.scheduledExecutorService.shutdown();
        this.messageHandlerServer.kill();
        System.out.println("stop ServerConnection..." + nickname);
        server.removeServerConnection(this);
    }

    /**
     * update(change) the nickname
     * @param nickName
     */
    public void updateNickname(String nickName) {
        this.nickname = nickName;
    }

    /**
     * get system name of local computer host
     * @return computer host name
     */
    public String getDefaultName() {
        nickname = socket.getLocalAddress().getHostName();
        return nickname;
    }

    /**
     * get the actual nickname of the client
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * get this object of the messageHandlerServer
     * @return messageHandlerServer of this ServerConnection
     */
    public MessageHandlerServer getMessageHandlerServer() {
        return messageHandlerServer;
    }

    /**
     * save the name of the game the user joined
     * @param gameID
     */
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    /**
     * set the number to 0 if player is in public lobby
     * set the number to 1 if player is in separate lobby or in game
     * @param number 0,1
     */
    public void setState(int number) {
        stateNumber = number;
    }
}

