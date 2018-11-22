/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    // Initializes a new point.
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    //Draws this point to standard draw.
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // Draws the line segment between this point and the specified point
    // to standard draw.
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     */
    public double slopeTo(Point that) {
        double rise = that.y - this.y;
        double run = that.x - this.x;
        if (rise == 0 && run == 0) return Double.NEGATIVE_INFINITY; // degenerate
        if (run == 0) return Double.POSITIVE_INFINITY;  // vertical
        if (rise == 0) return +0.0;

        return (1.0 * rise) / run;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     * <p>
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (this.y > that.y) return +1;
        else if (this.y < that.y) return -1;
        else {
            return Integer.compare(this.x, that.x);
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeOrderComparator();
    }

    private class SlopeOrderComparator implements Comparator<Point> {

        public int compare(Point v, Point w) {
            double vSlope = slopeTo(v);
            double wSlope = slopeTo(w);
            return Double.compare(vSlope, wSlope);
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // Unit tests the Point data type.
    public static void main(String[] args) {
        Point p1, p2;
        p1 = new Point(4, 5);
        p2 = new Point(4, 5);
        assert p1.slopeTo(p2) == Double.NEGATIVE_INFINITY;

        p1 = new Point(450, -100);
        p2 = new Point(450, 200);
        assert p1.slopeTo(p2) == Double.POSITIVE_INFINITY;

        p1 = new Point(90, 200);
        p2 = new Point(80, 200);
        assert Double.valueOf(+0.0).equals(p1.slopeTo(p2));

        p1 = new Point(80, 200);
        p2 = new Point(90, 200);
        assert Double.valueOf(+0.0).equals(p1.slopeTo(p2));

        p1 = new Point(268, 168);
        p2 = new Point(432, 168);
        assert Double.valueOf(+0.0).equals(p1.slopeTo(p2));

        p1 = new Point(500, 405);
        p2 = new Point(450, 202);
        assert p1.slopeTo(p2) == 4.06;

        p1 = new Point(450, 1050);
        p2 = new Point(500, 554);
        assert p1.slopeTo(p2) == -9.92;

        p1 = new Point(500, 123);
        p2 = new Point(133, 553);
        assert p1.slopeTo(p2) == -1.1716621253405994;

        p1 = new Point(133, 123);
        p2 = new Point(432, 553);
        assert p1.slopeTo(p2) == 1.43812709030100334;

        p1 = new Point(337, 455);
        p2 = new Point(36, 341);
        assert p1.slopeTo(p2) == 0.3787375415282392;

        p1 = new Point(2675, 26173);
        p2 = new Point(22800, 23177);
        assert p1.slopeTo(p2) == -0.1488695652173913;

        p1 = new Point(5, 2);
        p2 = new Point(2, 4);
        assert p1.slopeTo(p2) == -0.6666666666666666;

        Point p = new Point(12, 247);
        Point q = new Point(491, 405);
        Point r = new Point(276, 309);
        assert p.slopeTo(q)    == 0.3298538622129436;
        assert p.slopeTo(r)    == 0.23484848484848486;
        assert p.slopeOrder().compare(q, r) == 1;

        p = new Point(22469, 23292);
        q = new Point(16278, 28269);
        r = new Point(2346, 5414);
        assert p.slopeTo(q)    == -0.8039089000161525;
        assert p.slopeTo(r)    == 0.8884361178750684;
        assert p.slopeOrder().compare(q, r) == -1;

        p = new Point(7, 1);
        q = new Point(9,0);
        r = new Point(0,1);
        assert p.slopeTo(q)    == -0.5;
        assert p.slopeTo(r)    == 0.0;
        assert p.slopeOrder().compare(q, r) == -1;
    }
}
