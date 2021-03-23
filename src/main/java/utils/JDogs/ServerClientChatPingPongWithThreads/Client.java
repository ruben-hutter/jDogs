package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private ReceiveFromServer receiveFromServer;
    private KeyboardInput keyboardInput;
    private ConnectionToServerMonitor connectionToServerMonitor;
    private SendFromClient sendFromClient;
    private String nickname;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {

        Queue sendQueue = new Queue();

        Socket socket = null;
        try {
            socket = new Socket("localhost", 8090);
        } catch (IOException e) {
            e.printStackTrace();
        }

        suggestNickname();

        //monitoring thread

        connectionToServerMonitor = new ConnectionToServerMonitor(sendQueue);
        Thread monitorThread = new Thread(connectionToServerMonitor);
        monitorThread.start();

        //receives messages from server
        assert socket != null;
        receiveFromServer = new ReceiveFromServer(socket, sendQueue, connectionToServerMonitor);
        Thread receiverThread = new Thread(receiveFromServer);
        receiverThread.start();


        //sends messages to server
        sendFromClient = new SendFromClient(socket, sendQueue);
        Thread toServerThread = new Thread(sendFromClient);
        toServerThread.start();

        //processes keyboard input
        keyboardInput = new KeyboardInput(this, sendQueue);
        Thread keyboard = new Thread(keyboardInput);
        keyboard.start();


    }

    public void suggestNickname() {
        try {
            nickname = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public String getNickname() {
        return nickname;
    }

    public void killClient() {

        receiveFromServer.kill();
        sendFromClient.kill();
        connectionToServerMonitor.kill();
        keyboardInput.kill();


    }
}
