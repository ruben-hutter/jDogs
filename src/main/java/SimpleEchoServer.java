import java.io.*;
import java.net.*;

public class SimpleEchoServer {

    public static void main(String[] args) {
        try {
            System.out.println("Waiting to connect Port 8090...");
            ServerSocket echod = new ServerSocket(8090);
            Socket socket = echod.accept();
            System.out.println("Connected!");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            int c;
            while ((c = in.read()) != -1) {
                out.write((char) c);
                System.out.print((char) c);
            }
            System.out.println("Disconnected!");
            socket.close();
            echod.close();
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }
}
