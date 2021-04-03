package jDogs.gui;

import com.google.common.eventbus.EventBus;

public enum GuavaEventBus {
    INSTANCE;

    private EventBus eventBus = new EventBus();

    public void addToEventBus(Object object) {
        eventBus.register(object);
    }

    public void post(Object object) {
        eventBus.post(object);
    }

}
