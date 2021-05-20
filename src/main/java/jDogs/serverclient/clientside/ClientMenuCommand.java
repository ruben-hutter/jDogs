package jDogs.serverclient.clientside;


import jDogs.gui.GUIManager;
import jDogs.gui.GuiParser;
import jDogs.serverclient.helpers.Queuejd;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ClientMenuCommand contains the menu/lobby
 * commands which are sent from the server to
 * communicate with the client.
 *
 */

public class ClientMenuCommand {

    private Client client;
    private SendFromClient sendFromClient;
    private Queuejd sendQueue;
    private Queuejd keyBoardInQueue;
    private static final Logger logger = LogManager.getLogger(ClientMenuCommand.class);

    /**
     * Constructor of ClientMenuCommand
     * @param client instance of client
     * @param sendFromClient thread to send to server
     * @param sendQueue QueueJd object to store messages to send
     * @param keyBoardInQueue Queuejd object to store messages from user to send
     */
    ClientMenuCommand(Client client,SendFromClient sendFromClient, Queuejd sendQueue, Queuejd keyBoardInQueue) {
        this.client = client;
        this.sendQueue = sendQueue;
        this.sendFromClient = sendFromClient;
        this.keyBoardInQueue = keyBoardInQueue;
    }

    /**
     * executes commands that fitted the formal criteria of ReceivedFromServer
     * @param text command and information
     */
    public void execute (String text) {
        logger.debug("Entered ClientMenuCommand with: " + text);
        ClientMenuProtocol command = ClientMenuProtocol.toCommand(text.substring(0, 4));
        try {
            switch (command) {
                case USER:
                    String name;
                    //case 1: Server wants a nickname, it sends "USER" only
                    if (text.length() == 4) {
                        name = client.getNickname();
                        sendQueue.enqueue("USER " + name);
                    } else {
                        //case 2: Server confirms nickname, it sends "USER " + new nick
                        name = text.substring(5);
                        client.setNickname(name);
                        sendFromClient.keyBoardInBlocked = false;

                        Platform.runLater(() -> GUIManager.getInstance().getPubLobbyController().displayNickname(name));
                        System.out.println("your new nick is " + name);
                    }
                    break;

                case PCHT:
                    System.out.println("PCHT: " + text.substring(4));
                    Platform.runLater(() ->
                            GUIManager.getInstance().sendPCHTToGui(text.substring(5)));
                    break;

                case WCHT:
                    System.out.println("WCHT: " + text.substring(5));

                    Platform.runLater(() ->
                            GUIManager.getInstance().sendWCHTtoGui(text.substring(5)));
                    break;

                case LPUB:
                    //just compare to existing String/Array and replace if necessary
                    Platform.runLater(() ->
                            GUIManager.getInstance().getPubLobbyController().addUser(text.substring(5)));

                    System.out.println("LPUB: " + text.substring(5));
                    break;

                case LCHT:
                    Platform.runLater(() ->
                            GUIManager.getInstance().sendLCHTToGui(text.substring(5)));

                    System.out.println("LCHT " + text.substring(5));
                    break;

                case DPER:
                    Platform.runLater(() ->
                            GUIManager.getInstance().getPubLobbyController().removeUser(text.substring(5)));
                    break;

                case JOIN:
                    System.out.println("JOIN " + text);
                    Platform.runLater(() ->
                            GUIManager.getInstance().goToSeparateLobby(text.substring(5)));
                    Client.getInstance().sendMessageToServer("LPUB");
                    break;

                case OGAM:
                    System.out.println("OGAM: " + text.substring(5));

                    Platform.runLater(() ->
                            GUIManager.getInstance().getPubLobbyController().addOpenGame(text.substring(5)));
                    break;

                case DOGA:
                    //TODO remove openGame from GUI-Lobby-Display
                    System.out.println("DOGA: " + text.substring(5));

                    Platform.runLater(() ->
                            GUIManager.getInstance().getPubLobbyController().removeOpenGame(text.substring(5)));
                    break;

                case STAT:
                    System.out.println("all Games " + text.substring(5));
                    break;

                case INFO:
                    Platform.runLater(() ->
                            GUIManager.getInstance().sendINFOtoGui(text.substring(5)));
                    break;
                case PLAR:
                    Platform.runLater(() ->
                            GUIManager.getInstance().getSeparateLobbyController().addPlayerArray(text.substring(5)));
                    break;
                case PLYR:
                    Platform.runLater(() ->
                            GUIManager.getInstance().getSeparateLobbyController().addPlayer(text.substring(5)));
                    break;
                case DPLR:
                    Platform.runLater(() ->
                            GUIManager.getInstance().getSeparateLobbyController().removePlayer(text.substring(5)));
                    break;
                case TEAM:
                    Platform.runLater(() ->
                            GUIManager.getInstance().getSeparateLobbyController().
                                    displayChangedTeams(text.substring(5)));
                    break;

                case STAR:
                    Platform.runLater(() ->
                    GUIManager.getInstance().getSeparateLobbyController().displayStart());
                    break;

                case DSTR:
                    Platform.runLater(() ->
                    GUIManager.getInstance().getSeparateLobbyController().cancelStart());
                    break;

                case SCOR:
                    try{
                        GUIManager.getInstance().getOptionsController();
                    } catch (Exception e) {
                        //Do nothing
                    }
                    break;

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("couldn`t process message from server: " + text);
        }
    }
}
