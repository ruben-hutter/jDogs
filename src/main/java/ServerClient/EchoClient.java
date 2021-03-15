import java.net.*;
import java.io.*;

public class EchoClient {
    
    public static void main(String[] args) {
        try {
            String host = "localhost";
            int port = 8090;

            //check if server is available
            while (!isOnline(host, port)) {
                System.out.println("wait for connection to server...");
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Socket sock = new Socket(host, port);
            InputStream in = sock.getInputStream();
            OutputStream out= sock.getOutputStream();
            // create server reading thread
            InThread th = new InThread(in);
            Thread iT = new Thread(th); iT.start();
            // stream input
            BufferedReader conin = new BufferedReader(new InputStreamReader(System.in));
            String line = " ";
            while (true) {
                // reading input stream
                line = conin.readLine();
                if (line.equalsIgnoreCase("QUIT")) {
                    break;
                }
                // writing to ECHO server
                out.write(line.getBytes());
            } // terminate program
            System.out.println("terminating ..");
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean isOnline(String host, int port) {
        boolean online = true;
        try {
            InetSocketAddress sa = new InetSocketAddress(host, port);
            Socket ss = new Socket();
            ss.connect(sa, 1);
            ss.close();
        } catch (IOException e) {
            online = false;
        }
        return online;
    }
}
