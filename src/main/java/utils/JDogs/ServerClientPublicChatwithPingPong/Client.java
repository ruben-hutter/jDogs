package utils.JDogs.ServerClientPublicChatwithPingPong;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class Client {

    ClientConnection clientConnection;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {

        try {
            Socket socket = new Socket("localhost", 8090);
            clientConnection = new ClientConnection(socket, this);
            Thread ccThread = new Thread(clientConnection);
            ccThread.start();


            listenforInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenforInput() {
        Scanner console = new Scanner(System.in);

        while (true) {
            while(!console.hasNextLine()) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            String input = console.nextLine();

            if (input.toLowerCase().equalsIgnoreCase("quit")) {
                break;
            }
            clientConnection.sendStringToServer(input);
        }

        clientConnection.close();

    }
}
