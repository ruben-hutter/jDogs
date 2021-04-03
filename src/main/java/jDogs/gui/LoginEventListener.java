package jDogs.gui;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public class LoginEventListener {

    private GUIManager guiManager;

    public LoginEventListener(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void login(LoginEvent loginEvent) {

        //System.out.println("nickname: " + loginEvent.nickname);


        guiManager.goToLobby();
    }



}
