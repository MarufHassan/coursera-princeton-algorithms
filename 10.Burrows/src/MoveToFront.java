import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding,
    // reading from standard input and writing to standard output
    public static void encode() {
        char[] pos = new char[R];
        for (int i = 0; i < R; i++)
            pos[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int idx = indexOf(pos, c);
            BinaryStdOut.write((char)idx);
            for (int j = idx; j > 0; j--)
                pos[j] = pos[j - 1];
            pos[0] = c;
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static int indexOf(char[] pos, char c) {
        for (int i = 0; i < R; i++) {
            if (pos[i] == c)
                return i;
        }
        return -1;
    }

    // apply move-to-front decoding,
    // reading from standard input and writing to standard output
    public static void decode() {
        char[] pos = new char[R];
        for (int i = 0; i < R; i++)
            pos[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(pos[c]);
            char tmp = pos[c];
            for (int j = c; j > 0; j--) pos[j] = pos[j-1];
            pos[0] = tmp;
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else
            decode();
    }
}
