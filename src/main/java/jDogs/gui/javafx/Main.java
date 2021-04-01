package jDogs.gui.javafx;

import javafx.application.Application;

/**
 * this starts at the moment the client
 */
public class Main {

    /**
     * This is simply a wrapper to launch the {@link GUI} class.
     * The reason this class exists is documented in {@link GUI#main(String[])}
     */
    public static void main(String[] args) {

        Application.launch(ChatGui.class, args);
    }
}
