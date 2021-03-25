package JDogs.ServerClientEnvironment;

/***
 * this Queue is used to store messages from one thread and collect and process them by another thread
 */

import java.util.NoSuchElementException;

    public class Queue {

        Node head;
        Node tail;

        public Queue() {
            this.head = null;
            this.tail = null;

        }

        public synchronized void enqueue(String s) {

            Node n = new Node(s);

            if (head == null) {
                head = n;
            } else {
                tail.next = n;
            }
            tail = n;

        }

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
            //System.out.println("dequeuing " + s + " isempty " + isEmpty());
            return s;

        }

        public synchronized boolean isEmpty() {
            return (head == null);
        }


}
