package jDogs.gui;

import java.sql.SQLOutput;

/**
 * this class contains all static methods to parse messages
 * into the right format to feed it to the window scene.
 */

public class GuiParser {

    //returns openGames as OpenGameObjects
    public static OpenGame getOpenGame(String message) {

        int idSeparator = -1;
        int responsibleSeparator = -1;
        int sumSeparator = -1;


        for (int i = 0; i < message.length(); i++) {
            if(message.toCharArray()[i] == ';') {
                idSeparator = i;
                break;
            }
        }

        for (int i = idSeparator + 1; i < message.length(); i++) {
            if(message.toCharArray()[i] == ';') {
                responsibleSeparator = i;
                break;
            }
        }

        for (int i = responsibleSeparator + 1; i < message.length(); i++) {
            if(message.toCharArray()[i] == ';') {
                sumSeparator = i;
                break;
            }
        }


        System.out.println(idSeparator);
        System.out.println(responsibleSeparator);
        System.out.println(sumSeparator);

        if(idSeparator == -1 || responsibleSeparator == -1 || sumSeparator == -1) {
            System.out.println("couldn t parse message " + message + ". has wrong format..");
            return null;
        }

        String id = message.substring(0,idSeparator);
        String responsible = message.substring(idSeparator + 1, responsibleSeparator);
        String sum = message.substring(responsibleSeparator + 1, sumSeparator);
        String needed = message.substring(sumSeparator + 1);





        return new OpenGame(id,responsible,sum,needed);
    }
}
