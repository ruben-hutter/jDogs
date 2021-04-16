package jDogs;

public class Example {

    public static void main(String[] args) {
        String message = "maintenance maintenance2";
        int size = 2;
        String[] array = new String[size];
        int position = 0;
        int i = 0;
        int count = 0;
        while (count < size - 1) {

            if (Character.isWhitespace(message.charAt(i))) {
                array[count] = message.substring(position, i);
                System.out.println("ARRAY parse " + array[count]);
                position = i + 1;
                count++;
            }
            System.out.println(message.charAt(i));
            i++;
        }
        array[count - 1] = message.substring(position);
        System.out.println("ARRAY.length " + array.length);

    }
}