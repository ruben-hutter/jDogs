package JDogs.ServerClientEnvironment;

public class GameCommand {

    public GameCommand() {

        //or static?

    }

    public void execute(String text) {
        String command = text.substring(0,4);

        switch(command) {

            case "EXIT":
                // TODO startExit();
                //finish game
                break;
            case "MOVE":
                // TODO startMove()
                // Server: give it to GameEngine if move is according to the rules

                break;

            case "CTTP":
                // TODO start CTTP
                //change cards
                break;
        }

    }

}
