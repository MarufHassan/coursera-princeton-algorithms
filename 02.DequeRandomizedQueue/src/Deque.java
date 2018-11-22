import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node head = null;
    private Node tail = null;
    private int n;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("New item can\'t be null.");

        Node node = new Node();
        node.item = item;
        node.next = head;
        if (head != null)
            head.prev = node;
        head = node;
        if (tail == null)
            tail = head;
        n++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("new item can\'t be null.");

        Node node = new Node();
        node.item = item;
        node.prev = tail;
        if (tail != null)
            tail.next = node;
        tail = node;
        if (head == null)
            head = tail;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (head == null)
            throw new NoSuchElementException("Empty list");

        Item item = head.item;
        if (head == tail) {
            head = tail = null;
        } else {
            head.next.prev = null;
            head = head.next;
        }
        n--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (tail == null)
            throw new NoSuchElementException("Empty list");

        Item item = tail.item;
        if (head == tail) {
            head = tail = null;
        } else {
            tail.prev.next = null;
            tail = tail.prev;
        }
        n--;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Currently not supported");
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("No element found");
            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();
        d.addLast(40);
        d.addFirst(10);
        d.addLast(30);
        d.addFirst(20);
        for (int i : d) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println(d.removeFirst());
        System.out.println(d.removeLast());
        
    }
}
