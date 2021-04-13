package jDogs.gui;

public class Example {

    public static void main(String[] args) {

        /*
        String text = "Spiel1 4 Gregor Black Frida Green John Red Joe Yellow";

        int gameNameSeparator = 0;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                gameNameSeparator = i;
                break;
            }
        }

        int numberParticipants = Integer
                .parseInt(text.substring(gameNameSeparator + 1, gameNameSeparator + 2));
        int participantSeparator = gameNameSeparator;
        String[][] playersArray;
        playersArray = new String[numberParticipants][2];
        int volatileSeparator = gameNameSeparator;
        int i = 0;
        int j = volatileSeparator + 3;
        int first = 0;
        while (i < numberParticipants) {

            while (j < text.length()) {
                if (Character.isWhitespace(text.charAt(j))) {
                    if (first == 0) {
                        first = j;
                        if (i == numberParticipants - 1) {
                            playersArray[i][0] = text.substring(volatileSeparator, first);
                            playersArray[i][1] = text.substring(first);
                            i++;
                            break;

                        }
                    } else {
                        playersArray[i][0] = text.substring(volatileSeparator, first);
                        playersArray[i][1] = text.substring(first, j);
                        System.out.println(j);
                        i++;
                        first = 0;
                        break;
                    }
                }
                j++;
            }
            j++;
            volatileSeparator = j;
        }


        for (int z = 0; z < playersArray.length; z++) {
            int y = 0;
            System.out.println(playersArray[z][y]);
            System.out.println(playersArray[z][y + 1]);

        }
        */
        FieldOnBoard[] fieldsOnTrack = new FieldOnBoard[64];
        int trackCounter = 0;
        int leftestPoint = 2;
        int rightestPoint = 18;
        int highestPoint = 2;
        int lowestPoint = 18;

        for (int i = 1; i < 17; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(highestPoint, leftestPoint + i);
            System.out.println(fieldsOnTrack[trackCounter].getX() + " " + fieldsOnTrack[trackCounter].getY());
            trackCounter++;

        }

        //right side(2/18,3/18.....18/18)
        for (int i = 1; i < 17; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(highestPoint + i, rightestPoint);
            System.out.println(fieldsOnTrack[trackCounter].getX() + " " + fieldsOnTrack[trackCounter].getY());
            trackCounter++;
        }

        //side down
        for (int i = 1; i < 17; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(lowestPoint, rightestPoint - i);
            System.out.println(fieldsOnTrack[trackCounter].getX() + " " + fieldsOnTrack[trackCounter].getY());
            trackCounter++;
        }

        for (int i = 1; i < 17; i++) {
            fieldsOnTrack[trackCounter] = new FieldOnBoard(lowestPoint - i, leftestPoint);
            System.out.println(fieldsOnTrack[trackCounter].getX() + " " + fieldsOnTrack[trackCounter].getY());
            trackCounter++;
        }


    }
}
