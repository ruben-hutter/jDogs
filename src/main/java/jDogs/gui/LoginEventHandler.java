package jDogs.gui;

/**
 * this enum allows to communicate from loginController to GuiManager
 */

public enum LoginEventHandler {

    INSTANCE;

    private LoginHandler loginHandler;

    public void setUp(GUIManager guiManager) {
        this.loginHandler = new LoginHandler(guiManager);
    }

    public void loggedIn(String nickname) {
        loginHandler.getLoggedIn(nickname);
    }






}
