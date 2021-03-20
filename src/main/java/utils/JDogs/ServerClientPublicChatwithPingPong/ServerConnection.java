import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ServerConnection implements Runnable {
    Server server;
    Socket socket;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true;

    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void sendStringToClient(String text) {
        try {
            dout.writeUTF(text + "\n");
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ServerConnection error 1: send String to Client error....");
            //here: error handling needed, when Server can t be reached, program gets stuck here
            System.exit(-1);
        }

    }


    public void sendStringToAllClients(String text) {
        for (int index = 0; index < server.connections.size(); index++) {
            ServerConnection sc = server.connections.get(index);
            sc.sendStringToClient(text);
        }
    }

    @Override
    public void run() {
        int sentPongs = 0;

        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            while (shouldRun) {


                while (din.available() == 0) {
                    //send a signal to this client, if he doesn`t answer
                    // in a certain amount of time => problem handling
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("ServerConnection error 2");
                    }
                    sendStringToClient("\n");

                    if (sentPongs > 1000) {
                       System.out.println("problem handling is needed..bad connection. SentPongs:  " + sentPongs);
                       System.out.println("client-address: " + socket.getInetAddress().getHostAddress());
                        //problem handling here
                    }
                    sentPongs++;
                }
                sentPongs = 0;

                String textIn = din.readUTF();
                if (textIn.isBlank() || textIn.isEmpty() ) {
                    //System.out.println("still there?");
                    sendStringToClient("\n");

                } else {
                    System.out.println("fromClient  " + textIn);
                    sendStringToAllClients(textIn);
                }
            }
            din.close();
            dout.close();
            socket.close();

        } catch (IOException ioe) {
            System.out.println("ServerConnection error 3");
            //ioe.printStackTrace();
        }
    }


}




