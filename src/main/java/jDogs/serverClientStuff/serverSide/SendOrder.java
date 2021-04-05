package jDogs.serverClientStuff.serverSide;

public class SendOrder implements Runnable{
    Server server;
    String destiny;
    String origin;
    String message;

    SendOrder(Server server, String destiny, String origin, String message) {
        this.server = server;
        this.destiny = destiny;
        this.origin = origin;
        this.message = message;
    }

    @Override
    public void run() {
       SendFromServer sender = server.getSenderForWhisper(destiny);
       sender.sendStringToClient("WCHT " + origin + ";" + message);
    }
}
