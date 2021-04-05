package jDogs.gui;

/**
 * this class is a wrapper for the guiManager in the Enum LoginEventHandler
 */
public class LoginHandler {

    GUIManager guiManager;
    public LoginHandler(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    public void getLoggedIn(String nickname) {
        guiManager.setScene(nickname);
    }

}
