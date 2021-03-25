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

        setUp();
    }

    public void setUp() {

        Queue receiveQueue = new Queue();
        Queue sendQueue = new Queue();

        Socket socket = null;

        String serveraddress;
        int portnumber;

        System.out.println("IP-Adresse des Servers:");
        Scanner scanner = new Scanner(System.in);
        serveraddress = scanner.nextLine();
        System.out.println("Portnummer:");
        portnumber = scanner.nextInt();



        try {
            socket = new Socket(serveraddress, portnumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //monitoring thread
        connectionToServerMonitor = new ConnectionToServerMonitor(this, sendQueue);
        Thread monitorThread = new Thread(connectionToServerMonitor);
        monitorThread.start();

        //receives messages from server
        //assert socket != null;
        receiveFromServer = new ReceiveFromServer(socket,this, receiveQueue,
                connectionToServerMonitor);
        Thread receiverThread = new Thread(receiveFromServer);
        receiverThread.start();

        //sends messages to server
        sendFromClient = new SendFromClient(socket,this, sendQueue);
        Thread toServerThread = new Thread(sendFromClient);
        toServerThread.start();

        //processes keyboard input
        keyboardInput = new KeyboardInput(this, sendQueue);
        Thread keyboard = new Thread(keyboardInput);
        keyboard.start();

        //handles received messages meaningfully
        messageHandlerClient = new MessageHandlerClient(receiveQueue, sendQueue);
        Thread messageHandleThread = new Thread(messageHandlerClient);
        messageHandleThread.start();
    }

    synchronized public void newSetUp() {

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
