import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private static final int[] directionX = {+1, +1, 0, -1, -1, -1, 0, +1};
    private static final int[] directionY = {0, +1, +1, +1, 0, -1, -1, -1};

    private DeluxeTrieSET dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new IllegalArgumentException();
        this.dictionary = new DeluxeTrieSET();

        for (String s : dictionary) {
            if (s == null)
                throw new IllegalArgumentException();
            this.dictionary.add(s);
        }

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> words = new HashSet<String>();
        dfs(board, words);
        return words;
    }

    private void dfs(BoggleBoard board, Set<String> words) {
        int nRow = board.rows();
        int nCol = board.cols();
        boolean[][] marked = new boolean[nRow][nCol];
        for (int r = 0; r < nRow; r++) {
            for (int c = 0; c < nCol; c++) {
                dfs(board, marked, r, c, new StringBuilder(), words);
            }
        }
    }

    private void dfs(BoggleBoard b, boolean[][] marked, int row, int col, StringBuilder pre, Set<String> words) {
        if (marked[row][col])
            return;

        char letter = b.getLetter(row, col);
        pushLetter(pre, letter);
        String word = pre.toString();
        if (word.length() >= 3 && dictionary.contains(word))
            words.add(word);
        if (word.length() >= 3 && !dictionary.hasPrefix(word)) {
            popLetter(pre, letter);
            return;
        }

        marked[row][col] = true;
        for (int x = 0; x < directionX.length; x++) {
            int newRow = row + directionY[x];
            int newCol = col + directionX[x];
            if (!invalid(b.rows(), b.cols(), newRow, newCol)) {
                dfs(b, marked, newRow, newCol, pre, words);
            }
        }
        popLetter(pre, letter);
        marked[row][col] = false;
    }

    private void pushLetter(StringBuilder sb, char c) {
        sb.append(c);
        if (c == 'Q') sb.append('U');
    }

    private void popLetter(StringBuilder sb, char letter) {
        sb.setLength(sb.length() - 1);
        if (letter == 'Q') sb.setLength(sb.length() - 1);
    }

    private boolean invalid(int nRow, int nCol, int row, int col) {
        return row < 0 || row >= nRow || col < 0 || col >= nCol;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return dictionary.contains(word) ? score(word.length()) : 0;
    }

    private int score(int len) {
        switch (len) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }
}