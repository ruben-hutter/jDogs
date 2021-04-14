package jDogs.gui;

import jDogs.serverclient.clientside.Client;
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
        System.out.println("message game " + message);

        for (int i = 0; i < message.length(); i++) {
            if (Character.isWhitespace(message.charAt(i))) {
                idSeparator = i;
                break;
            }
        }

        for (int i = idSeparator + 1; i < message.length(); i++) {
            if (Character.isWhitespace(message.charAt(i))) {
                responsibleSeparator = i;
                break;
            }
        }

        for (int i = responsibleSeparator + 1; i < message.length(); i++) {
            if (Character.isWhitespace(message.charAt(i))) {
                sumSeparator = i;
                break;
            }
        }


        System.out.println(idSeparator);
        System.out.println(responsibleSeparator);
        System.out.println(sumSeparator);

        if (idSeparator == -1 || responsibleSeparator == -1 || sumSeparator == -1) {
            System.out.println("couldn t parse message " + message + ". has wrong format..");
            return null;
        }

        String id = message.substring(0,idSeparator);
        String responsible = message.substring(idSeparator + 1, responsibleSeparator);
        String sum = message.substring(responsibleSeparator + 1, sumSeparator);
        String needed = message.substring(sumSeparator + 1);


        return new OpenGame(id,responsible,sum,needed);
    }


    public static String sendWcht(String message) {
        for (int i = 0; i < message.length(); i++) {
            if (Character.isWhitespace(message.toCharArray()[i])) {
                if (!message.substring(i + 1).isBlank() || !message.substring(i + 1).isEmpty()) {
                    return message.substring(0, i) + " " + message.substring(i + 1);
                }
            }
        }
        return null;
    }

    public static String[] getArray(String activeUsers) {
        // activeUsers String contains at the first place: the number of nicknames in String
        // e.g. "3;Joe;Jonas;John"
        String[] array = new String[activeUsers.charAt(0)];
        int arrayCount = 0;
        int first = 2;
        for (int i = 2; i < activeUsers.length(); i++) {
            if(activeUsers.charAt(i) == ' ') {
                array[arrayCount] = activeUsers.substring(first,i);
                arrayCount++;
                first = i + 1;
            }
        }
        return array;
    }

    public static void parseGameSetUp(String gameInfo) {


    }
}
