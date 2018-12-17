import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int[][] pixels;
    private double[][] energy;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        validate(picture, "Picture can\'t be null");

        this.width = picture.width();
        this.height = picture.height();
        this.pixels = pixels(picture);
        this.energy = energy();
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width, height);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                picture.setRGB(y, x, pixels[x][y]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validate(x, y);

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return 1000;

        int deltaX = delta(pixels[y][x + 1], pixels[y][x - 1]);
        int deltaY = delta(pixels[y + 1][x], pixels[y - 1][x]);
        return Math.sqrt(deltaX + deltaY);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(energy, height, width, false);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(energy, width, height, true);
    }

    private int[] findSeam(double[][] energy, int width, int height, boolean vertical) {
        DeluxeAcyclicSP path = new DeluxeAcyclicSP(energy, width, height, vertical);
        int[] seam = new int[height];
        int i = 0;
        for (int v : path.pathTo()) {
            seam[i++] = v;
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validate(seam, width, height);

        int[][] tmpPixels = new int[height - 1][width]; // temporary pixels
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height - 1; y++) {
                if (y < seam[x]) {
                    tmpPixels[y][x] = pixels[y][x];
                } else {
                    tmpPixels[y][x] = pixels[y + 1][x];
                }
            }
        }
        // update with new information
        pixels = tmpPixels;
        height = pixels.length;
        width = pixels[0].length;
        energy = energy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validate(seam, height, width);

        int[][] tmpPixels = new int[height][width - 1]; // temporary pixels
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width - 1; y++) {
                if (y < seam[x]) {
                    tmpPixels[x][y] = pixels[x][y];
                } else {
                    tmpPixels[x][y] = pixels[x][y + 1];
                }
            }
        }
        // update with new information
        pixels = tmpPixels;
        height = pixels.length;
        width = pixels[0].length;
        energy = energy();
    }


    /****************** Private Helper Methods *********************/

    private int[][] pixels(Picture picture) {
        int[][] pixels = new int[picture.height()][picture.width()];
        for (int x = 0; x < picture.height(); x++) {
            for (int y = 0; y < picture.width(); y++) {
                pixels[x][y] = picture.getRGB(y, x);
            }
        }
        return pixels;
    }

    private double[][] energy() {
        double[][] energy = new double[height][width];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (x == 0 || x == height - 1 || y == 0 || y == width - 1) {
                    energy[x][y] = 1000;
                } else {
                    energy[x][y] = energy(y, x);
                }
            }
        }
        return energy;
    }

    private int delta(int previous, int next) {

        int rx = Math.abs(get(previous, 16) - get(next, 16)); //
        int gx = Math.abs(get(previous, 8) - get(next, 8)); //
        int bx = Math.abs(get(previous, 0) - get(next, 0)); //

        return (rx * rx) + (gx * gx) + (bx * bx);
    }

    private int get(int rgb, int color) {
        return (rgb >> color) & 0xFF;
    }


    private void validate(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException();
    }

    private void validate(Object ob, String logs) {
        if (ob == null)
            throw new IllegalArgumentException(logs);
    }

    // seam validation
    private void validate(int[] seam, int height, int width) {
        validate(seam, "seams can\'t be null");

        if (seam.length != height)
            throw new IllegalArgumentException("invalid seam length, the length of the seam is "
                    + seam.length + "(but it should be " + height + ")");
        if (width <= 1)
            throw new IllegalArgumentException();

        for (int i = 1; i <= seam.length; i++) {
            if (seam[i - 1] < 0 || seam[i - 1] >= width)
                throw new IllegalArgumentException("entry " + seam[i - 1] +
                        " is not between " + 0 + "and " + width);

            if (i >= seam.length) continue;
            int distance = Math.abs(seam[i] - seam[i - 1]);
            if (distance > 1)
                throw new IllegalArgumentException("distance between pixel " + i +
                        " and pixel " + (i + 1) + " is " + distance);
        }
    }
}
