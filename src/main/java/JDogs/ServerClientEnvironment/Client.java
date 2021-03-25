package JDogs.ServerClientEnvironment;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

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
        //sets up connection to Server
        setUp();
    }

    public void setUp() {
        //queues save messages,
        // received ones
        // and those who will be sent
        Queue receiveQueue = new Queue();
        Queue sendQueue = new Queue();

        Socket socket = null;

        String serveraddress;
        int portnumber;
        //enter IP-Address of server
        System.out.println("IP-Adresse des Servers:");
        Scanner scanner = new Scanner(System.in);
        serveraddress = scanner.nextLine();
        //enter Port
        System.out.println("Portnummer:");
        portnumber = scanner.nextInt();


        //connect to server
        try {
            socket = new Socket(serveraddress, portnumber);
        } catch (IOException e) {
            System.err.println("no server found...implement error handling here");
            e.printStackTrace();
        }
        /***
         * 5 threads to handle connection are started here
         */

        //1.monitor connection to server thread
        connectionToServerMonitor = new ConnectionToServerMonitor(this, sendQueue);
        Thread monitorThread = new Thread(connectionToServerMonitor);
        monitorThread.start();

        //2.receive messages from server thread
        receiveFromServer = new ReceiveFromServer(socket,this, receiveQueue,
                connectionToServerMonitor);
        Thread receiverThread = new Thread(receiveFromServer);
        receiverThread.start();

        //3.send messages to server thread
        sendFromClient = new SendFromClient(socket,this, sendQueue);
        Thread toServerThread = new Thread(sendFromClient);
        toServerThread.start();

        //4.process keyboard input thread
        keyboardInput = new KeyboardInput(this, sendQueue);
        Thread keyboard = new Thread(keyboardInput);
        keyboard.start();

        //5.handle received messages meaningfully
        messageHandlerClient = new MessageHandlerClient(receiveQueue, sendQueue);
        Thread messageHandleThread = new Thread(messageHandlerClient);
        messageHandleThread.start();
    }

    synchronized public void newSetUp() {
        //kills all threads..reconnection could be started from here later
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
