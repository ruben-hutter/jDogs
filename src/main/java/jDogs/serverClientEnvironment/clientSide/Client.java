package jDogs.serverClientEnvironment.clientSide;

import jDogs.gui.javafx.ChatGui;
import jDogs.serverClientEnvironment.MonitorCS;
import jDogs.serverClientEnvironment.QueueJD;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * starts threads for sending/receiving to/from server
 * instantiates receiver & sender Queue
 * kill client from here
 */
public class Client {

    private ReceiveFromServer receiveFromServer;
    private KeyboardInput keyboardInput;
    private SendFromClient sendFromClient;
    private MessageHandlerClient messageHandlerClient;
    private MonitorCS monitorCS;
    private String nickname;
    private QueueJD receiveQueue;
    private QueueJD sendQueue;
    private QueueJD keyBoardInQueue;
    private ChatGui chatGui;

    public Client(ChatGui chatGui) {
        this.chatGui = chatGui;
        setUp();
    }

    /**
     * Sets up connection to Server
     * Messages are saved in specific queues
     * 5 threads to handle connection are started
     */
    public void setUp() {
        this.receiveQueue = new QueueJD();
        this.sendQueue = new QueueJD();
        this.keyBoardInQueue = new QueueJD();

        Socket socket = null;

        String serveraddress = "localhost";
        int portnumber = 8090;

        /*System.out.println("IP-Adresse des Servers:");
        Scanner scanner = new Scanner(System.in);
        serveraddress = scanner.nextLine();
        scanner = new Scanner(System.in);
        System.out.println("port number:");
        portnumber = scanner.nextInt();
         */

        // connect to server
        try {
            socket = new Socket(serveraddress, portnumber);
        } catch (IOException e) {
            System.err.println("no server found...implement error handling here..shutdown now");
            //e.printStackTrace();
        }

        monitorCS = new MonitorCS();

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
        messageHandlerClient = new MessageHandlerClient(this,chatGui, sendFromClient,receiveQueue, sendQueue, keyBoardInQueue);
        Thread messageHandleThread = new Thread(messageHandlerClient);
        messageHandleThread.start();
    }
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
        //setUp();
    }
}
