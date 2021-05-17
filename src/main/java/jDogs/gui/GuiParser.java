package jDogs.gui;

import jDogs.serverclient.clientside.Client;
import java.sql.SQLOutput;

/**
 * this class contains all static methods to parse messages
 * into the right format to feed it to the window scene.
 */

public class GuiParser {

    /**
     * parses an opengame to display in gui
     * @param message a string containing an opengame
     * @return an opengame to display in gui
     */
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
        int totalSeparator = -1;

        for (int i = sumSeparator + 1; i < message.length(); i++) {
            if (Character.isWhitespace(message.charAt(i))) {
                totalSeparator = i;
                break;
            }
        }


        if (idSeparator == -1 || responsibleSeparator == -1 || sumSeparator == -1 || totalSeparator == -1) {
            System.out.println("couldn t parse message " + message + ". has wrong format..");
            return null;
        }

        String id = message.substring(0,idSeparator);
        String responsible = message.substring(idSeparator + 1, responsibleSeparator);
        String sum = message.substring(responsibleSeparator + 1, sumSeparator);
        String needed = message.substring(sumSeparator + 1, totalSeparator);
        String teamMode = message.substring(totalSeparator + 1);

        if (teamMode.equals("1")) {
            teamMode = "yes";
        } else {
            teamMode = "no";
        }
        return new OpenGame(id,responsible,sum,needed,teamMode);
    }

    /**
     * checks if a message is sent in the right format and sends it to server
     * @param message parse message to send as wcht message
     * @return a string to send to server
     */
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

    /**
     * a method to parse strings received in one long string
     * @param usersString is an array sent from server to parse,
     *                   usually contains usernames
     * @return an Array with the Strings which were sent
     */
    public static String[] getArray(String usersString) {
        // activeUsers String contains at the first place: the number of nicknames in String
        // e.g. "3 Joe Jonas John"

        String[] array = new String[usersString.charAt(0) - 48];
        int arrayCount = 0;
        int first = 2;
        for (int i = 2; i < usersString.length(); i++) {
            if(Character.isWhitespace(usersString.charAt(i))) {
                array[arrayCount] = usersString.substring(first,i);
                System.out.println(array[arrayCount]);
                arrayCount++;
                first = i + 1;
            }
        }
        array[arrayCount] = usersString.substring(first);
        return array;
    }

    public static void main(String[] args) {
        //TODO GAME SENDS A WRONG ARRAY it has 5 names but should have 4
        String game = "GAME 1 4 SDDD greG2 greg greG greG";
        String gameString ="2 Greg gregor";
        for (String name : getArray(gameString)) {
            System.out.println("name " + name);
        }
    }


    /**
     * returns the number in the abbreviation enum
     * @param colorAbb YELO, GREN, BLUE, REDD
     * @return 0-3
     */
    public static int getNumber(String colorAbb) {
        int count = 0;
        for (ColorAbbreviations colorAbbreviations1 : ColorAbbreviations.values()) {
            System.out.println("colorabb " + colorAbbreviations1.toString());
            if (colorAbb.equals(colorAbbreviations1.toString())) {
                return count;
            }
            count++;
        }
        return -1;
    }
}
