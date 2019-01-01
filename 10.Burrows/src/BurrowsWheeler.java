import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform, reading from standard input
    // and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray arr = new CircularSuffixArray(s);
        int len = s.length();
        char[] result = new char[len];
        int first = -1;
        for (int i = 0; i < len; i++) {
            int index = arr.index(i);
            if (index == 0) {
                first = i;
            }
            int r = (index - 1) % len;
            if (r < 0) r += len;

            result[i] = s.charAt(r);
        }

        BinaryStdOut.write(first);
        for (int i = 0; i < len; i++)
            BinaryStdOut.write(result[i]);

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int len = s.length();

        char[] front = new char[len];
        for (int i = 0; i < len; i++) front[i] = s.charAt(i);
        Arrays.sort(front);

        int[] next = next(s, front);

        int x = first;
        for (int i = 0; i < len; i++) {
            char c = front[x];
            BinaryStdOut.write(c);
            x = next[x];
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static int[] next(String s, char[] front) {
        int len = s.length();
        int[] n = new int[len];

        Map<Character, Queue<Integer>> pos = new HashMap<Character, Queue<Integer>>();

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            Queue<Integer> q = null;
            if (pos.containsKey(c)) {
                q = pos.get(c);
            }
            else {
                q = new Queue<Integer>();
            }
            q.enqueue(i);
            pos.put(c, q);
        }

        for (int i = 0; i < len; i++) {
            char c = front[i];
            n[i] = pos.get(c).dequeue();
        }
        return n;
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("+"))
            inverseTransform();
        else
            transform();
    }
}
