package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.io.IOException;
import java.net.Socket;

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
        /*Queue receiveQueue = new Queue();
        Queue sendQueue = new Queue();

         */
        setUp();
    }

    public void setUp() {

        Queue receiveQueue = new Queue();
        Queue sendQueue = new Queue();

        Socket socket = null;

        try {
            socket = new Socket("localhost", 8090);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //monitoring thread

        connectionToServerMonitor = new ConnectionToServerMonitor(this, sendQueue);
        Thread monitorThread = new Thread(connectionToServerMonitor);
        monitorThread.start();

        //receives messages from server
        assert socket != null;
        receiveFromServer = new ReceiveFromServer(socket,this, sendQueue, receiveQueue, connectionToServerMonitor);
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
        System.out.println("trying to reset connection to server..");

        setUp();
    }
}
