package jDogs.serverclient.helpers;

import java.util.NoSuchElementException;
//ok
/**
 * This Queue is used to store messages from one thread and collect and process them by another thread
 * synchronized methods to prevent conflicts betwixt threads
 */
public class QueueJD {

    Node head;
    Node tail;

    public QueueJD() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Adds an element to the Queue
     * @param s a given String message
     */
    public synchronized void enqueue(String s) {
        Node n = new Node(s);
        if (head == null) {
            head = n;
        } else {
            tail.next = n;
        }
        tail = n;
    }

    /**
     * Removes an element of the Queue
     * @return the last element of the Queue
     * @throws NoSuchElementException if the Queue is empty
     */
    public synchronized String dequeue() throws NoSuchElementException {
        if (head == null) {
            throw new NoSuchElementException("tried to dequeue element from empty queue");
        }
        String s = head.value;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
        }
        // System.out.println("dequeuing " + s + " isempty " + isEmpty());
        return s;
    }

    /**
     * Checks if the Queue is empty
     * @return true if empty
     */
    public synchronized boolean isEmpty() {
        return (head == null);
    }
}
