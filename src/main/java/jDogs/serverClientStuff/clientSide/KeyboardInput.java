package jDogs.serverClientStuff.clientSide;

import jDogs.serverClientStuff.helpers.QueueJD;
import java.util.Scanner;

/**
 * Receives any input from client
 * blank/empty lines are ignored
 * "quit" activates client.kill()
 * every other message is sent to
 * sendQueue(to be sent to server)
 */
public class KeyboardInput implements Runnable {

    private final QueueJD keyBoardInput;
    private final Scanner console;
    private boolean running;
    private final Client client;

    public KeyboardInput(Client client, QueueJD keyBoardInput) {
        this.keyBoardInput = keyBoardInput;
        this.console = new Scanner(System.in);
        this.client = client;
        this.running = true;
    }

    @Override
    public void run() {
        String input;
        while (running) {
            if (!console.hasNextLine()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if(console.hasNextLine()) {
                   input = console.nextLine();
                   keyBoardInput.enqueue(input);
                   System.out.println("from keyboard:  " + input);
                }
            }
        }
    }

    /**
     * Kills thread
     */
    public void kill() {
        running = false;
    }
}
