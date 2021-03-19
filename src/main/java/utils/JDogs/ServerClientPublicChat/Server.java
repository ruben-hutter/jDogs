import java.io.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {

    ServerSocket server;
    //contains all PrintWriter-Objects of all clients
    ArrayList<PrintWriter> list_clientWriter;

    public static void main(String[] args) {

         Server s = new Server();
            if (s.runServer()) {

                s.listenToClients();
            } else {
                // Do nothing
                System.err.println("Server did not start..");
            }

        }
    public class ReachClient implements Runnable {
        Socket client;

        public ReachClient(Socket client) {
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

        public class ClientHandler implements Runnable {

            Socket client;
            BufferedReader reader;

            public ClientHandler(Socket client) {

                try {
                    this.client = client;

                    reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
             public void run() {
                String message;
                InetAddress inetAddress = client.getInetAddress();

                try {
                    while((message = reader.readLine()) != null) {
                        if (message.isEmpty() || message.isBlank()) {
                            //do nothing
                        } else {
                            System.out.println("From client: \n" + message);
                            sendToAllClients(message);
                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        //this method starts threads for new incoming clients
        public void listenToClients() {

            while (true) {

                try {
                    Socket client = server.accept();
                    //adds their writer to list
                    PrintWriter writer = new PrintWriter(client.getOutputStream());
                    list_clientWriter.add(writer);

                    Thread clientThread = new Thread(new ClientHandler(client));
                    clientThread.start();

                    Thread reachClient = new Thread(new ReachClient(client));
                    reachClient.start();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    //start Server with this method
    public boolean runServer() {

        try {
            server = new ServerSocket(8090);
            System.out.println("server did start");
            list_clientWriter = new ArrayList<PrintWriter>();
            return true;

        } catch (IOException e) {
            System.out.println("Server did not start");
            e.printStackTrace();
            return false;
        }
    }

    //send messages to all clients
    public void sendToAllClients(String message) {
        //represents a kind of list with all writers of the clients
        Iterator it = list_clientWriter.iterator();

        while (it.hasNext()) {
            PrintWriter writer = (PrintWriter) it.next();
            writer.println(message);
            writer.flush();

        }
    }



}
