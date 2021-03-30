package JDogs.ServerClientEnvironment.ClientSide;

/**
 * ClientGameCommand contains the game
 * commands which are sent from the
 * server to communicate with the
 * client.
 *
 */

public class ClientGameCommand {

    ClientGameCommand() {

    }

    public void execute(String text) {

        String command = text.substring(0,4);

        switch(command) {




        }

    }

}
