package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Queue sendQueue;

    private ReceiveFromServer receiveFromServer;
    private SendFromClient sendFromClient;
    private KeyboardInput keyboardInput;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {

        this.sendQueue = new Queue();

        Socket socket = null;
        try {
            socket = new Socket("localhost", 8090);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //receives messages from server
        this.receiveFromServer = new ReceiveFromServer(socket, sendQueue);
        Thread receiverThread = new Thread(receiveFromServer);
        receiverThread.start();


        //sends messages to server
        this.sendFromClient = new SendFromClient(socket,sendQueue);
        Thread toServerThread = new Thread(sendFromClient);
        toServerThread.start();

        //processes keyboard input
        this.keyboardInput = new KeyboardInput(this, sendQueue);
        Thread keyboard = new Thread(keyboardInput);
        keyboard.start();


    }

    public void killClient() {

        System.exit(-1);

    }
}
