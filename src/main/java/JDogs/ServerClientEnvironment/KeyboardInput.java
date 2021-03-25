package JDogs.ServerClientEnvironment;

import java.util.Scanner;

/***
 * receives any input from client
 * blank/empty lines are ignored
 * "quit" activates client.kill()
 * every other message is sent to
 * sendQueue(to be sent to server)
 *
 */

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
           if (!console.hasNextLine()) {

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
               if(console.hasNextLine()) {
                   input = console.nextLine();


                   if (input.isEmpty() || input.isBlank()) {
                       // do nothing if input is an empty/blank string


                   } else {
                       if (input.equalsIgnoreCase("quit")) {
                           client.kill();
                       }

                       System.out.println("from keyboard:  " + input);
                       sendQueue.enqueue(input);
                   }

               }
           }
        }
    }

    public void kill() {
        running = false;
    }
}
