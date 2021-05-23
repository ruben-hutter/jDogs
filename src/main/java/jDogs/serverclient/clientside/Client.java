package jDogs.serverclient.clientside;

import jDogs.Main;
import jDogs.serverclient.helpers.Monitorcs;
import jDogs.serverclient.helpers.Queuejd;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * starts threads for sending/receiving to/from server
 * instantiates receiver and sender Queue
 * delete client from here
 */
public class Client {

    private ReceiveFromServer receiveFromServer;
    private KeyboardInput keyboardInput;
    private SendFromClient sendFromClient;
    private MessageHandlerClient messageHandlerClient;
    private Monitorcs monitorCS;
    private String nickname;
    private Queuejd receiveQueue;
    private Queuejd sendQueue;
    private Queuejd keyBoardInQueue;
    private static Client instance;
    private String serveraddress;
    private String username;
    private int portnumber;
    private ArrayList<String> pubUserList = new ArrayList<>();

    /**
     * Constructor of a client
     */
    public Client() {
        instance = this;
        setUp();
    }

    /**
     * returns the singleton of this class
     * @return singleton
     */
    public static Client getInstance() {
        return instance;
    }


      /**
     * Sets up connection to Server
     * Messages are saved in specific queues
     * 5 threads to handle connection are started
     */
    public void setUp() {
        this.serveraddress = Main.getInstance().getHostAddress();
        this.portnumber = Main.getInstance().getPort();
        this.receiveQueue = new Queuejd();
        this.sendQueue = new Queuejd();
        this.keyBoardInQueue = new Queuejd();
        this.nickname = Main.getInstance().getUsername();
        Socket socket = null;


        // connect to server
        try {
            socket = new Socket(serveraddress, portnumber);
        } catch (IOException e) {
            System.err.println("no server found...implement error handling here..shutdown now");
            //e.printStackTrace();
        }

        monitorCS = new Monitorcs();

        // 1.monitor connection to server thread starts every 5 seconds
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ConnectionToServerMonitor(this, sendQueue, monitorCS), 5000, 5000, TimeUnit.MILLISECONDS);

        // 2.receive messages from server thread
        receiveFromServer = new ReceiveFromServer(socket,this, receiveQueue);
        Thread receiverThread = new Thread(receiveFromServer);
        receiverThread.start();

        // 3.send messages to server thread
        sendFromClient = new SendFromClient(socket,this, sendQueue, keyBoardInQueue);
        Thread toServerThread = new Thread(sendFromClient);
        toServerThread.start();

        // 4.process keyboard input thread
        keyboardInput = new KeyboardInput(this, keyBoardInQueue);
        Thread keyboard = new Thread(keyboardInput);
        keyboard.start();

        // 5.handle received messages meaningfully
        messageHandlerClient = new MessageHandlerClient(this, sendFromClient,receiveQueue, sendQueue, keyBoardInQueue);
        Thread messageHandleThread = new Thread(messageHandlerClient);
        messageHandleThread.start();
    }

    /**
     * sends a message to server by enqueuing
     * @param message the sent message
     */
    public void sendMessageToServer(String message) {
        keyBoardInQueue.enqueue(message);
    }

    /**
     * Returns the hostname of the client
     * @return hostname
     */
    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
    //transmit received ping message to monitor object

    /**
     * sends the time of received ping message to monitor object
     * @param time the last time received from server
     */
    public void monitorMsg(long time) {
        this.monitorCS.receivedMsg(time);
    }

    /**
     * Kills all threads.. reconnection could
     * be started from here later
     */
    synchronized public void kill() {
        receiveFromServer.kill();
        sendFromClient.kill();
        keyboardInput.kill();
        messageHandlerClient.kill();
        System.out.println("trying to reset connection to server is not activated. Shutdown Client..");
        System.exit(-1);
    }

    /**
     * sets the nickname of this client
     * (from gui)
     * @param nickname the name of the user
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * get actual nickname
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * add a user to public lobby list
     * @param user name
     */
    public void removeFromPubList(String user) {
        pubUserList.remove(user);
    }

    /**
     * remove a user from public lobby list
     * @param user name
     */
    public void addToPubList(String user) {
        pubUserList.add(user);
    }

    /**
     * get PubUserList
     * @return list with user in public lobby
     */
    public ArrayList<String> getPubUserList() {
        return pubUserList;
    }
}
