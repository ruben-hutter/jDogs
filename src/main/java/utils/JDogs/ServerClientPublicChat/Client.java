import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    String userName;
    Scanner fromKeyboard;

    Socket client;
    PrintWriter writer;
    BufferedReader reader;

    public static void main(String[] args) {
        Client c = new Client();
        c.getName();
        //c.createGUI();
        c.createChat();
    }

    //public void createGUI() {}

    public void getName() {
        boolean noName = true;

        System.out.println("type in your user_name");
        fromKeyboard = new Scanner(System.in);

        while (noName) {

            if ((userName = fromKeyboard.nextLine()) != null) {
                if (!userName.isEmpty() && !userName.isBlank()) {
                    noName = false;
                }
            }
        }

    }

    public void createChat() {
        //type in user_name;


        System.out.println("try to connect to server...");

        if (!connectToServer()) {
            //show if connected or not
            System.out.println("connection to server failed");
        } else {
            System.out.println("connected to server! Welcome " + userName + "!");
        }
        //listens if messages arrive from server
        Thread t = new Thread(new MessagesFromServerListener());
        t.start();

        fromKeyboard = new Scanner(System.in);

        String line;

        while (true) {
            //write from keyboard and give it to server



            if ((line = fromKeyboard.nextLine()) != null) {
                if (!line.isEmpty() && !line.isBlank()) {
                sendMessageToServer(line);
                }
            }
        }
    }

    public boolean connectToServer() {

        try {
            client = new Socket("localhost", 8090);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream());



            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;

        }
    }

    public void sendMessageToServer(String line) {
        writer.println(userName + " : " + line + "\n");
        writer.flush();
    }

    public class ReachServer implements Runnable {
        Socket client;

        public ReachServer(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            InetAddress inetAddress = client.getInetAddress();
            while (true) {
                try {
                    Thread.sleep(5000);
                    if (inetAddress.isReachable(5000)) {
                        System.out.println("is reachable  " + inetAddress.getHostAddress() + " " + inetAddress.getHostName());
                    } else {
                        System.err.println("not reachable  " + inetAddress.getHostAddress() + " " + inetAddress.getHostName());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class MessagesFromServerListener implements Runnable {
        @Override
         public void run() {

            Thread reachServer = new Thread(new ReachServer(client));
            reachServer.start();

            String message;


            try {

                while ((message = reader.readLine()) != null) {


                    if (!message.isEmpty()) {
                        System.out.println("from server:  " + message);
                    }


                }

            } catch (IOException e) {
                //means, connection is lost

                System.out.println("couldn t receive message");

                //try again to reach Server if no message could be received
            }
        }
    }







}
