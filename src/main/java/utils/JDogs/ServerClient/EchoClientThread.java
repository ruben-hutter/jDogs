package utils.JDogs.ServerClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClientThread implements Runnable {

    private int name;
    private Socket socket;

    public EchoClientThread(int name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }
    public void run() {
        String msg = "EchoServer: Connection " + name;
        System.out.println(msg + " connected");
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(("cs108: " + msg + "\r\n").getBytes());
            int c;
            while ((c = in.read()) != -1) {
                out.write((char) c);
                System.out.println((char) c);
            }
            System.out.println("Terminate " + name);
            socket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
