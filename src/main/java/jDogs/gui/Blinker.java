package jDogs.gui;

/**
 * this class starts a blinker in separate lobby
 */
public class Blinker implements Runnable {
    int mode;
   public Blinker(String controller) {
       if (controller.equals("public")) {
           mode = 0;
       } else {
           mode = 1;
       }
    }

    @Override
    public void run() {
       if (mode == 0) {
           GUIManager.getInstance().getPubLobbyController().blink();
       } else {
           GUIManager.getInstance().getSeparateLobbyController().blink();
       }

    }
}
