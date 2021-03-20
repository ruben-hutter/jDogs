import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection implements Runnable {
    boolean shouldRun = true;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
        //https://www.youtube.com/watch?v=0YvPY9gXUXg

    public ClientConnection(Socket socket, Client client) {
        this.socket = socket;
        try {
            this.din = new DataInputStream(socket.getInputStream());
            this.dout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendStringToServer(String text) {

        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void run() {

                try {
                    while (shouldRun) {
                        //server keeps silent
                        while (din.available() == 0) {
                            sendStringToServer("\n");
                            Thread.sleep(100);
                        }

                        String reply = din.readUTF();

                        if (reply.isEmpty() || reply.isBlank()) {
                            //System.out.println("Still there?");
                            sendStringToServer("\n");
                        } else {
                            System.out.println("from server:  " + reply);
                        }
                    }
                } catch(IOException e){
                e.printStackTrace();
                close();
                } catch(InterruptedException e){
                e.printStackTrace();
                }
            }


    public void close() {

        try {
            din.close();
            dout.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
