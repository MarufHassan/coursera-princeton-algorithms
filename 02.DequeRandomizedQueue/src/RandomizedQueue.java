import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("New items can not be null.");
        if (n == items.length)
            resize(2 * items.length);
        items[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Empty queue");
        int r = StdRandom.uniform(n);
        Item item = items[r];
        exchange(items, r, --n);
        items[n] = null;
        if (n > 0 && n == items.length / 4)
            resize(items.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("Empty queue");
        int r = StdRandom.uniform(n);
        return items[r];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private Item[] itItems;
        private int p;

        public RandomIterator() {
            itItems = (Item[]) new Object[n];
            for (int i = 0; i < itItems.length; i++) {
                itItems[i] = items[i];
            }
            StdRandom.shuffle(itItems);
            p = 0;
        }

        public boolean hasNext() {
            return p < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return itItems[p++];
        }

    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    private void exchange(Item item[], int i, int j) {
        Item t = item[i];
        item[i] = item[j];
        item[j] = t;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        // RandomizedQueue<Integer> r = new RandomizedQueue<>();
    }
}