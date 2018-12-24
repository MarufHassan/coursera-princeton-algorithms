import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class DeluxeTrieSET implements Iterable<String> {
    private static final int R = 26;        // extended ASCII

    private Node root;      // root of trie
    private Node p;

    /**
     * Initializes an empty set of strings.
     */
    public DeluxeTrieSET() {
    }

    /**
     * Does the set contain the given key?
     *
     * @param key the key
     * @return {@code true} if the set contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        Node t = x;
        while (t != null) {
            if (d == key.length()) return t;
            char c = (char) (key.charAt(d++) - 'A');
            t = t.next[c];
        }
        return t;
    }

    /**
     * Adds the key to the set if it is not already present.
     *
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, null, key, 0);
    }

    private Node add(Node x, Node parent, String key, int d) {
        if (x == null) {
            x = new Node();
        }
        ;
        if (d == key.length()) {
            x.isString = true;
        } else {
            char c = (char) (key.charAt(d) - 'A');
            x.next[c] = add(x.next[c], parent, key, d + 1);
        }
        return x;
    }

    /**
     * Returns all of the keys in the set, as an iterator.
     * To iterate over all of the keys in a set named {@code set}, use the
     * foreach notation: {@code for (Key key : set)}.
     *
     * @return an iterator to all of the keys in the set
     */
    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     *
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     * as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.isString) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    private boolean search(String key) {
        Node x = root;
        for (int len = 0; x != null && len < key.length(); len++)
            x = x.next[key.charAt(len) - 'A'];

        if (x == null) return false;
        return true;
    }

    /*
     * given a prefix, is there any word in the dictionary
     * that starts with that prefix?
     */

    public boolean hasPrefix(String prefix) {
        return search(prefix);
    }

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }
}
