import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validate(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        List<Point2D> list = new ArrayList<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                list.add(p);
            }
        }
        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validate(p);
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestp = null;
        for (Point2D current: points) {
            double distance = p.distanceTo(current);
            if (distance < minDistance) {
                minDistance = distance;
                nearestp = current;
            }
        }
        return nearestp;
    }

    private void validate(Object ob) {
        if (ob == null)
            throw new IllegalArgumentException();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
