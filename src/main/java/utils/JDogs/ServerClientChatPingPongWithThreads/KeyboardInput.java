package utils.JDogs.ServerClientChatPingPongWithThreads;

import java.util.Scanner;

public class KeyboardInput implements Runnable {

    private final Queue sendQueue;
    private final Scanner console;
    private boolean running;
    private final Client client;

    public KeyboardInput(Client client, Queue sendQueue) {
        this.sendQueue = sendQueue;
        this.console = new Scanner(System.in);
        this.client = client;
        this.running = true;
    }

    @Override
    public void run() {
        String input;
        while (running) {
            while(!console.hasNextLine()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(console.hasNextLine()) {
                input = console.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    running = false;
                    client.killClient();
                }
                System.out.println("from keyboard:  " + input);
                sendQueue.enqueue(input);
            }
        }
    }
}
