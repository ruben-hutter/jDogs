package jDogs.gui;

/**
 * this class starts a blinker in separate lobby
 */
public class Blinker implements Runnable {

   public Blinker() {
    }

    @Override
    public void run() {
       GUIManager.getInstance().getSeparateLobbyController().blink();

    }
}
