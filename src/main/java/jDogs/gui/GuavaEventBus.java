package jDogs.gui;

import com.google.common.eventbus.EventBus;

public enum GuavaEventBus {
    //it seems 2 list elements work better, otherwise gradle tells me "GradleEventBus.INSTANCE doesn`t exist
    INSTANCE;

    private final EventBus eventBus = new EventBus();

    public void addToEventBus(Object object) {
        eventBus.register(object);
    }

    public void post(Object object) {
        eventBus.post(object);
    }


}
