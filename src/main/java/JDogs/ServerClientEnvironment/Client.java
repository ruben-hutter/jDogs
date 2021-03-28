package JDogs.ServerClientEnvironment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * starts threads for sending/receiving to/from server
 * instantiates receiver & sender Queue
 * kill client from here
 */
public class Client {

    private ReceiveFromServer receiveFromServer;
    private KeyboardInput keyboardInput;
    private ConnectionToServerMonitor connectionToServerMonitor;
    private SendFromClient sendFromClient;
    private MessageHandlerClient messageHandlerClient;
    private String nickname;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        setUp();
    }

    /**
     * Sets up connection to Server
     * Messages are saved in specific queues
     * 5 threads to handle connection are started
     */
    public void setUp() {
        Queue receiveQueue = new Queue();
        Queue sendQueue = new Queue();
        Queue keyBoardInQueue = new Queue();

        Socket socket = null;

        String serveraddress = "25.74.38.24";
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

        // 1.monitor connection to server thread
        connectionToServerMonitor = new ConnectionToServerMonitor(this, sendQueue);
        Thread monitorThread = new Thread(connectionToServerMonitor);
        monitorThread.start();

        // 2.receive messages from server thread
        receiveFromServer = new ReceiveFromServer(socket,this, receiveQueue,
                connectionToServerMonitor);
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

    /**
     * Kills all threads.. reconnection could
     * be started from here later
     */
    synchronized public void kill() {
        receiveFromServer.kill();
        sendFromClient.kill();
        connectionToServerMonitor.kill();
        keyboardInput.kill();
        messageHandlerClient.kill();
        System.out.println("trying to reset connection to server is not activated. Shutdown Client..");
        System.exit(-1);
        //setUp();
    }
}
