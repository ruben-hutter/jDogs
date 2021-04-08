package jDogs.serverclient.clientside;

/**
 * ClientGameCommand contains the game
 * commands which are sent from the
 * server to communicate with the
 * client.
 *
 */
//

public class ClientGameCommand {

    ClientGameCommand() {

    }

    public void execute(String text) {

        String command = text.substring(0,4);

        switch(command) {

            case "TURN":
                if (text.length() == command.length()) {
                    System.out.println("TURN: your turn");
                } else {
                    System.out.println("it is " + text.substring(5) + "`s turn");
                }
                break;

            case "ROUN":
                //TODO received hand of cards display in Game GUI
                System.out.println("ROUN: " + text.substring(5));
                break;

            default:
                System.out.println("the command " + command + " is not implemented. Can`t execute " + text);

        }

    }

}
