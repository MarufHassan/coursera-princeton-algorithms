import edu.princeton.cs.algs4.Stack;

public class DeluxeAcyclicSP {
    private int[] directionY = {+1, +1, +1}; // row direction
    private int[] directionX = {-1,  0, +1}; // column direction

    private double[][] distTo;
    private int[][] edgeTo;
    private int row, col;
    private int min;

    public DeluxeAcyclicSP(double[][] grid, int width, int height, boolean vertical) {
        row = height;
        col = width;

        distTo = new double[row][col];
        edgeTo = new int[row][col];

        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int y = 0; y < col; y++) {
            distTo[0][y] = 0;
        }

        traverse(grid, vertical);
        min = min(row, col);
    }

    private int min(int row, int col) {
        int min = 0;
        for (int y = 0; y < col; y++) {
            if (distTo[row - 1][y] < distTo[row - 1][min])
                min = y;
        }
        return min;
    }

    private void traverse(double[][] grid, boolean vertical) {
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                int fromRow, fromCol;
                if (vertical) {
                    fromRow = x; fromCol = y;
                }
                else {
                    fromRow = y; fromCol = x;
                }

                for (int i = 0; i < 3; i++) {
                    int toRow, toCol;
                    if (vertical) {
                        toRow = fromRow + directionY[i];
                        toCol = fromCol + directionX[i];
                    } else {
                        toRow = fromRow + directionX[i];
                        toCol = fromCol + directionY[i];
                    }
                    if (vertical) {
                        if (validate(toRow, toCol))
                            relax(grid[toRow][toCol], fromRow, fromCol, toRow, toCol);
                    } else {
                        if (validate(toCol, toRow)) {
                            relax(grid[toRow][toCol], fromCol, fromRow, toCol, toRow);
                        }
                    }
                }
            }
        }
    }

    private void relax(double value, int fromRow, int fromCol, int toRow, int toCol) {
        if (distTo[toRow][toCol] >= distTo[fromRow][fromCol] + value) {
            distTo[toRow][toCol] = distTo[fromRow][fromCol] + value;
            edgeTo[toRow][toCol] = fromCol;
        }
    }

    private boolean validate(int row, int col) {
        return (row >= 0 && row < this.row) && (col >= 0 && col < this.col);
    }

    public Iterable<Integer> pathTo() {
        Stack<Integer> path = new Stack<Integer>();
        path.push(min);

        int last = row - 1;
        int index = edgeTo[last][min];
        while (index != 0) {
            path.push(index);
            index = edgeTo[--last][index];
        }
        return path;
    }
}