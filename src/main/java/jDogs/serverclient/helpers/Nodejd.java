package jDogs.serverclient.helpers;
/**
 * A helper-class for QueueJD
 */
public class Nodejd {

    String value;
    Nodejd next;

    /**
     * construct a Node in QueueJD
     * @param s message
     */
    Nodejd(String s) {
        this.value = s;
    }
}