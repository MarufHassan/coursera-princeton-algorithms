import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] sites;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private final int top;
    private final int bottom;
    private final int n;
    private int count; // number of opened sites

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n should be positive.");

        this.n = n;
        sites = new boolean[n + 1][n + 1];
        top = 0;
        bottom = n * n + 1;
        count = 0;

        uf = new WeightedQuickUnionUF(n * n + 2);
        ufFull = new WeightedQuickUnionUF(n * n + 1);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col))
            return;

        sites[row][col] = true;
        count++;

        int site = xyTo1D(row, col);
        if (row == 1) {
            uf.union(site, top);
            ufFull.union(site, top);
        }
        if (row == n)
            uf.union(site, bottom);

        int[] directionX = { 1, 0, -1, 0 };
        int[] directionY = { 0, 1, 0, -1 };

        for (int i = 0; i < 4; i++) {
            int nRow = row + directionY[i];
            int nCol = col + directionX[i];
            if (nRow >= 1 && nRow <= n && nCol >= 1 && nCol <= n) {
                if (isOpen(nRow, nCol)) {
                    uf.union(site, xyTo1D(nRow, nCol));
                    ufFull.union(site, xyTo1D(nRow, nCol));
                }
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sites[row][col];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return ufFull.connected(top, xyTo1D(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    private int xyTo1D(int x, int y) {
        return n * (x - 1) + y;
    }

    private void validate(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n)
            throw new IllegalArgumentException("row " + row + " or column " + col + " out of bounds");
    }
}
