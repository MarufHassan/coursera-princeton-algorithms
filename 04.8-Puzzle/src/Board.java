import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int n;
    private final int[][] tiles;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks[0].length;
        tiles = copy(blocks);
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) {
                    if (tiles[i][j] != xyTo1D(i + 1, j + 1)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = tiles[i][j];
                if (value != 0) {
                    int goalX = (value - 1) / n;
                    int goalY = (value - 1) % n;
                    distance += (Math.abs(goalX - i) + Math.abs(goalY - j));
                }
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twin = copy(tiles);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (tiles[i][j] != 0 && tiles[i][j + 1] != 0) {
                    exch(twin, i, j, i, j + 1);
                    return new Board(twin);
                }
            }
        }
        return null;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] directionX = {+1, 0, -1, 0};
        int[] directionY = {0, -1, 0, +1};

        int blankX = -1, blankY = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    blankY = i;
                    blankX = j;
                    break;
                }
            }
        }

        List<Board> boards = new ArrayList<Board>();
        for (int i = 0; i < directionX.length; i++) {
            int computedX = blankX + directionX[i];
            int computedY = blankY + directionY[i];
            if (computedX >= 0 && computedX < n && computedY >= 0 && computedY < n) {
                int[][] blocks = copy(tiles);
                exch(blocks, computedY, computedX, blankY, blankX);
                boards.add(new Board(blocks));
            }
        }

        return boards;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int[][] copy(int[][] blocks) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
        return copy;
    }

    private int xyTo1D(int x, int y) {
        return n * (x - 1) + y;
    }

    private void exch(int[][] blocks, int row1, int col1, int row2, int col2) {
        int t = blocks[row1][col1];
        blocks[row1][col1] = blocks[row2][col2];
        blocks[row2][col2] = t;
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        Board b;

        b = new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}});
        assert b.hamming() == 0;
        assert b.manhattan() == 0;

        b = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
        assert b.hamming() == 5;
        assert b.manhattan() == 10;

        b = new Board(new int[][]{{8, 7, 6}, {5, 4, 3}, {2, 1, 0}});
        assert b.hamming() == 8;
        assert b.manhattan() == 16;

        b = new Board(new int[][]{{0, 7, 6}, {5, 4, 3}, {2, 8, 1}});
        assert b.hamming() == 7;
        assert b.manhattan() == 14;
    }
}