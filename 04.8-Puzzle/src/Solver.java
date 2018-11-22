import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Node root;
    private final Board board;

    private class Node implements Comparable<Node> {
        Board board;
        int moves;
        int manhattan;
        Node prev;

        Node(Board board, Node prev) {
            this.board = board;
            this.prev = prev;
            this.manhattan = board.manhattan();

            if (this.prev == null) this.moves = 0;
            else this.moves = prev.moves + 1;
        }

        int priority() {
            return moves + manhattan;
        }

        public int compareTo(Node that) {
            return this.priority() - that.priority();
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        this.board = initial;
        root = solve(initial);
    }

    private Node solve(Board initial) {
        if (initial.dimension() == 1) return new Node(initial, null);

        MinPQ<Node> regularPQ = new MinPQ<Node>();
        MinPQ<Node> twinPQ = new MinPQ<Node>();

        Board twin = initial.twin();
        regularPQ.insert(new Node(initial, null));
        twinPQ.insert(new Node(twin, null));

        while (true) {
            Node node;
            node = regularPQ.delMin();
            if (node.board.isGoal()) {
                return node;
            }
            for (Board b : node.board.neighbors()) {
                if (node.prev == null || (!b.equals(node.prev.board))) {
                    regularPQ.insert(new Node(b, node));
                }
            }

            node = twinPQ.delMin();
            if (node.board.isGoal()) {
                return null;
            }
            for (Board b : node.board.neighbors()) {
                if (node.prev == null || (!b.equals(node.prev.board))) {
                    twinPQ.insert(new Node(b, node));
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return root != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return root.moves;
        else
            return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> solution = new Stack<Board>();

        Node x = root;
        while (x.prev != null) {
            solution.push(x.board);
            x = x.prev;
        }
        solution.push(board);
        return solution;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
