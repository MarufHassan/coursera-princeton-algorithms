import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private static final double CONFIDENCE_95 = 1.96;

	private final double[] meanValues;
	private double mean;
	private double stddev;
	private final int trials;

	// perform trials independent experiments on an n-by-n grid
	public PercolationStats(int n, int trials) {
		if (n <= 0 || trials <= 0)
			throw new IllegalArgumentException("n " + n + " or trials " + trials + " out of bounds");

		this.trials = trials;
		meanValues = new double[trials];

		for (int i = 0; i < trials; i++) {
			Percolation perc = new Percolation(n);
			while (!perc.percolates()) {
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				if (!perc.isOpen(row, col))
					perc.open(row, col);
			}
			meanValues[i] = (double) perc.numberOfOpenSites() / (n * n);
		}
		mean = StdStats.mean(meanValues);
		stddev = StdStats.stddev(meanValues);
	}

	// sample mean of percolation threshold
	public double mean() {
		return mean;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return stddev;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mean - (CONFIDENCE_95 * stddev) / Math.sqrt(trials);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mean + (CONFIDENCE_95 * stddev) / Math.sqrt(trials);
	}

	public static void main(String[] args) {
		// PercolationStats ps = new PercolationStats(5, 200);
	}
}
