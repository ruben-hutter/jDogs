package JDogs.ServerClient;

import java.io.IOException;
import java.io.InputStream;

public class InThread implements Runnable {

    InputStream in;
    public InThread(InputStream in) {
        this.in = in;
    }
    public void run() {
        int len;
        byte[] b = new byte[100];
        try {
            while (true) {
                if ((len = in.read(b)) == -1) {
                    break;
                }
                System.out.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
